package com.example.firebase_messanger.app

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import lv.chi.photopicker.ChiliPhotoPicker
import com.example.firebase_messanger.loaders.GlideImageLoader


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ChiliPhotoPicker.init(GlideImageLoader())
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        ChiliPhotoPicker.init(
            GlideImageLoader(), "com.example.firebase_messanger"
        )
    }

}