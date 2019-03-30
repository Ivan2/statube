package ru.sis.statube.presentation.screens.channels

import android.content.Context
import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.ChannelInteractor
import ru.sis.statube.model.Channels

class ChannelsPresenter {

    fun searchChannels(context: Context, text: String, pageToken: String? = null, onLoad: (channels: Channels) -> Unit) = resolvedLaunch({
        val channels = ChannelInteractor.getInstance().searchAsync(context, text, pageToken).await()
        onLoad(channels)
    }, {
        onLoad(Channels())
    })

}
