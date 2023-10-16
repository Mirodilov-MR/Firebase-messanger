package com.example.firebase_messanger.loaders

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.firebase_messanger.R
import lv.chi.photopicker.loader.ImageLoader


class GlideImageLoader : ImageLoader {
    override fun loadImage(context: Context, view: ImageView, uri: Uri) {
        Glide.with(context).asBitmap().load(uri).placeholder(R.drawable.icplaceholder).centerCrop().into(view)
    }
}