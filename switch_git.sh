#!/bin/bash
# ============================================================
# Lanka Fresh Mart — Git Author Switcher
# Group: 2026-Y2-S1-MLB-B1G1-02
# ============================================================
# Usage:  source switch_git.sh <member_number>
# Example: source switch_git.sh 1
# ============================================================

# ---- Group Members ----
# 1. Padmakumara I. M. M. D     — IT25102076
# 2. Balasuriya B. M. S. H      — IT25103034
# 3. Fernando N. A. S.           — IT25101280
# 4. Nambikandage D. A.          — IT25510376
# 5. Ranasinghe R A I M          — IT25102250
# 6. Ananya P. K. O.             — IT25510327

case "$1" in
  1)
    git config user.name "Padmakumara I. M. M. D"
    git config user.email "it25102076@my.sliit.lk"
    echo "✅ Switched to: Padmakumara I. M. M. D (IT25102076)"
    ;;
  2)
    git config user.name "Balasuriya B. M. S. H"
    git config user.email "it25103034@my.sliit.lk"
    echo "✅ Switched to: Balasuriya B. M. S. H (IT25103034)"
    ;;
  3)
    git config user.name "Fernando N. A. S."
    git config user.email "it25101280@my.sliit.lk"
    echo "✅ Switched to: Fernando N. A. S. (IT25101280)"
    ;;
  4)
    git config user.name "Nambikandage D. A."
    git config user.email "it25510376@my.sliit.lk"
    echo "✅ Switched to: Nambikandage D. A. (IT25510376)"
    ;;
  5)
    git config user.name "Ranasinghe R A I M"
    git config user.email "it25102250@my.sliit.lk"
    echo "✅ Switched to: Ranasinghe R A I M (IT25102250)"
    ;;
  6)
    git config user.name "Ananya P. K. O."
    git config user.email "it25510327@my.sliit.lk"
    echo "✅ Switched to: Ananya P. K. O. (IT25510327)"
    ;;
  *)
    echo "❌ Invalid member number!"
    echo ""
    echo "Usage: source switch_git.sh <number>"
    echo ""
    echo "Members:"
    echo "  1 — Padmakumara I. M. M. D  (IT25102076)"
    echo "  2 — Balasuriya B. M. S. H   (IT25103034)"
    echo "  3 — Fernando N. A. S.        (IT25101280)"
    echo "  4 — Nambikandage D. A.       (IT25510376)"
    echo "  5 — Ranasinghe R A I M       (IT25102250)"
    echo "  6 — Ananya P. K. O.          (IT25510327)"
    ;;
esac
