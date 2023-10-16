package com.example.firebase_messanger.dialogs

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.example.firebase_messanger.databinding.DeleteDialogBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class DeleteDialog(context: Context) : AlertDialog(context) {
    private lateinit var binding: DeleteDialogBinding
    private var firebaseUser: FirebaseUser = Firebase.auth.currentUser!!
    private var storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DeleteDialogBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.btnSave.setOnClickListener {
            Toast.makeText(context, "Bosildi", Toast.LENGTH_SHORT).show()
        }
    }
}
