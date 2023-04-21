package com.mizikarocco.routes

import io.ktor.server.http.content.*
import io.ktor.server.routing.*

//const val RESOURCES_PATH_FROM_ROUTE = "src/main/resources/static/"
fun Route.staticRoutes() {
//    val resourceCss = {}.javaClass.getResource("/static/css/songs_style.css")
//    val resourceJs = {}.javaClass.getResource("/static/js/songs_functions.js")

    static("/") {
        staticBasePackage = "static"
        //resources(".")
        resources("css")
        resources("js")
    }

}