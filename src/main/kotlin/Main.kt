package com.vandenbreemen

import com.vandenbreemen.viddisplayrast.data.GameDataRequirements

fun main() {
    println("Hello World!")

    val requirement =
        GameDataRequirements(100, 200, 8, 8, 1024)
    println(requirement)
}