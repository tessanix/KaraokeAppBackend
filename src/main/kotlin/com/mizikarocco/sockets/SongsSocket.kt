package com.mizikarocco.sockets

import com.mizikarocco.data.Connection
import com.mizikarocco.data.requests.WebSocketResponse
import com.mizikarocco.utils.JsonOperationsOnSongs
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.*
import kotlin.collections.LinkedHashSet

fun Route.songsSockets(
    jsonOperationsOnSongs: JsonOperationsOnSongs
){
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

        webSocket("/websocketsongspage") {
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

                        when (action) {
                            "addSong" -> {
                                val category = data["category"]?.jsonPrimitive?.content!!
                                val title = data["title"]?.jsonPrimitive?.content!!
                                val author = data["author"]?.jsonPrimitive?.content!!
                                val uuid = jsonOperationsOnSongs.addToDatabase(category, title, author)
                                connections.forEach { it.session.send(
                                    Frame.Text(
                                        Json.encodeToString(
                                            WebSocketResponse(
                                                action = "addSong",
                                                status = "Success",
                                                data = mapOf(
                                                    "id" to uuid,
                                                    "title" to title,
                                                    "author" to author,
                                                    "category" to category
                                                )
                                            )
                                        )
                                    ))
                                }
                            }

                            "deleteSong" -> {
                                val category = data["category"]?.jsonPrimitive?.content!!
                                val uuid = data["id"]?.jsonPrimitive?.content!!
                                jsonOperationsOnSongs.deleteFromDatabase(id = uuid)

                                connections.forEach { it.session.send(
                                    Frame.Text(
                                        Json.encodeToString(
                                            WebSocketResponse(
                                                action = "deleteSong",
                                                status = "Success",
                                                data = mapOf(
                                                    "id" to uuid,
                                                    "category" to category
                                                )
                                            )
                                        )
                                    ))
                                }
                            }
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