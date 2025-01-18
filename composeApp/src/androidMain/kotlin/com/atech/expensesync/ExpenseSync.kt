package com.atech.expensesync

import android.app.Application
import com.atech.expensesync.modules.KoinInitializer

class ExpenseSync : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
    }
}