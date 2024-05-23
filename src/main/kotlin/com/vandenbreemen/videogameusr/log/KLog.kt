package com.vandenbreemen.videogameusr.log

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/*
------------------~~~~~~~~~~~~~~~~----------------------------
Simple logging library that will eventually have pluggability
into UIs for displaying it on the screen so the developer/user
can see what's going on
------------------~~~~~~~~~~~~~~~~----------------------------
 */


enum class KlogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR
    ;

    fun isAtLeast(level: KlogLevel): Boolean {
        return this.ordinal >= level.ordinal
    }
}


private val dispatcher = Dispatchers.Default
private const val LOG_DELAY = 100L
private val dateFormat = SimpleDateFormat("HH:mm:ss.SSS")
private data class Entry (
    val timestamp: Long,
    val level: KlogLevel,
    val message: String,
    val throwable: Throwable? = null

) {
    fun toLogString(): String {

        val stackTrace = throwable?.stackTrace?.joinToString("\n") ?: ""

        val formatted = dateFormat.format(timestamp)
        return "$formatted: $level: $message${if(stackTrace.isNotBlank()) "\n$stackTrace" else ""}"
    }
}

data class KLogConfig(val level: KlogLevel, val logImmediate: Boolean = false)

private val loqQueue = Vector<Entry>()
val logJob = CoroutineScope(dispatcher).launch {

    //  Hours, minutes, seconds, milliseconds
    while (true) {
        delay(LOG_DELAY)
        if (loqQueue.isNotEmpty()) {
            loqQueue.removeAll { entry ->
                println(entry.toLogString())
                true
            }
        }
    }
}

/**
 * Current logging level
 */
private var config = KLogConfig(KlogLevel.INFO)

fun klogConfig(updated: KLogConfig) {
    config = updated
}

private fun logOutOfBand(timestamp: Long, level: KlogLevel, message: String, throwable: Throwable? = null) {
    if(config.logImmediate){
        println(Entry(timestamp, level, message, throwable).toLogString())
        return
    }
    loqQueue.add(Entry(timestamp, level, message, throwable))
}

fun klog(level: KlogLevel, message: String) {
    if(!level.isAtLeast(config.level)){
        return
    }
    logOutOfBand(System.currentTimeMillis(), level, message)
}

fun klog(level: KlogLevel, messageLmb: () -> String) {
    if(!level.isAtLeast(config.level)){
        return
    }
    logOutOfBand(System.currentTimeMillis(), level, messageLmb())
}

fun klog(level: KlogLevel, message: String, throwable: Throwable) {
    if(!level.isAtLeast(config.level)){
        return
    }
    logOutOfBand(System.currentTimeMillis(), level, message, throwable)
}

fun klog(message: String) {
    klog(KlogLevel.INFO, message)
}

fun klog(message: String, throwable: Throwable) {
    klog(KlogLevel.INFO, message, throwable)
}