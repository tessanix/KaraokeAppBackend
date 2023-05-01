package com.mizikarocco.sockets

import com.mizikarocco.data.requests.WebSocketResponse
import com.mizikarocco.data.session.HttpUserSession
import com.mizikarocco.data.session.WebSocketUserSession
import com.mizikarocco.utils.JsonOperationsOnRequests
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.*


fun generateNewId(connections: Map<String, WebSocketUserSession>): String{
    var newId = UUID.randomUUID().toString()
    while(connections.keys.contains(newId)){
        newId = UUID.randomUUID().toString()
    }
    return newId
}

fun Route.requestsSocket(
    jsonOperationsOnRequests : JsonOperationsOnRequests
){
    //val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
    val connections = Collections.synchronizedMap<String, WebSocketUserSession>(HashMap())

    webSocket("/websocketrequestspage") {
        // websocketSession
        //println("Adding user!")
        val thisConnection = WebSocketUserSession(this)
        val userSession = call.sessions.get<HttpUserSession>()
        val userId = if (userSession != null) "0000000000" else generateNewId(connections)
        connections[userId] = thisConnection
        try {
            for (frame in incoming) {

                if (frame is Frame.Text) {

                    val messageJson = Json.parseToJsonElement(frame.readText())
                    val action = messageJson.jsonObject["action"]?.jsonPrimitive?.content
                    val data = messageJson.jsonObject["data"]?.jsonObject!!
                    var response: WebSocketResponse? = null
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
                            response = buildResponse( uuid, action, mutableMapOf())
                        }
                    }
                    println("USER ID: $userId")
                    println("RESPONSE: $response")
                    if (userId == "0000000000") connections[userId]?.session?.send( Frame.Text(Json.encodeToString(response)) )
                    else{
                        connections.values.forEach {
                            it.session.send( Frame.Text(Json.encodeToString(response)) )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
        } finally {
            println("Removing $thisConnection!")
            connections.remove(userId)
        }
    }

}


fun buildResponse( uuid:String?, action:String, data:MutableMap<String,String>) : WebSocketResponse {
    return if(uuid!=null){
        data["id"] = uuid
        WebSocketResponse(action = action, status = "Success", data = data)
    } else WebSocketResponse(action = action, status = "Failure", data = emptyMap())
}