package com.mizikarocco

import com.google.gson.GsonBuilder
import com.mizikarocco.plugins.*
import com.mizikarocco.utils.JsonOperationsOnRequests
import com.mizikarocco.utils.JsonOperationsOnSongs
import io.ktor.server.application.*


fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val gson = GsonBuilder().setPrettyPrinting().create()

    val jsonOperationsOnRequests = JsonOperationsOnRequests(gson)
    val jsonOperationsOnSongs = JsonOperationsOnSongs(gson)

    configureTemplating()
    configureSerialization()
    configureMonitoring()
    configureSecurity( gson )
    configureSockets( jsonOperationsOnRequests )
    configureRouting( jsonOperationsOnRequests, jsonOperationsOnSongs )
}
