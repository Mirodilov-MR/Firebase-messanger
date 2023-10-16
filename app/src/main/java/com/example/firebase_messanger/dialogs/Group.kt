package com.example.firebase_messanger.dialogs

data class Group(
    val groupId: String = "",
    val groupName: String = "",
    val members: List<String> = emptyList()
)