# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & run

Everything is driven by the Gradle wrapper from the repo root.

- Run from sources: `./gradlew run` (alias for `runOsgi`). Use `./gradlew clean run` after suspicious compilation errors — bundle staging into `build/osgi/` is sticky.
- Debug: `./gradlew run -Pdebug=5005 -Psuspend=n` (defaults to `suspend=y`).
- Build a single sub-project: `./gradlew :mucommander-protocol-ftp:build`.
- Tests: `./gradlew test`. Single module: `./gradlew :mucommander-core:test`. Single class: `./gradlew :mucommander-core:test --tests com.mucommander.SomeTest`.
- JDK: source level is **Java 11** (`--release 11` enforced for every subproject), but CI in `.github/workflows/tests.yaml` uses JDK 17 — build/test with 17+ locally to match.
- Packaging targets (all use `jlink` + `jpackage`, output in `build/distributions/`): `dmg` + `macosPortable` (mac), `msi` / `winExe` / `windowsPortable` (win, `winExe` needs Inno Setup on PATH), `rpm` / `deb` / `linuxPortable` (linux). Each takes `-Parch=x86_64|aarch64`. Cross-arch builds: pass `-PjmodsPath=/path/to/<target-arch>/jdk/lib/jmods` so `jlink` reads jmods for the target architecture.

## Architecture

muCommander is a **single Java application composed of OSGi bundles** running on Apache Felix. This is the dominant fact about the codebase — most "where does X live" questions are answered by the bundle layout.

### Two source roots

- `src/` (top-level, `com.mucommander.main`) — the **launcher only**. `muCommander.java` is a fork of the Felix launcher: it parses CLI args via JCommander, locates `bundle/` and `app/` next to its jar, boots a Felix framework, and auto-deploys every bundle. The packaged jar (defined by `jar { ... }` in the root `build.gradle`) is what `jpackage` wraps into native installers and portables. Main-Class everywhere is `com.mucommander.main.muCommander`.
- `mucommander-*/` — the actual application, split across ~50 sub-projects listed in `settings.gradle`. Each is its own Gradle subproject and produces an OSGi bundle (`biz.aQute.bnd.builder` plugin generates the manifest from `bnd { ... }` in its `build.gradle`).

### Bundle composition is configured in the root build.gradle

The `osgiRuntime project(...)` declarations in the root `build.gradle` are the **canonical list of bundles deployed at runtime**. `createBundlesDir` stages them into `build/osgi/{app,bundle,conf}/`:

- `app/mucommander-core-*.jar` — started explicitly via `felix.auto.start.2`.
- `bundle/*.jar` — auto-deployed by Felix on startup.
- `conf/logback.xml` — logging config (path injected via `logback.configurationFile` system property in `muCommander.main`).

When adding a new module: include it in `settings.gradle`, add an `osgiRuntime project(':...')` line in the root `build.gradle`, and ensure your `build.gradle` declares a `Bundle-Activator` if it needs lifecycle hooks. Without the `osgiRuntime` line, the module is invisible at runtime even though it compiles.

### Module groups

- `mucommander-commons-*` — pure-Java libraries (file abstraction, IO, conf, collections, util, runtime detection). `commons-file` is the foundation: `AbstractFile`, `FileFactory`, `SchemeHandler`.
- `mucommander-protocol-*` — one bundle per remote filesystem (FTP, SFTP, SMB, S3, Dropbox, GDrive, OneDrive, NFS, ADB, oVirt, vSphere, registry, Hadoop, HTTP). Each registers `ProtocolProvider` services and optionally a `ProtocolPanelProvider` (UI) declared in `mucommander-protocol-api`.
- `mucommander-format-*` — archive formats (zip, tar, rar, 7z, gzip, bzip2, xz, ar, cpio, iso, lst, rpm, libguestfs). `mucommander-archiver` ties them together.
- `mucommander-viewer-*` — file viewers (text, image, pdf, binary), implementing the `mucommander-viewer-api` SPI.
- `mucommander-os-*` — per-OS desktop integration (`os-macos`, `os-linux`, `os-win`, `os-openvms`) implementing `mucommander-os-api`. Packaging tasks delete the wrong-platform OS bundles (e.g. `tgz` excludes `mucommander-os-win*`/`mucommander-os-macos*`).
- `mucommander-core` — the UI shell. Only this module exports `com.mucommander.ui.*` packages (see the long `Export-Package` list in `mucommander-core/build.gradle`). The two-pane UI lives in `com.mucommander.ui.main` (`MainFrame`, `FolderPanel`, `LocationBar`, …).
- Vendored libraries with custom OSGi packaging: `apache-bzip2`, `jetbrains-jediterm`, `sevenzipjbindings`, `gson`, `kotlin-reflect`, `sun-net-www`.

### Plugin discovery (very important when adding features)

`mucommander-core` does **not** import other modules at compile time (`compileOnly` only — see `mucommander-core/build.gradle`). It discovers them at runtime through OSGi service trackers in `com.mucommander.osgi.*`:

- `FileViewerServiceTracker` / `FileEditorServiceTracker` — picks up viewer/editor bundles.
- `OperatingSystemServiceTracker` — picks up the active `os-*` bundle.
- `BrowsableItemsMenuServiceTracker` — extension menu items.
- `ProtocolPanelProviderTracker` (`com.mucommander.ui.main.osgi`) — connect-to-server panels.
- `TranslationTracker` (`mucommander-translator`) — i18n bundles.

`com.mucommander.Activator` (in `mucommander-core`) is where all of this is wired up at bundle start. New protocols/viewers/OS adapters integrate by registering OSGi services in their own `Activator`, **not** by editing core.

### Configuration & preferences

- `mucommander-preferences` defines `MuConfigurations` / `MuPreference` and the snapshot mechanism (`MuSnapshot`, `SearchSnapshot`, `EditorSnapshot`, `ViewerSnapshot`).
- User prefs folder: `UserPreferencesDir.getDefaultPreferencesFolder()`, overridable with `--preferences <dir>` or by placing a `.portable` marker next to the launcher jar (portable mode).
- Felix cache lives in `java.io.tmpdir/mucommander-felix-cache-<user>`.

### Tests

Mixed frameworks — match what's already in the module:
- `mucommander-core` and `mucommander-commons-*` use **TestNG** (`testng.xml` in `src/test/resources`).
- Newer modules (`mucommander-protocol-*`, `mucommander-format-*`, `mucommander-protocol-api`, …) use **JUnit Jupiter** with `useJUnitPlatform()`.
- A few modules declare both — pay attention to the `test { useTestNG() | useJUnitPlatform() }` block in the relevant `build.gradle`.

### Code style

An Eclipse-format file is checked in at `mucommander-code-format.xml` — apply it before submitting changes touching pre-existing files.

### Translations

`mucommander-translator/src/main/resources/dictionary_<locale>.properties`. The repo is set up for Zanata (`zanata.xml`); contributors usually translate via the Zanata workflow, not direct PRs against the dictionaries.
