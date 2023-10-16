package com.example.firebase_messanger.fragments.call_list

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_messanger.R
import java.text.SimpleDateFormat
import java.util.*

class CallLogAdapter(private val context: Context, private val callLogList: List<CallLogItem>) :
    RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.call_log_item, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val callLogItem = callLogList[position]

        holder.contactName.text = callLogItem.name ?: "No saved number"
        holder.phoneNumber.text = callLogItem.number
        holder.calledTime.text = formatDate(callLogItem.callTime)
        holder.contactName.setOnClickListener {
            val phoneNumber = callLogItem.number
            dialPhoneNumber(phoneNumber)
        }
    }

    override fun getItemCount(): Int {
        return callLogList.size
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    inner class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactName: TextView = itemView.findViewById(R.id.contact_name)
        val phoneNumber: TextView = itemView.findViewById(R.id.phone_number)
        val calledTime: TextView = itemView.findViewById(R.id.called_time)
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
    }
}


