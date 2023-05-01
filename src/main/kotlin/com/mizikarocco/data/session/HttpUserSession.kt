package com.mizikarocco.data.session

import io.ktor.server.auth.*

data class HttpUserSession(
    val name: String,
    val count: Int
) : Principal
