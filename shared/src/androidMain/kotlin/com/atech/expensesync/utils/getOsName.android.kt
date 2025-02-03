package com.atech.expensesync.utils

actual fun getOsName(): String = "Android ${android.os.Build.VERSION.SDK_INT}"