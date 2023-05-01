package com.mizikarocco.utils

import com.google.gson.Gson
import java.io.File
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf

const val DATABASE_PATH_FROM_UTILS = "src/main/resources/database/" //   "/root/KaraokeAppBackend/database/"

fun getAdminCredentials(gson: Gson) : List<Map<String, String>> {
    val credentialsFile = File(DATABASE_PATH_FROM_UTILS + "admin_credentials.json")
    val credentialsListType = typeOf<MutableList<Map<String, String>>>().javaType
    return credentialsFile.reader().use { reader -> gson.fromJson(reader, credentialsListType) }
}

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
