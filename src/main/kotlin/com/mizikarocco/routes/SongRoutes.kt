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

        call.respond(HttpStatusCode.OK)
        jsonOperationsOnSongs.addToDatabase(cat!!, title!!, author!!)

        println(title)
        println(author)
        println(cat)
    }
}

fun Route.deleteSong(
    jsonOperationsOnSongs : JsonOperationsOnSongs
) {
    delete("/deleteSong") {
        val songId = call.request.queryParameters["songId"]!!
        val category = call.request.queryParameters["category"]!!
        // handle the delete request here, using the songId and param2 parameters
        println("song id :$songId")
        println("category :$category")

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



