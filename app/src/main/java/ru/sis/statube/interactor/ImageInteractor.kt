package ru.sis.statube.interactor

import android.content.Context
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ImageInteractor : Interactor() {

    companion object {
        private var INSTANCE: ImageInteractor? = null
        fun getInstance(): ImageInteractor {
            val instance = INSTANCE ?: ImageInteractor()
            if (INSTANCE == null)
                INSTANCE = instance
            return instance
        }
    }

    fun loadImageAsync(context: Context, url: String) = GlobalScope.async {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .submit()
            .get()
    }

}