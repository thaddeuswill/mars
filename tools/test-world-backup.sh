#!/usr/bin/env bash
# Test for world-backup.sh: backup, change, restore, retention, refusal. Prints PASS or FAIL.
set -euo pipefail
here="$(cd "$(dirname "$0")" && pwd)"
t="$(mktemp -d)"; trap 'rm -rf "$t"' EXIT
mkdir -p "$t/world/data" "$t/backups"
echo "pressure=0.6" > "$t/world/data/planet.txt"; touch "$t/world/session.lock"
fail() { echo "FAIL: $1"; exit 1; }
"$here/world-backup.sh" backup "$t/world" "$t/backups" 2 >/dev/null
tar -tzf "$t"/backups/*.tar.gz | grep -q session.lock && fail "session.lock was backed up"
echo "pressure=9.9" > "$t/world/data/planet.txt"; echo extra > "$t/world/data/new.txt"
b="$(ls "$t"/backups/*.tar.gz | head -1)"
"$here/world-backup.sh" restore "$t/world" "$b" >/dev/null
[ "$(cat "$t/world/data/planet.txt")" = "pressure=0.6" ] || fail "value not restored"
[ ! -e "$t/world/data/new.txt" ] || fail "later file survived the restore"
ls -d "$t"/world.before-restore-* >/dev/null || fail "the bad world was not kept"
[ "$(cat "$t"/world.before-restore-*/data/planet.txt)" = "pressure=9.9" ] || fail "kept world changed"
# retention: keep 2
for i in 1 2 3; do sleep 1; "$here/world-backup.sh" backup "$t/world" "$t/backups" 2 >/dev/null; done
[ "$(ls "$t"/backups/*.tar.gz | wc -l)" = "2" ] || fail "retention keeps 2"
# refusal: wrong folder name
mkdir "$t/other"
if "$here/world-backup.sh" restore "$t/other" "$b" >/dev/null 2>&1; then fail "restored into a differently named folder"; fi
echo "PASS world-backup.sh: backup, restore, kept bad world, retention, refusal"
