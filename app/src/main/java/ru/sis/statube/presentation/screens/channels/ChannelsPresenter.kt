package ru.sis.statube.presentation.screens.channels

import android.content.Context
import android.graphics.Bitmap
import ru.sis.statube.additional.BANNER_IMAGE_FILE_NAME
import ru.sis.statube.additional.launch
import ru.sis.statube.interactor.ChannelInteractor
import ru.sis.statube.interactor.ImageInteractor
import ru.sis.statube.model.Channel
import ru.sis.statube.model.Channels
import ru.sis.statube.presentation.Presenter
import java.io.File
import java.io.FileOutputStream

class ChannelsPresenter : Presenter() {

    fun loadFavouriteChannels(onLoad: (channels: List<Channel>) -> Unit) = launch({
        val channels = ChannelInteractor.getInstance().getFavouriteChannelsAsync().await()
        onLoad(channels)
    }, {
        onLoad(emptyList())
    })

    fun searchChannels(context: Context, text: String, pageToken: String?,
                       onLoad: (text: String, channels: Channels) -> Unit) = launch({
        val channels = ChannelInteractor.getInstance().searchChannelsAsync(context, text, pageToken).await()
        onLoad(text, channels)
    }, {
        onLoad(text, Channels())
    })

    fun loadChannel(context: Context, channelId: String, onLoad: (channel: Channel) -> Unit) = launch({
        showProgressDialog(context)
        val channel = ChannelInteractor.getInstance().getChannelAsync(context, channelId).await()?.let { channel ->
            val bannerImageFile = File(context.filesDir, BANNER_IMAGE_FILE_NAME)
            if (bannerImageFile.exists())
                bannerImageFile.delete()

            channel.bannerImageUrl?.let { bannerImageUrl ->
                ImageInteractor.getInstance().getImageAsync(context, bannerImageUrl).await()
            }?.let { bitmap ->
                FileOutputStream(bannerImageFile).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos)
                    fos.flush()
                }
            }

            channel
        }
        hideProgressDialog()
        channel?.let { onLoad(it) }
    }, {
        hideProgressDialog()
    })

    fun changeFavourite(channel: Channel) = launch {
        ChannelInteractor.getInstance().changeFavouriteChannelAsync(channel).await()
    }

}
