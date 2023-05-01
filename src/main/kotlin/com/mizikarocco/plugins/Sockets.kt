package com.mizikarocco.plugins

import com.mizikarocco.sockets.requestsSocket
import com.mizikarocco.sockets.songsSockets
import com.mizikarocco.utils.JsonOperationsOnRequests
import com.mizikarocco.utils.JsonOperationsOnSongs
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.time.Duration


fun Application.configureSockets(jsonOperationsOnRequests : JsonOperationsOnRequests) {

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing{
        requestsSocket(jsonOperationsOnRequests)
    }
}


