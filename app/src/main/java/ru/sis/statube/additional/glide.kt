package ru.sis.statube.additional

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.sis.statube.R
import java.io.File

fun ImageView.loadThumbnail(url: String?) {
    try {
        Glide.with(this.context)
            .load(url)
            .placeholder(R.drawable.default_thumbnail)
            .circleCrop()
            .dontAnimate()
            .into(this)
    } catch (_: Exception) {}
}

fun ImageView.loadBanner(file: File) {
    try {
        Glide.with(this.context)
            .load(file)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
            .dontAnimate()
            .into(this)
    } catch (_: Exception) {}
}

fun Context.loadAsBitmap(url: String?): Bitmap? {
    return try {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .submit()
            .get()
    } catch (_: Exception) {
        null
    }
}
