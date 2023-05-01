package com.mizikarocco.routes

import com.mizikarocco.data.session.HttpUserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.authRoutes() {

    authenticate("auth-form") {
        post("send_login") {
            val userName = call.principal<UserIdPrincipal>()?.name.toString()
            call.sessions.set(HttpUserSession(name = userName, count = 1))
            call.respond(HttpStatusCode.OK)
        }
    }

    get("/logout") {
        call.sessions.clear<HttpUserSession>()
        call.respondRedirect("/login")
    }

    get("/session") {
        val userSession = call.sessions.get<HttpUserSession>()
        if (userSession != null) {
            call.respondText(
                "Session found!",
                status = HttpStatusCode.OK)
        } else {
            call.respondText(
                "Session doesn't exist or is expired.",
                status = HttpStatusCode.NotFound)
        }
    }
}
//    authenticate("auth-session") {
//        get("hello") {
//            val userSession = call.principal<UserSession>()
//            call.sessions.set(userSession?.copy(count = userSession.count + 1))
//            call.respondText("Hello, ${userSession?.name}! Visit count is ${userSession?.count}.")
//        }
//    }
