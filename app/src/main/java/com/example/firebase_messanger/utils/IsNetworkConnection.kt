package com.example.firebase_messanger.utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class IsNetworkConnection {
    companion object {
        private lateinit var referance: DatabaseReference
        fun status(status: String) {
            referance = FirebaseDatabase.getInstance().getReference("Users")
                .child(Firebase.auth.currentUser!!.uid)
            val hashMap: HashMap<String, String> = HashMap()
            hashMap["status"] = status
            referance.updateChildren(hashMap as Map<String, Any>)
        }
    }
}