![Code](https://tokei.rs/b1/github/aiyu-ayaan/ExpenseSync?category=code)

[![wakatime](https://wakatime.com/badge/user/3a4240f0-6bea-4626-be2a-1129790e4336/project/797d9aa9-4aad-430c-96d4-010b2de71c89.svg)](https://wakatime.com/badge/user/3a4240f0-6bea-4626-be2a-1129790e4336/project/797d9aa9-4aad-430c-96d4-010b2de71c89)

This is a Kotlin Multiplatform project targeting Android, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
