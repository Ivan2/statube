package ru.sis.statube.presentation.presenter

import android.content.Context
import android.graphics.Bitmap
import ru.sis.statube.additional.VIDEO_IMAGE_FILE_NAME
import ru.sis.statube.additional.launch
import ru.sis.statube.interactor.ImageInteractor
import ru.sis.statube.interactor.VideosInteractor
import ru.sis.statube.model.Video
import java.io.File
import java.io.FileOutputStream

open class FullVideoLoadingPresenter : Presenter() {

    fun loadFullVideo(context: Context, video: Video, onLoad: (fullVideo: Video) -> Unit) = launch({
        showProgressDialog(context)
        val fullVideo = VideosInteractor.getInstance().getFullVideoAsync(context, video.id).await()

        val videoImageFile = File(context.filesDir, VIDEO_IMAGE_FILE_NAME)
        if (videoImageFile.exists())
            videoImageFile.delete()

        fullVideo?.thumbnail?.let { thumbnail ->
            ImageInteractor.getInstance().getImageAsync(context, thumbnail).await()
        }?.let { bitmap ->
            FileOutputStream(videoImageFile).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos)
                fos.flush()
            }
        }

        hideProgressDialog()
        onLoad(fullVideo ?: video)
    }, { e ->
        hideProgressDialog()
        onLoad(video)
        onError(context, e)
    })

}
