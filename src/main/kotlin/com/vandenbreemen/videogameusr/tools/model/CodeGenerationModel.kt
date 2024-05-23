package com.vandenbreemen.videogameusr.tools.model

import com.vandenbreemen.videogameusr.log.klog
import java.io.File

class CodeGenerationModel {

    companion object {
        const val CODE_GEN_DIR = "generated"
    }

    suspend fun setupCodeGeneratingDirectory() {
        //  Create the directory if it doesn't exist
        val dir = File(CODE_GEN_DIR)
        if(!dir.exists()){
            dir.mkdirs()
            klog("Created directory $CODE_GEN_DIR")
        }
    }

}