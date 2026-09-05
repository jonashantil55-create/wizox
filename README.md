# w1zox – Meteor addon for Minecraft 1.21.11

Client-side Meteor addon with three independent modules:

- **Admin List** – compares a user-maintained, comma-separated list of names with players the client can currently see. Add the HUD element `w1zox/Admin List` in Meteor's HUD editor.
- **Player Chunks** – draws the chunk borders of players the server has made visible to the client.
- **Spawner Markers** – incrementally scans blocks in already-loaded client chunks near the player and highlights normal spawners only. Trial spawners are excluded. It never sends additional server requests or queries unloaded chunks.

The default presentation uses a compact dark admin HUD card and red world markers/columns. Position the `w1zox/Admin List` HUD element in the upper-right corner in Meteor's HUD editor to match the reference layout.

## Installation

1. Install Fabric Loader, Meteor Client for Minecraft 1.21.11, and Java 21.
2. Copy `w1zox-1.0.0.jar` from `build/libs` into the Minecraft `mods` folder.
3. In Meteor's module screen, use the `w1zox` category to enable/configure modules.

## Build

Run `gradlew.bat build` on Windows (or `./gradlew build` on Linux/macOS). The output will be created in `build/libs`.

### Build without installing anything: GitHub Actions

This project includes `.github/workflows/build.yml`. Upload the unzipped project folder to a GitHub repository, open **Actions**, choose **Build w1zox JAR**, and select **Run workflow**. When it finishes, open that run and download the `w1zox-jar` artifact. It contains the installable JAR.

## Scope and privacy

This addon only reads client-visible world/player state. It contains no anti-cheat bypasses, exploit logic, packet manipulation, server-permission probing, or protection circumvention. A vanilla/Fabric client cannot reliably discover server admin permissions, nor can it prove whether a normal spawner was player-placed rather than naturally generated, so the admin list is intentionally name-based and the spawner marker covers all visible normal spawners.
