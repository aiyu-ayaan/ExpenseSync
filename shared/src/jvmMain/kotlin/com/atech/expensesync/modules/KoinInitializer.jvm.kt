package com.atech.expensesync.modules

import org.koin.core.context.startKoin

actual class KoinInitializer {
    actual fun init() {
        startKoin {
            modules(
                jvmModule,
                commonModule
            )
        }
    }
}