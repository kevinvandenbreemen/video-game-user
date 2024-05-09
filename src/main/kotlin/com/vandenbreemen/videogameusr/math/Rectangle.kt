package com.vandenbreemen.com.vandenbreemen.videogameusr.math

data class Rectangle(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
) {
    fun intersects(other: Rectangle): Boolean {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y
    }

    fun touches(other: Rectangle): Boolean {
        return x <= other.x + other.width &&
                x + width >= other.x &&
                y <= other.y + other.height &&
                y + height >= other.y
    }

}
