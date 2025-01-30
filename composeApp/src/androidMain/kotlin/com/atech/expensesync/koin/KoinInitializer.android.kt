package com.atech.expensesync.koin

import android.content.Context
import com.atech.expensesync.modules.androidModules
import com.atech.expensesync.modules.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

actual class KoinInitializer(
    private val context: Context
) {
    actual fun init() {
        startKoin {
            androidContext(context)
            modules(
                androidModules,
                commonModule,
                commonUIModules,
                androidUIModule
            )
        }
    }
}