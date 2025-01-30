package com.atech.expensesync.koin

import com.atech.expensesync.modules.commonModule
import com.atech.expensesync.modules.jvmModule
import org.koin.core.context.startKoin

actual class KoinInitializer {
    actual fun init() {
        startKoin {
            modules(
                jvmModule,
                commonModule,
                commonUIModules,
                jvmUIModule
            )
        }
    }
}