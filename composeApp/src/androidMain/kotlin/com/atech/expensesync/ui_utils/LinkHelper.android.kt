package com.atech.expensesync.ui_utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import com.atech.expensesync.utils.LoggerType
import com.atech.expensesync.utils.expenseSyncLogger

actual class LinkHelper(
    private val context: Context
) {
    actual fun openLink(url: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, url.toUri())

            if (context !is Activity) {
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(browserIntent)
        } catch (e: Exception) {
            expenseSyncLogger("Error opening link: ${e.message}", LoggerType.ERROR)
            Toast.makeText(context, e.message ?: "Error opening link", Toast.LENGTH_SHORT).show()
        }
    }
}