package ru.sis.statube.interactor

import android.content.Context
import ru.sis.statube.additional.async
import ru.sis.statube.additional.loadAsBitmap

class ImageInteractor : Interactor() {

    companion object {
        private var INSTANCE: ImageInteractor? = null
        fun getInstance() = INSTANCE ?: ImageInteractor().apply { INSTANCE = this }
    }

    fun getImageAsync(context: Context, url: String) = async {
        context.loadAsBitmap(url)
    }

}