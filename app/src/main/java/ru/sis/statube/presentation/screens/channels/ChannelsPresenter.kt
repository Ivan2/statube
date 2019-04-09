package ru.sis.statube.presentation.screens.channels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import ru.sis.statube.additional.BANNER_IMAGE_FILE_NAME
import ru.sis.statube.additional.CHANNEL_DATA_KEY
import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.ChannelInteractor
import ru.sis.statube.interactor.ImageInteractor
import ru.sis.statube.model.Channel
import ru.sis.statube.model.Channels
import ru.sis.statube.presentation.Presenter
import ru.sis.statube.presentation.screens.channel.ChannelActivity
import java.io.File
import java.io.FileOutputStream

class ChannelsPresenter : Presenter() {

    fun loadFavouriteChannels(onLoad: (channels: List<Channel>) -> Unit) = resolvedLaunch({
        val channels = ChannelInteractor.getInstance().getFavouriteChannelsAsync().await()
        onLoad(channels)
    }, {
        onLoad(emptyList())
    })

    fun searchChannels(context: Context, text: String, pageToken: String?,
                       onLoad: (text: String, channels: Channels) -> Unit) = resolvedLaunch({
        val channels = ChannelInteractor.getInstance().searchChannelsAsync(context, text, pageToken).await()
        onLoad(text, channels)
    }, {
        onLoad(text, Channels())
    })

    fun loadChannel(context: Context, channelId: String, onLoad: (channel: Channel?) -> Unit) = resolvedLaunch({
        showProgressDialog(context)
        ChannelInteractor.getInstance().getChannelAsync(context, channelId).await()?.let { channel ->
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

            val intent = Intent(context, ChannelActivity::class.java)
            intent.putExtra(CHANNEL_DATA_KEY, channel)
            context.startActivity(intent)
        }
        hideProgressDialog()
    }, {
        hideProgressDialog()
        onLoad(null)
    })

    fun changeFavourite(channel: Channel) = resolvedLaunch({
        ChannelInteractor.getInstance().changeFavouriteChannelAsync(channel).await()
    }, {})

}
