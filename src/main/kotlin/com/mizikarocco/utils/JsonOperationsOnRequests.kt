package com.mizikarocco.utils

import com.google.gson.Gson
import com.mizikarocco.data.ClientRequest
import java.io.*
import java.util.*
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf


class JsonOperationsOnRequests(gson: Gson) : JsonOperations(gson) {

    private val clientRequestListType = typeOf<MutableList<Map<String, String>>>().javaType
    private val inputFile = File(DATABASE_PATH_FROM_UTILS + "requests.json")
    private val outputFile = File(DATABASE_PATH_FROM_UTILS + "requests_updated.json")

    override fun addToDatabase(category: String, title: String, author: String): String? {

        val clientRequestList: MutableList<Map<String, String>> =
            if (inputFile.exists()) {
                BufferedReader(FileReader(inputFile)).use { reader ->
                    gson.fromJson(reader, clientRequestListType)
                }
            } else mutableListOf()

        var uuid = UUID.randomUUID()

        if(clientRequestList.isNotEmpty()) {
            while (!verifyIntegrity(clientRequestList, uuid.toString())) {
                uuid = UUID.randomUUID()
            }
        }
        clientRequestList.add(
            mapOf(
                "id" to uuid.toString(),
                "clientName" to category,
                "title" to title,
                "author" to author
            )
        )

        val isModifiedSuccessfully = rewriteJson(clientRequestList, inputFile, outputFile)

        return if(isModifiedSuccessfully) uuid.toString() else null
    }


    override fun deleteFromDatabase(id: String) {

        val clientRequestList: MutableList<Map<String, String>> =
            if (inputFile.exists()) {
                inputFile.bufferedReader().use { reader ->
                    gson.fromJson(reader, clientRequestListType)
                }
            } else mutableListOf()


        if(clientRequestList.isNotEmpty()){
            println("removing something...")
            println("request : $id")

            println("BEFORE : ")

            println(clientRequestList)

            val requestToRemove = clientRequestList.find { request -> request["id"] == id }
            requestToRemove?.let { clientRequestList.remove(it)  }
            println("AFTER : ")

            println(clientRequestList)
        }
        rewriteJson(clientRequestList, inputFile, outputFile)
    }

    override fun getAllFromDatabase(): Map<String, List<ClientRequest>> {
        val clientRequestsList: List<Map<String, String>> = inputFile.reader().use { reader ->
            gson.fromJson(reader, clientRequestListType)
        }

        val clientRequestsMap: MutableMap<String, MutableList<ClientRequest>> = mutableMapOf()

        for (request in clientRequestsList) {
            val clientName = request["clientName"]!!
            val clientRequest = ClientRequest(
                request["id"]!!,
                //request["clientName"]!!,
                request["title"]!!,
                request["author"]!!
            )

            if(clientRequestsMap.keys.contains(clientName))
                clientRequestsMap[clientName]?.add(clientRequest)
            else
                clientRequestsMap[clientName] = mutableListOf(clientRequest)
        }
        return clientRequestsMap
    }

    private fun verifyIntegrity(data: List<Map<String, String>>, newId:String) : Boolean{
        data.forEach { request ->
            if(request["id"] == newId) return false
        }
        return true
    }

}