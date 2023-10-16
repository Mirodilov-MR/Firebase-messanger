package com.example.firebase_messanger.adapters.user

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_messanger.R
import com.example.firebase_messanger.activities.UserMessagesActivity
import com.example.firebase_messanger.databinding.UserItemBinding
import com.example.firebase_messanger.dialogs.DeleteDialog
import com.example.firebase_messanger.model.User
import com.squareup.picasso.Picasso
import java.io.IOException


class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private var userList = emptyList<User>()
    fun data(user: List<User>) {
        this.userList = user
        notifyDataSetChanged()
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetwork
        if (networkInfo != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(networkInfo)
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        }
        return false
    }

    inner class ViewHolder(var userBinding: UserItemBinding) :
        RecyclerView.ViewHolder(userBinding.root) {

        fun bindData(user: User) {
            userBinding.profileName.text = user.userFullName

            if (user.status?.contains("online") == true && isNetworkConnected(userBinding.root.context)) {
                userBinding.statusOnline.setBackgroundResource(R.drawable.widget_online)
            } else {
                userBinding.statusOnline.setBackgroundResource(R.drawable.widget_offline)
            }
            val profileImageUrl = user.userImgURL
            if (!profileImageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(profileImageUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.account)
                    .into(userBinding.profileImage)
            } else {
                userBinding.profileImage.setImageResource(R.drawable.account)
            }

            userBinding.constraint.setOnClickListener {
                val intent =
                    Intent(userBinding.constraint.context, UserMessagesActivity::class.java)
                intent.putExtra("user", user)
                userBinding.constraint.context.startActivity(intent)
            }

            userBinding.constraint.setOnLongClickListener {
                val context = userBinding.constraint.context
                val deleteDialog = DeleteDialog(context)
                deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                deleteDialog.show()

                Toast.makeText(context, "Long press on ${user.userFullName}", Toast.LENGTH_SHORT)
                    .show()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UserItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.user_item, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @get:Throws(InterruptedException::class, IOException::class)
    private val isNetworkConnected: Boolean
        get() {
            val command = "ping -c 1 google.com"
            return Runtime.getRuntime().exec(command).waitFor() == 0
        }
}