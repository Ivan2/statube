package ru.sis.statube.presentation.screens.channel

import ru.sis.statube.additional.resolvedLaunch
import ru.sis.statube.interactor.ChannelInteractor
import ru.sis.statube.model.Channel
import ru.sis.statube.presentation.Presenter

class ChannelPresenter : Presenter() {

    fun changeFavourite(channel: Channel) = resolvedLaunch({
        ChannelInteractor.getInstance().changeFavouriteAsync(channel).await()
    }, {})

}
