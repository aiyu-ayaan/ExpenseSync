package com.atech.expensesync.ui_utils

import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import java.awt.Frame
import java.awt.Point
import java.awt.Toolkit
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JWindow
import javax.swing.SwingUtilities
import javax.swing.Timer

actual fun showToast(message: String, duration: Duration) {
    SwingUtilities.invokeLater {
        val mainFrame = findActiveMainFrame()
        val toastDurationMs = when (duration) {
            Duration.SHORT -> 2000
            Duration.LONG -> 3500
        }

        val window = JWindow(mainFrame).apply {
            contentPane = createToastPanel(message)
            pack()
            location = calculateToastPosition(mainFrame, size)
            isVisible = true
        }

        setupAutoClose(window, toastDurationMs)
    }
}

private fun createToastPanel(message: String): JPanel {
    return JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        background = Color(50, 50, 50)
        border = BorderFactory.createEmptyBorder(16, 24, 16, 24)
        add(JLabel(message).apply {
            foreground = Color.WHITE
            alignmentX = Component.CENTER_ALIGNMENT
            font = font.deriveFont(Font.BOLD, 14f)
        })
        isOpaque = true
    }
}

private fun calculateToastPosition(parent: Frame?, toastSize: Dimension): Point {
    return if (parent != null && parent.isVisible) {
        // Position at the bottom of the application window
        Point(
            parent.x + (parent.width - toastSize.width) / 2, // Center horizontally
            parent.y + parent.height - toastSize.height - 20 // 20px from the bottom
        )
    } else {
        // Fallback to screen bottom if no parent window is found
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        Point(
            (screenSize.width - toastSize.width) / 2,
            screenSize.height - toastSize.height - 50
        )
    }
}

private fun setupAutoClose(window: JWindow, delayMs: Int) {
    Timer(delayMs) {
        window.dispose()
    }.apply {
        isRepeats = false
        start()
    }
}

private fun findActiveMainFrame(): Frame? {
    return Frame.getFrames().firstOrNull { it.isVisible }
}