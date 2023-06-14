package com.mizikarocco.routes

import io.ktor.server.http.content.*
import io.ktor.server.routing.*


fun Route.staticRoutes() {
    staticResources("/", "static/css")
    staticResources("/", "static/js")
}