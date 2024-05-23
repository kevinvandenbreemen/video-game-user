package com.vandenbreemen.videogameusr.model

import com.vandenbreemen.com.vandenbreemen.videogameusr.model.ColorInteractor
import com.vandenbreemen.viddisplayrast.data.ByteColorDataInteractor
import com.vandenbreemen.videogameusr.tools.model.CodeGenerationModel
import com.vandenbreemen.videogameusr.view.render.CanvasRasterRender
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Objects commonly used so you don't have to keep creating them
 */
object CoreDependenciesHelper {

    val byteDataInteractor = ByteColorDataInteractor()
    fun getColorInteractor(): ColorInteractor {
        return ColorInteractor(byteDataInteractor)
    }

    fun getCodeGenerationModel(): CodeGenerationModel {
        return CodeGenerationModel()
    }

    fun getIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    fun getCanvasRasterRender(): CanvasRasterRender {
        return CanvasRasterRender(getColorInteractor())
    }

}