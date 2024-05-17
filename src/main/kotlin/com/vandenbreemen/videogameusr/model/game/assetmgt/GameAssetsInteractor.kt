package com.vandenbreemen.videogameusr.model.game.assetmgt

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import java.io.File
import java.io.PrintWriter
import java.util.*

private enum class AssetReadAction {
                                   NONE,
    SPRITE,
    LEVEL
}

private data class AssetReadContext(val requirements: GameDataRequirements, val assetReadAction: AssetReadAction, val spriteIndex: Int = -1,
                                    val spriteBytes: ByteArray? = null, val levelTiles: IntArray? = null, val levelName: String? = null, val levelWidth: Int = -1, val levelHeight: Int = -1, val world: TileBasedGameWorld? = null) {
    override fun toString(): String {
        return "CTX(action=$assetReadAction)"
    }
}

/**
 * Loads assets from resources in the classpath
 */
class GameAssetsInteractor {

    /**
     *
     */
    fun loadAssetsFromClasspath(path: String, requirements: GameDataRequirements, world: TileBasedGameWorld) {
        this::class.java.getResourceAsStream(path)?.let {
            klog(KlogLevel.DEBUG, "Loading assets from $path")
            it.use { stream ->
                Scanner(stream).use { scanner ->

                    var context = AssetReadContext(requirements, AssetReadAction.NONE, world = world)

                    while(scanner.hasNextLine()){
                        context = processLine(scanner.nextLine(), context)
                    }
                }
            }
        }

    }

    fun loadAssetsFromFile(path: String, requirements: GameDataRequirements, world: TileBasedGameWorld) {
        klog(KlogLevel.DEBUG, "Loading assets from $path")
        File(path).useLines { lines ->
            var context = AssetReadContext(requirements, AssetReadAction.NONE, world = world)
            lines.forEach { line ->
                context = processLine(line, context)
            }
        }
    }

    private fun processLine(line: String, context: AssetReadContext): AssetReadContext {
        klog(KlogLevel.DEBUG) { "($context): $line"}

        return when(context.assetReadAction) {
            AssetReadAction.NONE -> {
                if(line.startsWith("@s:")) {
                    //  Get the sprite index
                    val spriteIndex = line.substring(3).toInt()

                    return AssetReadContext(context.requirements, AssetReadAction.SPRITE, spriteIndex, byteArrayOf(), world = context.world)
                } else if(line.startsWith("@l:")) {
                    //  Get the level name
                    val parts = line.split(":")
                    klog(KlogLevel.DEBUG) { "Level token:  Parts: $parts" }
                    val levelName = parts[1]
                    val width = parts[2].toInt()
                    val height = parts[3].toInt()

                    return AssetReadContext(context.requirements, AssetReadAction.LEVEL, levelName = levelName, levelWidth = width, levelHeight = height, world = context.world, levelTiles = intArrayOf())
                }
                else {
                    klog(KlogLevel.WARN) { "Unexpected line $line" }
                    context
                }
            }
            AssetReadAction.SPRITE -> {

                val currentByteArray = context.spriteBytes!!.toMutableList()

                if(currentByteArray.size == context.requirements.spriteWidth * context.requirements.spriteHeight){
                    //  Done with this sprite
                    klog(KlogLevel.DEBUG) { "Loaded sprite ${context.spriteIndex}"}
                    context.requirements.setData(context.spriteIndex, currentByteArray.toByteArray())
                    return AssetReadContext(context.requirements, AssetReadAction.NONE, world = context.world)
                }

                val bytes = line.split(",").mapNotNull {
                    klog(KlogLevel.DEBUG) { "Byte: $it" }
                    if(it.isBlank()) {
                        return@mapNotNull null
                    }
                    it.trim().toByte() }.toByteArray()
                currentByteArray.addAll(bytes.toList())

                AssetReadContext(context.requirements, AssetReadAction.SPRITE, context.spriteIndex, currentByteArray.toByteArray(), world = context.world)
            }
            AssetReadAction.LEVEL -> {
                val world = context.world!!
                val currentLevelTiles = context.levelTiles!!.toMutableList()

                //  Is the level complete?
                klog(KlogLevel.DEBUG) { "Current level tiles: ${currentLevelTiles.size} vs ${context.levelWidth * context.levelHeight}"}
                if(currentLevelTiles.size == context.levelWidth * context.levelHeight){
                    klog(KlogLevel.DEBUG) { "Loaded level ${context.levelName} successfully"}
                    val level = world.addLevel(context.levelName!!, context.levelWidth, context.levelHeight)

                    //  For x and y
                    for(y in 0 until context.levelHeight) {
                        for (x in 0 until context.levelWidth) {
                            val tile = currentLevelTiles[y * context.levelWidth + x]
                            level.setSpriteTileAt(x, y, tile)
                        }
                    }

                    return AssetReadContext(context.requirements, AssetReadAction.NONE, world = context.world)
                }

                val tiles = line.split(",").mapNotNull {
                    if(it.isBlank()) {
                        return@mapNotNull null
                    }
                    it.trim().toInt() }.toIntArray()
                currentLevelTiles.addAll(tiles.toList())

                AssetReadContext(context.requirements, AssetReadAction.LEVEL, levelTiles = currentLevelTiles.toIntArray(), levelName = context.levelName!!, world = context.world, levelWidth = context.levelWidth, levelHeight = context.levelHeight)

            }
            else -> {
                klog(KlogLevel.WARN) { "Unexpected action ${context.assetReadAction} in context $context"}
                context
            }
        }
    }

    fun writeAssetsToFile(pathToWriteTo: String, gameDataRequirements: GameDataRequirements, world: TileBasedGameWorld) {
        PrintWriter(File(pathToWriteTo)).use { printWriter->

            //  All the sprites
            for (index in 0 until gameDataRequirements.maxBytes / (gameDataRequirements.spriteWidth * gameDataRequirements.spriteHeight)) {
                val spriteData = gameDataRequirements.getSpriteData(index)
                printWriter.println("@s:$index")
                spriteData.forEachIndexed { index, byte ->
                    if(index % gameDataRequirements.spriteWidth == 0){
                        printWriter.println()
                    }
                    printWriter.print("$byte, ")
                }
                printWriter.println("\n")
            }

            //  All the levels
            world.getLevelNames().forEach { levelName ->
                val level = world.getLevel(levelName)
                printWriter.println("@l:$levelName:${level.widthInTiles}:${level.heightInTiles}")

                for(y in 0 until level.heightInTiles) {
                    for (x in 0 until level.widthInTiles) {
                        val tile = level.getSpriteTileAt(x, y)
                        printWriter.print("$tile, ")
                    }
                    printWriter.println("")
                }
                printWriter.println("\n")
            }

            printWriter.println("@e")
        }

    }

}