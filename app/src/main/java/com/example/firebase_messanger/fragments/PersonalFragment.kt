package com.example.firebase_messanger.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.example.firebase_messanger.adapters.user.UsersAdapter
import com.example.firebase_messanger.databinding.FragmentPersonalBinding
import com.example.firebase_messanger.dialogs.Group
import com.example.firebase_messanger.dialogs.LoadProgressDialog
import com.example.firebase_messanger.model.User

class PersonalFragment : Fragment() {

    private var _binding: FragmentPersonalBinding? = null
    private val binding get() = _binding
    private var adapter = UsersAdapter()
    private var userList: ArrayList<User>? = null
    private lateinit var loadingDialog: LoadProgressDialog
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonalBinding.inflate(inflater, container, false)
        loadingDialog = LoadProgressDialog(inflater.context)
        loadingDialog.loadDialog()
        auth = Firebase.auth
        binding?.recycler?.layoutManager = LinearLayoutManager(requireContext())
        userList = ArrayList()
        loadUsers()
        binding?.recycler?.adapter = adapter
        return binding?.root
    }

    private fun loadUsers() {
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                loadingDialog.dismissDialog()
                userList?.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null && user.id != auth.currentUser?.uid && user.status == "online") {
                        Log.d("TTA", "onDataChange: ${user.userFullName}")
                        userList!!.add(user)
                    }
                }
                adapter.data(userList!!)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}