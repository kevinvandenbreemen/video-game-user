package com.vandenbreemen.videogameusr.model

import com.vandenbreemen.com.vandenbreemen.videogameusr.model.ColorInteractor
import com.vandenbreemen.viddisplayrast.data.ByteColorDataInteractor

/**
 * Objects commonly used so you don't have to keep creating them
 */
object CoreDependenciesHelper {

    val byteDataInteractor = ByteColorDataInteractor()
    fun getColorInteractor(): ColorInteractor {
        return ColorInteractor(byteDataInteractor)
    }

}