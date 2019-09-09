package ru.sis.statube.presentation.screens.channel

import ru.sis.statube.additional.launch
import ru.sis.statube.interactor.ChannelInteractor
import ru.sis.statube.model.Channel
import ru.sis.statube.presentation.presenter.Presenter

class ChannelPresenter : Presenter() {

    fun changeFavourite(channel: Channel) = launch {
        ChannelInteractor.getInstance().changeFavouriteChannelAsync(channel).await()
    }

}
