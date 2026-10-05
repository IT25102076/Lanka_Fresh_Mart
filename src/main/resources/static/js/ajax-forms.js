/**
 * AJAX form handler for staff management pages.
 *
 * Opt-in: add the `data-ajax-forms` attribute to the page's <main> element.
 * Every POST form inside that <main> is sent with fetch() instead of a full
 * page reload. The server still does its normal POST -> redirect -> GET, and
 * we swap the freshly rendered `.container` into the current page.
 * Because the page never reloads, the scroll position does not move at all.
 *
 * Flash messages (.alert-lfm) on these pages are shown as floating toasts
 * instead of inline banners, so they never push the table down.
 */
(function () {
    const main = document.querySelector('main[data-ajax-forms]');
    if (!main) return;

    /* ---------- Toast notifications ---------- */
    function getStack() {
        let stack = document.querySelector('.lfm-toast-stack');
        if (!stack) {
            stack = document.createElement('div');
            stack.className = 'lfm-toast-stack';
            stack.setAttribute('aria-live', 'polite');
            document.body.appendChild(stack);
        }
        return stack;
    }

    function showToast(message, type) {
        const isError = type === 'danger';
        const toast = document.createElement('div');
        toast.className = 'lfm-toast ' + (isError ? 'lfm-toast-danger' : 'lfm-toast-success');
        toast.setAttribute('role', isError ? 'alert' : 'status');

        const icon = document.createElement('i');
        icon.className = 'bi ' + (isError ? 'bi-exclamation-octagon-fill' : 'bi-check-circle-fill') + ' lfm-toast-icon';

        const text = document.createElement('div');
        text.className = 'lfm-toast-text';
        text.textContent = message;

        const close = document.createElement('button');
        close.type = 'button';
        close.className = 'lfm-toast-close';
        close.setAttribute('aria-label', 'Close');
        close.innerHTML = '<i class="bi bi-x-lg"></i>';

        const bar = document.createElement('div');
        bar.className = 'lfm-toast-bar';

        toast.append(icon, text, close, bar);
        getStack().appendChild(toast);

        const dismiss = () => {
            if (toast.classList.contains('is-leaving')) return;
            toast.classList.add('is-leaving');
            toast.addEventListener('animationend', () => toast.remove(), { once: true });
        };
        close.addEventListener('click', dismiss);
        const timer = setTimeout(dismiss, isError ? 6000 : 3500);
        toast.addEventListener('mouseenter', () => { clearTimeout(timer); toast.classList.add('is-paused'); });
        toast.addEventListener('mouseleave', () => setTimeout(dismiss, 1500));
    }

    /** Pull inline flash alerts (success / error only) out of `root` and show them as toasts. */
    function alertsToToasts(root) {
        root.querySelectorAll('.alert-lfm-success, .alert-lfm-danger').forEach(el => {
            const msg = el.textContent.trim();
            const type = el.classList.contains('alert-lfm-danger') ? 'danger' : 'success';
            el.remove();
            if (msg) showToast(msg, type);
        });
    }

    // Messages rendered on a normal page load (e.g. first visit / fallback submit)
    alertsToToasts(main);

    /** Close any open Bootstrap modal and remove its backdrop before the DOM swap. */
    function closeOpenModals() {
        main.querySelectorAll('.modal').forEach(el => {
            if (window.bootstrap && bootstrap.Modal) {
                const inst = bootstrap.Modal.getInstance(el);
                if (inst) inst.dispose();
            }
        });
        document.querySelectorAll('.modal-backdrop').forEach(b => b.remove());
        document.body.classList.remove('modal-open');
        document.body.style.removeProperty('overflow');
        document.body.style.removeProperty('padding-right');
    }

    /* ---------- In-place form submits ---------- */
    let busy = false;

    document.addEventListener('submit', async function (e) {
        const form = e.target;
        if (!(form instanceof HTMLFormElement) || !main.contains(form)) return;
        if ((form.getAttribute('method') || 'get').toLowerCase() !== 'post') return;
        // We now rely on layout's global data-confirm listener to intercept and handle confirmations
        if (e.defaultPrevented) return;

        e.preventDefault();
        if (busy) return;
        busy = true;
        main.classList.add('ajax-busy');

        try {
            const body = new FormData(form, e.submitter || undefined);
            const res = await fetch(form.action, {
                method: 'POST',
                body: body,
                credentials: 'same-origin'
            });
            const html = await res.text();
            const doc = new DOMParser().parseFromString(html, 'text/html');

            const newMain = doc.querySelector('main[data-ajax-forms]');

            if (!res.ok || !newMain) {
                // Unexpected response (e.g. session expired -> login page)
                window.location.href = res.url || window.location.href;
                return;
            }

            closeOpenModals();

            const scrollY = window.scrollY;
            main.classList.add('no-animations');

            // Replace every content node in <main> (container, modals, ...) but keep
            // the page's existing <script> tags, which have already run.
            const isContent = n => n.nodeType === 1 && n.tagName !== 'SCRIPT';
            const oldNodes = [...main.childNodes].filter(isContent);
            const newNodes = [...newMain.childNodes].filter(isContent)
                .map(n => document.importNode(n, true));
            newNodes.forEach(n => alertsToToasts(n));   // remove banners before insert

            const anchor = oldNodes[0] || main.firstChild;
            newNodes.forEach(n => main.insertBefore(n, anchor));
            oldNodes.forEach(n => n.remove());

            // Guard against any layout shift moving the viewport
            window.scrollTo({ top: scrollY, behavior: 'instant' });
        } catch (err) {
            console.error('AJAX form submit failed, falling back to normal submit', err);
            HTMLFormElement.prototype.submit.call(form);
        } finally {
            busy = false;
            main.classList.remove('ajax-busy');
        }
    });
})();
