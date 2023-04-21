package com.mizikarocco.data;

import kotlinx.serialization.Serializable

@Serializable
data class ClientRequest(
        val id: String,
        val title:String,
        val author:String
)