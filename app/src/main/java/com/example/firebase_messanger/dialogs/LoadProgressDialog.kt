package com.example.firebase_messanger.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.firebase_messanger.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder



class LoadProgressDialog(var context: Context) {

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var dialog: Dialog

    fun loadDialog(){
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
        materialAlertDialogBuilder.setView(R.layout.progress_dialog)
        materialAlertDialogBuilder.setCancelable(false)
        materialAlertDialogBuilder.background = ColorDrawable(Color.TRANSPARENT)
        dialog = materialAlertDialogBuilder.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }

}