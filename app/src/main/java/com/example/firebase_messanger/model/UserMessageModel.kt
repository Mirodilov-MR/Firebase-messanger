package com.example.firebase_messanger.model

import java.io.Serializable



class UserMessageModel: Serializable {

    var sender: String? = null
    var receiver: String? = null
    var messageImageLink: String? = null
    var message: String? = null
    var messageDate: String? = null
    var messageRead: Boolean? = null

    constructor()

    constructor(
        sender: String,
        receiver: String,
        messageImageLink: String,
        message: String,
        messageDate: String,
        messageRead: Boolean
    ){
        this.sender = sender
        this.receiver = receiver
        this.messageImageLink = messageImageLink
        this.message = message
        this.messageDate = messageDate
        this.messageRead = messageRead
    }
}