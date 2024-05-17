package com.vandenbreemen.videogameusr.model.game.assetmgt

import com.vandenbreemen.com.vandenbreemen.videogameusr.log.KlogLevel
import com.vandenbreemen.com.vandenbreemen.videogameusr.log.klog
import com.vandenbreemen.viddisplayrast.data.GameDataRequirements
import com.vandenbreemen.videogameusr.model.game.TileBasedGameWorld
import java.util.*

private enum class AssetReadAction {
                                   NONE,
    SPRITE,
    TILE
}

private data class AssetReadContext(val requirements: GameDataRequirements, val assetReadAction: AssetReadAction, val spriteIndex: Int = -1,
                                    val spriteBytes: ByteArray? = null) {
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
            Scanner(it).use { scanner ->

                var context = AssetReadContext(requirements, AssetReadAction.NONE)

                while(scanner.hasNextLine()){
                    context = processLine(scanner.nextLine(), context)
                }
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

                    return AssetReadContext(context.requirements, AssetReadAction.SPRITE, spriteIndex, byteArrayOf())
                } else {
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
                    return AssetReadContext(context.requirements, AssetReadAction.NONE)
                }

                val bytes = line.split(",").mapNotNull {
                    klog(KlogLevel.DEBUG) { "Byte: $it" }
                    if(it.isEmpty()) {
                        return@mapNotNull null
                    }
                    it.trim().toByte() }.toByteArray()
                currentByteArray.addAll(bytes.toList())

                AssetReadContext(context.requirements, AssetReadAction.SPRITE, context.spriteIndex, currentByteArray.toByteArray())
            }
            else -> {
                klog(KlogLevel.WARN) { "Unexpected action ${context.assetReadAction} in context $context"}
                context
            }
        }
    }

}