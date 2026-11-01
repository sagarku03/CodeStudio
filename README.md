# CodeStudio

CodeStudio is a full-featured, on-device mobile IDE and AI-assisted development workspace for Android. It combines a modern Jetpack Compose interface with Sora Editor, a built-in terminal emulator running native Linux rootfs/PRoot environments, Git integration, and multi-agent AI coding support (Gemini & Claude Code).

---

## Key Features

- **Mobile Code Editor**: Built with Sora Editor supporting syntax highlighting (Java, Kotlin, Python, JavaScript, HTML, C/C++, etc.), textmate grammar rules, auto-indentation, and file management.
- **Embedded Linux Terminal**: Built-in Termux/PRoot-based terminal environment with native PTY support, shell sessions, package management, and local developer toolchains on ARM64.
- **AI Pair Programming**: Integrated support for Gemini models via Google OAuth and API key configurations, alongside Claude Code and automated agents with live diagnostics and tool approval workflows.
- **In-App Browser & Tools**: Integrated Android WebView runtime with extension host APIs, devtools inspection, and automation capabilities.
- **Source Control**: Integrated Git source control supporting clone, stage, commit, push, branch switching, and log inspection.
- **Backup & Safe Storage**: Safe Space Manager and archive tools for projects, terminal sessions, and workspace data.

---

## Prerequisites

- **Android SDK**: `compileSdk 36`, `minSdk 24`, `targetSdk 36`
- **JDK**: Java Development Kit 21
- **Gradle**: Gradle wrapper version as in gradle/wrapper/gradle-wrapper.properties
- **Architecture**: `arm64-v8a` recommended for native terminal rootfs

---

## Build Instructions

### Building Debug APK Locally

```bash
# Clone the repository
git clone https://github.com/sagarku03/CodeStudio.git
cd CodeStudio

# Build debug APK
gradle assembleDebug
```

The resulting debug APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

### Building Release APK

To build a signed release APK, configure the following environment variables (or specify them in `.env`):

```bash
export KEYSTORE_PATH="/path/to/release.jks"
export STORE_PASSWORD="your_store_password"
export KEY_PASSWORD="your_key_password"

gradle assembleRelease
```

The resulting signed release APK will be located at:
`app/build/outputs/apk/release/app-release.apk`

---

## GitHub Actions Automated Releases

The repository includes a GitHub Actions workflow (`.github/workflows/release.yml`) that automatically builds and publishes release APKs when pushing to `main` or tagging a release:

1. **Configure Repository Secrets** (in **Settings > Secrets and variables > Actions**):
   - `KEYSTORE_BASE64`: Base64-encoded string of your signing keystore (`.jks` file)
   - `STORE_PASSWORD`: Keystore store password
   - `KEY_PASSWORD`: Key alias password
   - `GEMINI_API_KEY`: (Optional) Gemini API key for BuildConfig
   - `GOOGLE_OAUTH_CLIENT_ID`: (Optional) Google OAuth Desktop Client ID
   - `GOOGLE_OAUTH_CLIENT_SECRET`: (Optional) Google OAuth Desktop Client Secret

2. **Trigger a Release**:
   - Push to `main` or push a tag (e.g. `v1.0.0`).
   - The workflow reads the version from `version.json`, builds the APK, and creates an official GitHub Release with `CodeStudio-v{version_name}.apk` attached.

---

## Configuration & Environment Variables

Create a local `.env` file (based on `.env.example`) to inject API keys into `BuildConfig`:

```properties
GEMINI_API_KEY=
GOOGLE_OAUTH_CLIENT_ID=
GOOGLE_OAUTH_CLIENT_SECRET=
```

---

## Credits & Acknowledgements

- **Original Project**: [AntiGem](https://github.com/santoshkurmi/antigem) by Santosh Kurmi
- **Terminal Engine**: [termux-app](https://github.com/termux/termux-app) / Termux Terminal Emulator
- **Editor Engine**: [Sora Editor](https://github.com/Rosemoe/sora-editor) by Rosemoe
- **Protobuf Runtime**: [Square Wire](https://github.com/square/wire)

---

## License

Copyright (C) 2026 Santosh Kurmi (original project "AntiGem", https://github.com/santoshkurmi/antigem)
Modifications Copyright (C) 2026 Sagar (sagarku03)

Licensed under the GNU General Public License v3.0 or any later version (see LICENSE). This is a modified version of AntiGem.
