package com.example.firebase_messanger.adapters.user

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebase_messanger.R
import com.example.firebase_messanger.databinding.UserTextMessageLeftBinding
import com.example.firebase_messanger.databinding.UserTextMessageRightBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.firebase_messanger.model.UserMessageModel
import java.io.File
import java.io.IOException


class UserMessagesReadAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var userMessagesList = emptyList<UserMessageModel>()
    private var fUser: FirebaseUser? = null
    private lateinit var storage: FirebaseStorage


    fun data(message: List<UserMessageModel>) {
        this.userMessagesList = message
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        fUser = Firebase.auth.currentUser
        return if (userMessagesList[position].sender.equals(fUser?.uid)) {
            TYPE_SENDER
        } else {
            TYPE_RECEIVER
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SENDER) {
            ViewHolderRight(
                UserTextMessageRightBinding.bind(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_text_message_right, parent, false)
                )
            )
        } else {
            ViewHolderLeft(
                UserTextMessageLeftBinding.bind(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_text_message_left, parent, false)
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        return if (viewType == TYPE_SENDER) {
            (holder as ViewHolderRight).bindData(userMessagesList[position])
        } else {
            (holder as ViewHolderLeft).bindData(userMessagesList[position])
        }
    }

    override fun getItemCount(): Int {
        userMessagesList.let {
            return it.size
        }
    }

    inner class ViewHolderLeft(var binding1: UserTextMessageLeftBinding) :
        RecyclerView.ViewHolder(binding1.root) {
        fun bindData(userMessageModel: UserMessageModel) {
            binding1.userText.text = userMessageModel.message
            binding1.messageDate.text = userMessageModel.messageDate
            if (!userMessageModel.messageImageLink.equals("0")) {
                binding1.constraint.minWidth = WindowManager.LayoutParams.MATCH_PARENT
                loadImage(userMessageModel.messageImageLink.toString(), binding1.messageLink)
                binding1.messageLink.visibility = View.VISIBLE
            } else {
                binding1.messageLink.visibility = View.GONE
            }
        }
    }

    private fun loadImage(fileName: String, view: ImageView) {
        if (view.context is Activity) {
            val activity = view.context as Activity
            if (activity.isFinishing) {
                return
            }
        }
        storage = FirebaseStorage.getInstance()
        val pathReference =
            storage.getReferenceFromUrl("gs://smartchat-3c7ce.appspot.com").child(fileName)

        try {
            val file: File = File.createTempFile("users_images", "jpg")
            pathReference.getFile(file).addOnSuccessListener {
                val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                if (view.context is Activity) {
                    val activity = view.context as Activity
                    if (!activity.isFinishing) {
                        Glide.with(view.context).load(bitmap).centerCrop().into(view)
                    }
                }
            }.addOnFailureListener {
                Log.d("ERR", "onDataChange: ${it.message}")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    inner class ViewHolderRight(var binding2: UserTextMessageRightBinding) :
        RecyclerView.ViewHolder(binding2.root) {
        fun bindData(userMessageModel: UserMessageModel) {
            binding2.userText.text = userMessageModel.message
            binding2.messageDate.text = userMessageModel.messageDate
            Log.d("TTC", "bindData: ${userMessageModel.messageRead}")
            if (userMessageModel.messageRead == true) {
                binding2.messageSendIcon.setBackgroundResource(R.drawable.ic_seen_icon)
            } else {
                binding2.messageSendIcon.setBackgroundResource(R.drawable.ic_send_icon)
            }
            if (!userMessageModel.messageImageLink.equals("0")) {
                binding2.constraint.minWidth = WindowManager.LayoutParams.MATCH_PARENT
                loadImage(userMessageModel.messageImageLink.toString(), binding2.messageImageLink)
                binding2.messageImageLink.visibility = View.VISIBLE
            } else {
                binding2.messageImageLink.visibility = View.GONE
            }
        }

        private fun loadImage(fileName: String, view: ImageView) {
            if (view.context is Activity) {
                val activity = view.context as Activity
                if (activity.isFinishing) {
                    return
                }
            }

            storage = FirebaseStorage.getInstance()
            val pathReference =
                storage.getReferenceFromUrl("gs://smartchat-3c7ce.appspot.com").child(fileName)
            try {
                val file: File = File.createTempFile("users_images", "jpg")
                pathReference.getFile(file).addOnSuccessListener {
                    val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    if (view.context is Activity) {
                        val activity = view.context as Activity
                        if (!activity.isFinishing) {
                            Glide.with(view.context).load(bitmap).centerCrop().into(view)
                        }
                    }
                }.addOnFailureListener {
                    Log.d("ERR", "onDataChange: ${it.message}")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private companion object {
        val TYPE_RECEIVER = 0
        val TYPE_SENDER = 1
    }
}