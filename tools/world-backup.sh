#!/usr/bin/env bash
# Whole-world backup and restore for a Mars Terraforming server (ARCHITECTURE.md 7.4a).
# Run it with the server STOPPED (before class: backup; after a bad session: restore).
#   world-backup.sh backup  <world-dir> <backup-dir> [keep=10]
#   world-backup.sh list    <backup-dir>
#   world-backup.sh restore <world-dir> <backup-file>
# Restore never deletes: the current world is moved to <world-dir>.before-restore-<time>.
# This covers everything in the world folder (blocks, items, player inventory, machines from any
# mod, planet values). It is all or nothing: there is no per-planet restore here.
set -euo pipefail

usage() { sed -n '2,9p' "$0" | sed 's/^# \{0,1\}//'; exit 2; }
cmd="${1:-}"; [ -n "$cmd" ] || usage

case "$cmd" in
  backup)
    world="${2:?world dir}"; dest="${3:?backup dir}"; keep="${4:-10}"
    [ -d "$world" ] || { echo "No such world folder: $world" >&2; exit 1; }
    mkdir -p "$dest"
    stamp="$(date +%Y%m%d-%H%M%S)"
    file="$dest/${stamp}_$(basename "$world").tar.gz"
    tar -C "$(dirname "$world")" --exclude='session.lock' -czf "$file" "$(basename "$world")"
    echo "Backup written: $file ($(du -h "$file" | cut -f1))"
    # Keep only the newest $keep backups.
    ls -1 "$dest"/*_"$(basename "$world")".tar.gz | sort | head -n -"$keep" | xargs -r rm -f
    ;;
  list)
    dest="${2:?backup dir}"
    ls -1 "$dest"/*.tar.gz 2>/dev/null || echo "No backups in $dest"
    ;;
  restore)
    world="${2:?world dir}"; file="${3:?backup file}"
    [ -f "$file" ] || { echo "No such backup: $file" >&2; exit 1; }
    parent="$(dirname "$world")"
    top="$(tar -tzf "$file" | head -1 | cut -d/ -f1)"
    [ "$top" = "$(basename "$world")" ] || { echo "Backup holds '$top', not '$(basename "$world")'. Refusing." >&2; exit 1; }
    if [ -d "$world" ]; then
      moved="$world.before-restore-$(date +%Y%m%d-%H%M%S)"
      mv "$world" "$moved"
      echo "Current world kept at: $moved"
    fi
    tar -C "$parent" -xzf "$file"
    echo "Restored $world from $file"
    ;;
  *) usage ;;
esac
