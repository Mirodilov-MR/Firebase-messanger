package com.example.firebase_messanger.model

import java.io.Serializable


class User: Serializable {
    var id: String? = null
    var userFullName: String? = null
    var userName: String? = null
    var userImgURL: String? = null
    var status: String? = null
    var userPassword: String? = null

    constructor()

    constructor(
        id: String?,
        userFullName: String?,
        userName: String?,
        userImgURL: String?,
        status: String?,
        userPassword: String?
    ) {
        this.id = id
        this.userFullName = userFullName
        this.userName = userName
        this.userImgURL = userImgURL
        this.status = status
        this.userPassword = userPassword
    }
}
