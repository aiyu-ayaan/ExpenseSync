package com.atech.expensesync.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

actual fun restartApp() {
    runBlocking {
        val success = JvmProcessManager().restartJvmProcess()
        // Only exit current process if restart succeeded
        if (success) {
            exitProcess(0)
        }
    }
}

class JvmProcessManager(
    private val appMainClass: String = "com.atech.expensesync.MainKt",
    private val jvmArgs: List<String> = listOf("-Xmx512m"),
    private val workingDirectory: File = File(".")
) {
    private var process: Process? = null

    /**
     * Starts the JVM process if it's not already running
     */
    suspend fun startJvmProcess(): Boolean = withContext(Dispatchers.IO) {
        if (isProcessRunning()) {
            println("JVM process is already running")
            return@withContext false
        }

        try {
            val javaBin =
                System.getProperty("java.home") + File.separator + "bin" + File.separator + "java"
            val classpath = System.getProperty("java.class.path")

            val command = mutableListOf(javaBin)
            command.addAll(jvmArgs)
            command.add("-cp")
            command.add(classpath)
            command.add(appMainClass)

            val processBuilder = ProcessBuilder(command)
            processBuilder.directory(workingDirectory)
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)

            process = processBuilder.start()
            println("JVM process started successfully")
            true
        } catch (e: Exception) {
            println("Failed to start JVM process: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    /**
     * Stops the currently running JVM process
     */
    suspend fun stopJvmProcess(): Boolean = withContext(Dispatchers.IO) {
        process?.let {
            try {
                if (isProcessRunning()) {
                    it.destroy()
                    if (!it.waitFor(500, TimeUnit.MILLISECONDS)) {
                        it.destroyForcibly()
                    }
                    println("JVM process stopped successfully")
                    process = null
                    return@withContext true
                }
            } catch (e: Exception) {
                println("Error stopping JVM process: ${e.message}")
                e.printStackTrace()
            }
        }

        println("No running JVM process to stop")
        false
    }

    /**
     * Restarts the JVM process
     */
    suspend fun restartJvmProcess(): Boolean {
        stopJvmProcess()
        withContext(Dispatchers.IO) { Thread.sleep(500) }
        return startJvmProcess()
    }

    /**
     * Checks if the process is currently running
     */
    private fun isProcessRunning(): Boolean {
        return try {
            process?.isAlive ?: false
        } catch (e: Exception) {
            false
        }
    }
}
