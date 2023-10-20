package com.example.firebase_messanger.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.example.firebase_messanger.R
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.firebase_messanger.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.firebase_messanger.dialogs.AddNewAccountDialog
import com.example.firebase_messanger.dialogs.NewGroupDialog
import com.example.firebase_messanger.fragments.UsersLIstFragment
import com.example.firebase_messanger.fragments.PersonalFragment
import com.example.firebase_messanger.fragments.call_list.CallListFragment
import com.example.firebase_messanger.model.User
import com.example.firebase_messanger.utils.IsNetworkConnection.Companion.status
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemReselectedListener {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var referance: DatabaseReference
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        firebaseUser = Firebase.auth.currentUser!!
        storage = FirebaseStorage.getInstance()
        referance = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        referance.keepSynced(true)
        loadUserImage()
        loadNewAccountDialog()
        currentFragment(PersonalFragment())
        mainBinding.viewCategoryText.text = "SmartChat"

        mainBinding.bottomNav.selectedItemId = R.id.show_personal
        mainBinding.searchBar.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        mainBinding.createGroupBar.setOnClickListener {
            val newGroupDialog = NewGroupDialog(this)
            newGroupDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            newGroupDialog.show()
        }
        mainBinding.exitAccount.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        mainBinding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.show_all -> {
                    currentFragment(CallListFragment())
                    mainBinding.viewCategoryText.text = getString(R.string.all)
                    true
                }

                R.id.show_personal -> {
                    currentFragment(PersonalFragment())
                    mainBinding.viewCategoryText.text = getString(R.string.online_user)
                    true
                }

                R.id.show_group -> {
                    currentFragment(UsersLIstFragment())
                    mainBinding.viewCategoryText.text = getString(R.string.users)
                    true
                }

                else -> false
            }
        }

        mainBinding.bottomNav.setOnNavigationItemReselectedListener(this)
    }

    private fun loadUserImage() {
        referance.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                val userImgURL = user?.userImgURL
                if (userImgURL != null && userImgURL.isNotEmpty()) {
                    val pathReference = storage.getReferenceFromUrl(userImgURL)

                    try {
                        val file: File = File.createTempFile("images", "jpg")
                        pathReference.getFile(file).addOnSuccessListener { taskSnapshot ->
                            val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            mainBinding.profileImage.setImageBitmap(bitmap)
                        }.addOnFailureListener {
                            Log.d("TAG", "onDataChange: ${it.message}")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                   mainBinding.profileImage.setImageResource(R.drawable.account)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun loadNewAccountDialog() {
        mainBinding.profileImage.setOnClickListener {
            val newAccountDialog = AddNewAccountDialog(this)
            newAccountDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            newAccountDialog.show()
        }
    }

    private fun currentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
    }

    override fun onNavigationItemReselected(item: MenuItem) {
    }

    override fun onResume() {
        super.onResume()
        status("online")
    }

    override fun onPause() {
        super.onPause()
        status("offline")
    }
}