package com.mizikarocco.routes

import com.mizikarocco.utils.JsonOperationsOnRequests
import com.mizikarocco.utils.JsonOperationsOnSongs
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.songsPage(
    jsonOperationsOnSongs : JsonOperationsOnSongs
) {
    authenticate("auth-session") {
        get("songs") {
            call.respond(
                FreeMarkerContent(
                    "songs_page.ftl",
                    mapOf("songsMapByCategories" to jsonOperationsOnSongs.getAllFromDatabase())
                )
            )
        }
    }
}

fun Route.clientRequestsPage(
    jsonOperationsOnRequests : JsonOperationsOnRequests
) {
    authenticate("auth-session") {
        get("requests") {
            call.respond(
                FreeMarkerContent(
                    "client_requests_page.ftl",
                    mapOf("clientRequestsMap" to jsonOperationsOnRequests.getAllFromDatabase())
                )
            )
        }
    }
}

fun Route.loginPage() {

    get("/") {
        call.respondRedirect("/login")
    }

    get("login"){
        call.respond(FreeMarkerContent("login_page.ftl", null))
    }

}