package com.atech.expensesync.utils

import java.io.BufferedReader
import java.io.File


actual fun getMachineUUID(): String {
    return when {
        isWindows() -> getWindowsUUID()
        isMac() -> getMacUUID()
        isLinux() -> getLinuxUUID()
        else -> "UNKNOWN"
    }
}

private fun getWindowsUUID(): String {
    return try {
        val process = ProcessBuilder(
            "cmd.exe",
            "/c",
            "reg query HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Cryptography /v MachineGuid"
        ).start()
        process.inputStream.bufferedReader().use(BufferedReader::readText)
            .trim()
            .split("\\s+".toRegex())
            .last()
    } catch (e: Exception) {
        "UNKNOWN"
    }
}

private fun getMacUUID(): String {
    return try {
        val process = ProcessBuilder(
            "sh",
            "-c",
            "ioreg -rd1 -c IOPlatformExpertDevice | awk -F'\"' '/IOPlatformUUID/{print $(NF-1)}'"
        ).start()
        process.inputStream.bufferedReader().use(BufferedReader::readText).trim()
    } catch (e: Exception) {
        "UNKNOWN"
    }
}

private fun getLinuxUUID(): String {
    return try {
        val file = File("/etc/machine-id")
        if (file.exists()) {
            file.readText().trim()
        } else {
            val process = ProcessBuilder("sh", "-c", "dbus-uuidgen --get").start()
            process.inputStream.bufferedReader().use(BufferedReader::readText).trim()
        }
    } catch (e: Exception) {
        "UNKNOWN"
    }
}

private fun isWindows() = System.getProperty("os.name").contains("Windows", ignoreCase = true)
private fun isMac() = System.getProperty("os.name").contains("Mac", ignoreCase = true)
private fun isLinux() = System.getProperty("os.name").contains("Linux", ignoreCase = true)