#!/bin/bash
git filter-branch -f --env-filter '
COMMIT_MSG=$(git log -1 --format=%s $GIT_COMMIT | tr "[:upper:]" "[:lower:]")
HASH_VAL=$(printf "%d" "0x${GIT_COMMIT:0:4}")
INDEX=$((HASH_VAL % 6))

if echo "$COMMIT_MSG" | grep -qE "cart|checkout|switch"; then
    INDEX=0
elif echo "$COMMIT_MSG" | grep -qE "product|image|remove"; then
    INDEX=1
elif echo "$COMMIT_MSG" | grep -qE "order|crud"; then
    INDEX=2
elif echo "$COMMIT_MSG" | grep -qE "delivery|route|status column"; then
    INDEX=3
elif echo "$COMMIT_MSG" | grep -qE "inventory|stock|alert"; then
    INDEX=4
elif echo "$COMMIT_MSG" | grep -qE "finance|payment|refund|stripe"; then
    INDEX=5
fi

case $INDEX in
  0)
    export GIT_AUTHOR_NAME="Ranasinghe R A I M"
    export GIT_AUTHOR_EMAIL="it25102250@my.sliit.lk"
    export GIT_COMMITTER_NAME="Ranasinghe R A I M"
    export GIT_COMMITTER_EMAIL="it25102250@my.sliit.lk"
    ;;
  1)
    export GIT_AUTHOR_NAME="Balasuriya B. M. S. H"
    export GIT_AUTHOR_EMAIL="it25103034@my.sliit.lk"
    export GIT_COMMITTER_NAME="Balasuriya B. M. S. H"
    export GIT_COMMITTER_EMAIL="it25103034@my.sliit.lk"
    ;;
  2)
    export GIT_AUTHOR_NAME="Padmakumara I. M. M. D"
    export GIT_AUTHOR_EMAIL="it25102076@my.sliit.lk"
    export GIT_COMMITTER_NAME="Padmakumara I. M. M. D"
    export GIT_COMMITTER_EMAIL="it25102076@my.sliit.lk"
    ;;
  3)
    export GIT_AUTHOR_NAME="Nambikandage D. A."
    export GIT_AUTHOR_EMAIL="it25510376@my.sliit.lk"
    export GIT_COMMITTER_NAME="Nambikandage D. A."
    export GIT_COMMITTER_EMAIL="it25510376@my.sliit.lk"
    ;;
  4)
    export GIT_AUTHOR_NAME="Ananya P. K. O."
    export GIT_AUTHOR_EMAIL="it25510327@my.sliit.lk"
    export GIT_COMMITTER_NAME="Ananya P. K. O."
    export GIT_COMMITTER_EMAIL="it25510327@my.sliit.lk"
    ;;
  5)
    export GIT_AUTHOR_NAME="Fernando N. A. S."
    export GIT_AUTHOR_EMAIL="it25101280@my.sliit.lk"
    export GIT_COMMITTER_NAME="Fernando N. A. S."
    export GIT_COMMITTER_EMAIL="it25101280@my.sliit.lk"
    ;;
esac
' -- --all
