package com.example.firebase_messanger.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase_messanger.adapters.user.UsersAdapter
import com.example.firebase_messanger.databinding.ActivitySearchBinding
import com.example.firebase_messanger.model.User
import com.example.firebase_messanger.room.MyApplication
import com.example.firebase_messanger.room.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: UsersAdapter
    private var userList: ArrayList<User>? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        reference = FirebaseDatabase.getInstance().getReference("Users")
        setupRecyclerView()
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchUsers(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRecyclerView() {
        binding.userList.layoutManager = LinearLayoutManager(this)
        adapter = UsersAdapter()
        userList = ArrayList()
        binding.userList.adapter = adapter
    }

    private fun searchUsers(query: String) {
        val filteredUsers = ArrayList<User>()
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                filteredUsers.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null && user.id != auth.currentUser?.uid) {
                        val fullName = user.userFullName?.lowercase(Locale.ROOT)
                        if (fullName != null && fullName.contains(query.lowercase(Locale.ROOT))) {
                            filteredUsers.add(user)
                        }
                    }
                }
                adapter.data(filteredUsers)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

//    private fun searchUsers(query: String) {
//        val filteredUsers = ArrayList<UserEntity>()
//        val filteredUsersData = ArrayList<User>()
//
//        reference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                filteredUsers.clear()
//                for (snapshot in dataSnapshot.children) {
//                    val user = snapshot.getValue(User::class.java)
//                    if (user != null && user.id != auth.currentUser?.uid) {
//                        val fullName = user.userFullName?.lowercase(Locale.ROOT)
//                        if (fullName != null && fullName.contains(query.lowercase(Locale.ROOT))) {
//                            filteredUsers.add(UserEntity(user.id!!, user.userFullName!!))
//                            filteredUsersData.add(user)  // Add user to the list
//                        }
//                    }
//                }
//                adapter.data(filteredUsersData)
//                GlobalScope.launch(Dispatchers.IO) {
//                    MyApplication.database.userDao().insertUsers(filteredUsers)
//
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {}
//        })
//    }
}