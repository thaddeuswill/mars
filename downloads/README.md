# downloads

The latest verified build of the Mars Terraforming mod, for [INSTALL.md](../INSTALL.md).

| File | Built from | Verified by the coordinator |
|---|---|---|
| `mars-terraforming-0.1.0-stage1.jar` | commit `98f007e` (orbital resupply) | `./gradlew spotlessCheck build runGameTestServer`: 413 unit tests, 0 failures; `MARS-SELFTEST RESULT PASS passed=182` |

Put this one file in your Minecraft `mods` folder (see INSTALL.md section 5). It needs NeoForge 21.1.x for Minecraft 1.21.1.
**Not tested** from a real `mods` folder, on Windows or macOS, or on a dedicated server.
The jar's inner mod id and file name from the build are `mars-0.1.0-stage1.jar`; this copy is renamed only for clarity. If NeoForge complains about the name, rename it back to `mars-0.1.0-stage1.jar`.
