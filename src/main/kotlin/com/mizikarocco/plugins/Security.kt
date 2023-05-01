package com.mizikarocco.plugins


import com.google.gson.Gson
import com.mizikarocco.data.session.HttpUserSession
import com.mizikarocco.utils.getAdminCredentials
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

fun Application.configureSecurity(gson: Gson) {

    install(Sessions) {
        cookie<HttpUserSession>("user_session", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60 * 24 // == 1 day cookie
        }
    }

    install(Authentication) {

        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            val listOfCredentials = getAdminCredentials(gson)
            validate { credentials ->
                if (listOfCredentials.contains(
                        mapOf("username" to credentials.name, "password" to credentials.password)
                )){
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized, "Credentials are not valid")
            }
        }

        session<HttpUserSession>("auth-session") {
            validate { session ->
                if(session.name.startsWith("fre")) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }
    }
}

