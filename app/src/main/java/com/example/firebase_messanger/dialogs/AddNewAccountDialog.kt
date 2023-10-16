package com.example.firebase_messanger.dialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.firebase_messanger.databinding.AddNewAccountDialogBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.firebase_messanger.model.User
import java.io.File
import java.io.IOException
import java.util.*

class AddNewAccountDialog(context: Context) : AlertDialog(context) {

    private var newAccountDialogBinding: AddNewAccountDialogBinding =
        AddNewAccountDialogBinding.inflate(layoutInflater)

    private var firebaseUser: FirebaseUser = Firebase.auth.currentUser!!

    init {
        val reference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        val storage = FirebaseStorage.getInstance()

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    val pathReference =
                        storage.getReferenceFromUrl("gs://smartchat-3c7ce.appspot.com")
                            .child(user.userImgURL.toString())
                    try {
                        val file: File = File.createTempFile("profile_image", "jpg")
                        pathReference.getFile(file).addOnSuccessListener { taskSnapshot ->
                            val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            Log.d("PATH", "onDataChange: ${file.absolutePath}")

                        }.addOnFailureListener {
                            Log.d("TAG", "onDataChange: ${it.message}")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    newAccountDialogBinding.profileName.text = user.userFullName.toString()
                    newAccountDialogBinding.profileEmail.text = firebaseUser.email.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ${error.message}")
            }
        })

        setView(newAccountDialogBinding.root)
    }
}