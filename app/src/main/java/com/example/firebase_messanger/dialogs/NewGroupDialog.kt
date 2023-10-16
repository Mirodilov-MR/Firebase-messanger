package com.example.firebase_messanger.dialogs

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.example.firebase_messanger.databinding.GroupDialogBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class NewGroupDialog(context: Context) : AlertDialog(context) {
    private lateinit var binding: GroupDialogBinding
    private var firebaseUser: FirebaseUser = Firebase.auth.currentUser!!
    private var storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GroupDialogBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.btnSave.setOnClickListener {
            val groupName = binding.groupName.text.toString().trim()
            if (groupName.isNotEmpty()) {
                createGroup(groupName)
            } else {
                Toast.makeText(context, "Please enter a group name", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun createGroup(groupName: String) {
        val groupRef = FirebaseDatabase.getInstance().getReference("Groups")
        val newGroup = Group(groupRef.push().key!!, groupName, mutableListOf(firebaseUser.uid))
        groupRef.child(newGroup.groupId).setValue(newGroup).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Group created successfully", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(context, "Failed to create group", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
//i need create group chat, when user click createGroupBar programm open NewGroupDialog screen and user write group nama to edit text(id: group_name), and click btnSave, after clicked btnSave programm should create group and add all registered users to group, group should shown on MainActivity