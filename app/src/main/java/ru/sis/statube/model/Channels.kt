package ru.sis.statube.model

import java.io.Serializable

class Channels : Serializable {

    var nextPageToken: String? = null

    var channelList = ArrayList<Channel>()

}