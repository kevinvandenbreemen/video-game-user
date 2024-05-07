package com.vandenbreemen.com.vandenbreemen.videogameusr.log

/*
------------------~~~~~~~~~~~~~~~~----------------------------
Simple logging library that will eventually have pluggability
into UIs for displaying it on the screen so the developer/user
can see what's going on
------------------~~~~~~~~~~~~~~~~----------------------------
 */


enum class KlogLevel {
    INFO,
    WARN,
    ERROR

}

fun klog(level: KlogLevel, message: String) {
    println("KLOG: $level: $message")
}

fun klog(message: String) {
    klog(KlogLevel.INFO, message)
}