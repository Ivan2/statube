package ru.sis.statube.presentation.screens.channel

import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.ChannelInteractor
import ru.sis.statube.model.Channel

class ChannelPresenter {

    fun loadChannel(channelId: String, onLoad: (channel: Channel?) -> Unit) = resolvedLaunch({
        val channel = ChannelInteractor.getInstance().loadAsync(channelId).await()
        onLoad(channel)
    }, {
        onLoad(null)
    })

}
