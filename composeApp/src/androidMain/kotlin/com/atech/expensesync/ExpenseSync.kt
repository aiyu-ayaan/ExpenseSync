package com.atech.expensesync

import android.app.Application
import com.atech.expensesync.koin.KoinInitializer

class ExpenseSync : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
        instance = this
    }

    companion object {
        var instance: ExpenseSync? = null
            private set
    }
}