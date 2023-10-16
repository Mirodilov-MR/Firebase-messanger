package com.example.firebase_messanger.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import lv.chi.photopicker.PhotoPickerFragment
import com.example.firebase_messanger.adapters.user.UserMessagesReadAdapter
import com.example.firebase_messanger.databinding.ActivityUserMessagesBinding
import com.example.firebase_messanger.loaders.GlideImageLoader
import com.example.firebase_messanger.dialogs.LoadPickImageDialog
import com.example.firebase_messanger.model.User
import com.example.firebase_messanger.model.UserMessageModel
import com.example.firebase_messanger.utils.IsNetworkConnection.Companion.status
import java.io.File
import java.io.IOException
import java.util.*
import com.example.firebase_messanger.R
import com.squareup.picasso.Picasso
import lv.chi.photopicker.ChiliPhotoPicker
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class UserMessagesActivity : AppCompatActivity(), PhotoPickerFragment.Callback {

    private lateinit var userMessagesBinding: ActivityUserMessagesBinding
    private lateinit var storage: FirebaseStorage
    private val currentUser = Firebase.auth.currentUser
    private lateinit var reference: DatabaseReference
    private var adapter = UserMessagesReadAdapter()
    private var messageList: ArrayList<UserMessageModel>? = null
    private var seenListener: ValueEventListener? = null
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userMessagesBinding = ActivityUserMessagesBinding.inflate(layoutInflater)
        setContentView(userMessagesBinding.root)
        userMessagesBinding.allMessage.adapter = adapter
        registerForContextMenu(userMessagesBinding.allMessage)
        ChiliPhotoPicker.init(GlideImageLoader())
        storage = FirebaseStorage.getInstance()
        reference = FirebaseDatabase.getInstance().getReference("user_messages")
        reference.keepSynced(true)
        user = intent.getSerializableExtra("user") as User
        userMessagesBinding.profileName.text = user?.userFullName
        val pathReference =
            storage.getReferenceFromUrl("gs://smartchat-3c7ce.appspot.com")
                .child(user?.userImgURL.toString())
        if (user?.userImgURL?.length == 17) {
            userMessagesBinding.userNameLatter.text =
                user?.userFullName?.uppercase(Locale.ROOT)
        } else {
            userMessagesBinding.userNameLatter.text = ""
        }
        try {
            val file: File = File.createTempFile("profile_image", "jpg")
            pathReference.getFile(file).addOnSuccessListener {
                val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                userMessagesBinding.profileImage.setImageBitmap(bitmap)
            }.addOnFailureListener {
                Log.d("TAG", "onDataChange: ${it.message}")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        userMessagesBinding.closeActivity.setOnClickListener {
            finish()
        }
        changeMessageField()
        userMessagesBinding.sendMessage.setOnClickListener {
            sendMessages(user!!, userMessagesBinding.enterMessage.text.toString())
            userMessagesBinding.enterMessage.setText("")
            scrollToLastMessage()
            changeMessageField()
            setLayoutManager()
        }

        setLayoutManager()
        getOnlineStatus()
        user = intent.getSerializableExtra("user") as User
        loadUserImage(user!!)
        messageList = ArrayList()
        readAllMessages(currentUser!!.uid, user?.id.toString())
        userMessagesBinding.allMessage.adapter = adapter
        userMessagesBinding.sendFiles.setOnClickListener {
            openBottomSheet()
        }
        seenMessage(user?.id.toString())
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        val dialog = LoadPickImageDialog(this)
        dialog.loadImage(photos[0], user!!)
    }

    private fun readAllMessages(senderUserId: String, receiverUser: String) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList?.clear()
                for (dataSnapshot in snapshot.children) {
                    val message = dataSnapshot.getValue(UserMessageModel::class.java)
                    if (message!!.receiver.equals(senderUserId) && message.sender.equals(
                            receiverUser
                        )
                        || message.receiver.equals(receiverUser) && message.sender.equals(
                            senderUserId
                        )
                    ) {
                        messageList!!.add(message)
                    }
                }
                adapter.data(messageList!!)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun loadUserImage(user: User) {
        val profileImageReference =
            FirebaseStorage.getInstance().getReferenceFromUrl(user.userImgURL ?: "")

        if (user.userImgURL?.length == 17) {
            userMessagesBinding.userNameLatter.text = user.userFullName?.uppercase(Locale.ROOT)
        } else {
            userMessagesBinding.userNameLatter.text = ""
        }

        profileImageReference.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get()
                .load(uri)
                .placeholder(R.drawable.loading) // Placeholder image while loading
                .error(R.drawable.account) // Error image if loading fails
                .into(userMessagesBinding.profileImage)
        }.addOnFailureListener { exception ->

            Log.e("LoadImage", "Failed to get download URL: $exception")
            // Set a default image if no has image
            userMessagesBinding.profileImage.setImageResource(R.drawable.account)
        }
    }

    private fun scrollToLastMessage() {
        val layoutManager = userMessagesBinding.allMessage.layoutManager as LinearLayoutManager
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        val itemCount = adapter.itemCount

        if (lastVisiblePosition == itemCount - 2) {
            userMessagesBinding.allMessage.smoothScrollToPosition(itemCount - 1)
        }
    }

    private fun setLayoutManager() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        userMessagesBinding.allMessage.layoutManager = layoutManager
    }

    private fun changeMessageField() {
        userMessagesBinding.enterMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (userMessagesBinding.enterMessage.text.toString().isNotEmpty()) {
                    userMessagesBinding.sendFiles.visibility = GONE
                    userMessagesBinding.sendSound.visibility = GONE
                    userMessagesBinding.sendMessage.visibility = VISIBLE
                } else {
                    userMessagesBinding.sendFiles.visibility = VISIBLE
                    userMessagesBinding.sendSound.visibility = VISIBLE
                    userMessagesBinding.sendMessage.visibility = GONE
                }
            }

        })
    }

    private fun sendMessages(user: User, message: String) {
        val messageSend: HashMap<String, Any> = HashMap()
        val date: Calendar = Calendar.getInstance()
        currentUser?.uid?.let {
            messageSend.put("sender", it)
        }
        user.id?.let {
            messageSend.put("receiver", it)
        }
        messageSend["messageImageLink"] = "0"
        messageSend["message"] = message
        messageSend["messageDate"] = date.time.hours.toString() + ":" + date.time.minutes.toString()
        messageSend["messageRead"] = false
        reference.push().setValue(messageSend)
    }

    private fun seenMessage(userId: String) {
        reference = FirebaseDatabase.getInstance().getReference("user_messages")
        seenListener = reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val message = dataSnapshot.getValue(UserMessageModel::class.java)
                    if (message!!.receiver.equals(currentUser?.uid) && message.sender.equals(userId)) {
                        val hashMap: HashMap<String, Boolean> = HashMap()
                        hashMap["messageRead"] = true
                        dataSnapshot.ref.updateChildren(hashMap as Map<String, Any>)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun openBottomSheet() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            return
        }
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = true,
            maxSelection = 1,
            theme = lv.chi.photopicker.R.style.ChiliPhotoPicker_Light
        ).show(supportFragmentManager, "picker")
    }

    private fun getOnlineStatus() {
        val connectedRef =
            FirebaseDatabase.getInstance().getReference("Users").child(user?.id.toString())
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = snapshot.getValue<User>()
                Log.d("TTGG", "onDataChange: ${status?.status}")
                if (status!!.status == "online") {
                    userMessagesBinding.onlineStatus.text = "online"
                } else if (status.status == "offline") {
                    userMessagesBinding.onlineStatus.text = "last seen recently"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        if (v?.id == R.id.all_message) {
            menuInflater.inflate(R.menu.item_menu, menu)
        }
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onResume() {
        super.onResume()
        status("online")
    }

    override fun onPause() {
        super.onPause()
        reference.removeEventListener(seenListener!!)
        status("offline")
    }
}