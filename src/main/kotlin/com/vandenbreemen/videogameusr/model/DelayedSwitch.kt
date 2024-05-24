package com.vandenbreemen.videogameusr.model

import java.util.concurrent.atomic.AtomicLong

/**
 * Switch that can be set to true for a certain delay
 */
class DelayedSwitch(private val delay: Long = 1000L) {

    private val lastSwitchTime = AtomicLong(0)
    fun trigger(): Boolean {
        val currentTime = System.currentTimeMillis()
        if(currentTime - lastSwitchTime.get() > delay) {
            lastSwitchTime.set(currentTime)
            return true
        }
        return false
    }



}