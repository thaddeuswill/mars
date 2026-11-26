#!/usr/bin/env bash
# Takes Stage 1 in-game screenshots with the real Minecraft client under a virtual display.
# Linux only. Needs: Xvfb (xvfb-run), Mesa (software OpenGL), Java 21. Offline dev account only.
# Usage: tools/screenshots/run-shots.sh [script.shots]
# Output: mod/run-shots/screenshots/*.png and a copy in tools/screenshots/out/<timestamp>/.
set -euo pipefail
here="$(cd "$(dirname "$0")" && pwd)"
repo="$(cd "$here/../.." && pwd)"
script="${1:-$here/stage1.shots}"
out="$here/out/$(date +%Y%m%d-%H%M%S)"
mkdir -p "$out"

# Software rendering (Mesa llvmpipe). Slow, but enough for layout and colour checks.
export LIBGL_ALWAYS_SOFTWARE=1
export GALLIUM_DRIVER=llvmpipe

cd "$repo/mod"
start=$(date +%s)
set +e
timeout 1500 xvfb-run -a -s "-screen 0 1280x720x24" \
  ./gradlew runShotsClient --console=plain -PmarsShotScript="$script" 2>&1 | tee "$out/client.log" \
  | grep --line-buffered -E "MARS-SHOTS|OpenGL|GL version|ERROR|Exception|BUILD"
rc=${PIPESTATUS[0]}
set -e
echo "elapsed=$(( $(date +%s) - start ))s rc=$rc"
cp run-shots/screenshots/*.png "$out/" 2>/dev/null || true
ls -la "$out"
exit "$rc"
