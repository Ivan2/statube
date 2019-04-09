package ru.sis.statube.interactor

import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.sis.statube.additional.loadAsBitmap

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

    fun getImageAsync(context: Context, url: String) = GlobalScope.async {
        context.loadAsBitmap(url)
    }

}