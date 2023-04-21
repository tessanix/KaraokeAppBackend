package com.mizikarocco.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketResponse(
    val action :String,
    val status: String,
    val data: Map<String, String>
)