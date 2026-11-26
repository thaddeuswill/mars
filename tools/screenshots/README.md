# tools/screenshots

Real in-game screenshots with the dev-only helper in the companion mod (`mars.client.dev.DevTools`, active only with `-Dmars.devtools=true`). Owner: Technical Lead.

- `run-shots.sh [script]`: Linux. Starts the real client under Xvfb with Mesa software OpenGL, creates a fresh creative test world, runs the script, saves PNGs to `out/<timestamp>/` (and `mod/run-shots/screenshots/`). Offline dev account only.
- `stage1.shots`: the Stage 1 script. Steps: `cmd <command>`, `wait <ticks>`, `view x y z yaw pitch`, `hud on|off`, `fps`, `shot <name>`, `quit`.
- On your own computer (any OS): from `mod/`, run `./gradlew runShotsClient` (a normal window; no Xvfb needed). In any dev client started with `-Dmars.devtools=true`, `/mars_shot <name>` and `/mars_view x y z yaw pitch` also work by hand.

`out/` keeps each run's PNGs. Two early runs are kept on purpose as evidence: one stuck on the first-launch welcome screen, one showing the `/mars go` "fell out of the world" bug that the boot checks missed.
