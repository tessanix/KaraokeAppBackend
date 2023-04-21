package com.mizikarocco.utils

import com.google.gson.Gson
import com.mizikarocco.data.Song
import java.io.File
import java.util.*
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf


class JsonOperationsOnSongs(gson: Gson): JsonOperations(gson) {

    private val songMapByCategoryType = typeOf<Map<String, List<Song>>>().javaType
    private val inputFile = File(DATABASE_PATH_FROM_UTILS + "songs.json")
    private val outputFile = File(DATABASE_PATH_FROM_UTILS + "songs_updated.json")

    override fun addToDatabase(category: String, title: String, author: String): String {

        val songMapByCategory: Map<String, MutableList<Song>> =
            if (inputFile.exists()) {
                inputFile.bufferedReader().use { reader ->
                    gson.fromJson(reader, songMapByCategoryType)
                }
            } else emptyMap()

        var uuid = UUID.randomUUID()

        if(songMapByCategory.isNotEmpty()) {
            while (!verifyIntegrity(songMapByCategory, uuid.toString())) {
                uuid = UUID.randomUUID()
            }
            songMapByCategory[category]?.add(Song(uuid.toString(), title, author))
        }

        rewriteJson(songMapByCategory, inputFile, outputFile)

        return uuid.toString()    }

    override fun deleteFromDatabase(id: String ) {

        val songMapByCategory: Map<String, MutableList<Song>> =
            if (inputFile.exists()) {
                inputFile.bufferedReader().use { reader ->
                    gson.fromJson(reader, songMapByCategoryType)
                }
            } else emptyMap()

        if(songMapByCategory.isNotEmpty()){
            for((category, songList) in songMapByCategory) {
                val songToRemove = songList.find { it.id == id }
                if (songToRemove != null) {
                    songMapByCategory[category]?.remove(songToRemove)
                    break
                }
            }
        }
        rewriteJson(songMapByCategory, inputFile, outputFile)
    }


    override fun getAllFromDatabase(): Map<String, List<Song>> {
        val songMapByCategory: Map<String, List<Song>> =
            if (inputFile.exists()) {
                inputFile.bufferedReader().use { reader ->
                    gson.fromJson(reader, songMapByCategoryType)
                }
            } else emptyMap()

        return songMapByCategory
    }

    private fun verifyIntegrity(data: Map<String, MutableList<Song>>, newId:String) : Boolean{
        data.values.forEach { listSongs ->
            listSongs.forEach { song ->
                if(song.id == newId) return false
            }
        }
        return true
    }
}
