package com.mizikarocco.routes


import com.mizikarocco.utils.JsonOperationsOnSongs
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.getAllSongs(
    jsonOperationsOnSongs : JsonOperationsOnSongs
){
    get("/musics") {
        call.respond(
            HttpStatusCode.OK,
            jsonOperationsOnSongs.getAllFromDatabase()
        )
    }
}

fun Route.addNewSong(
    jsonOperationsOnSongs : JsonOperationsOnSongs
) {

    post("/addNewSong") {
        val formData = call.receiveParameters()
        val title = formData["title"]
        val author = formData["author"]
        val cat = formData["category"]

        val id = jsonOperationsOnSongs.addToDatabase(cat!!, title!!, author!!)
        call.respond(HttpStatusCode.OK, mapOf("id" to id))
    }
}

fun Route.deleteSong(
    jsonOperationsOnSongs : JsonOperationsOnSongs
) {
    delete("/deleteSong") {
        val songId = call.request.queryParameters["songId"]!!

        jsonOperationsOnSongs.deleteFromDatabase(songId)
        call.respondText("Song deleted successfully")
    }
}



//fun Route.sendClientRequest() {
//
//    post("/clientRequest") {
//        val newRequest = call.receive<ClientRequest>()
//        call.respond(HttpStatusCode.OK)
//        addClientRequestToDatabase(newRequest)
//    }
//}



