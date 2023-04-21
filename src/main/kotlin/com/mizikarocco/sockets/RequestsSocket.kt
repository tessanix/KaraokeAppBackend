package com.mizikarocco.sockets

import com.mizikarocco.data.Connection
import com.mizikarocco.data.requests.WebSocketResponse
import com.mizikarocco.utils.JsonOperationsOnRequests
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.*
import kotlin.collections.LinkedHashSet


fun Route.requestsSocket(
    jsonOperationsOnRequests : JsonOperationsOnRequests
){
    val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

    webSocket("/websocketrequestspage") {
        // websocketSession
        //println("Adding user!")
        val thisConnection = Connection(this)
        connections += thisConnection
        try {
            for (frame in incoming) {

                if (frame is Frame.Text) {
                    val messageJson = Json.parseToJsonElement(frame.readText())
                    val action = messageJson.jsonObject["action"]?.jsonPrimitive?.content
                    val data = messageJson.jsonObject["data"]?.jsonObject!!
                    var response: WebSocketResponse? = null
                    println("DATA: $data")
                    when (action) {
                        "addRequest" -> {
                            val clientName = data["clientName"]?.jsonPrimitive?.content!!
                            val title = data["title"]?.jsonPrimitive?.content!!
                            val author = data["author"]?.jsonPrimitive?.content!!
                            val uuid = jsonOperationsOnRequests.addToDatabase(clientName, title, author)
                            response = buildResponse(uuid, action, mutableMapOf("title" to title, "author" to author, "clientName" to clientName))
                        }
                        "deleteRequest" -> {
                            val uuid = data["id"]?.jsonPrimitive?.content!!
                            jsonOperationsOnRequests.deleteFromDatabase(id = uuid)
                            response = buildResponse(uuid, action, mutableMapOf())
                        }
                    }
                    connections.forEach {
                        it.session.send(
                        Frame.Text(Json.encodeToString(response)))
                    }
                }
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
        } finally {
            println("Removing $thisConnection!")
            connections -= thisConnection
        }
    }

}


fun buildResponse(uuid:String?, action:String, data:MutableMap<String,String>) : WebSocketResponse {
    return if(uuid!=null){
        data["id"] = uuid
        WebSocketResponse(action = action, status = "Success", data = data)
    } else WebSocketResponse(action = action, status = "Failure", data = emptyMap())
}