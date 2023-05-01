package com.mizikarocco.data;

import kotlinx.serialization.Serializable

//@Serializable

// ClientRequest data class server side contain id instead of clientName
@Serializable
data class ClientRequest(
        val id: String,
        //val clientName: String,
        val title:String,
        val author:String
)