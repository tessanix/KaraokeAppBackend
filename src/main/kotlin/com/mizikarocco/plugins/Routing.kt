package com.mizikarocco.plugins

import com.mizikarocco.routes.*
import com.mizikarocco.utils.JsonOperationsOnRequests
import com.mizikarocco.utils.JsonOperationsOnSongs
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    jsonOperationsOnRequests : JsonOperationsOnRequests,
    jsonOperationsOnSongs : JsonOperationsOnSongs
) {
    routing {

        authRoutes()

        loginPage()

        songsPage()

        clientRequestsPage(jsonOperationsOnRequests)

        addNewSong(jsonOperationsOnSongs)

        deleteSong(jsonOperationsOnSongs)

        getAllSongs(jsonOperationsOnSongs)

        staticRoutes()
    }
}

