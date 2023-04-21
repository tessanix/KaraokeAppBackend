package com.mizikarocco.utils

import com.google.gson.Gson
import java.io.File

const val DATABASE_PATH_FROM_UTILS = "src/main/resources/database/" // "/root/KaraokeAppBackend/database/"

abstract class JsonOperations(val gson: Gson) {

    abstract fun addToDatabase(category:String, title:String, author:String): String?

    abstract fun deleteFromDatabase(id: String)

    abstract fun getAllFromDatabase(): Any

    fun rewriteJson(elementToWrite: Any, inputFile: File, outputFile: File) : Boolean {
        // Écriture de la liste mise à jour dans un nouveau fichier JSON
        outputFile.bufferedWriter().use { writer ->
            gson.toJson(elementToWrite, writer)
        }
        // Remplacement de l'ancien fichier par le nouveau fichier
        val isDeleted = inputFile.delete()
        val isRenamed = outputFile.renameTo(inputFile)

        return isDeleted && isRenamed
    }
}
