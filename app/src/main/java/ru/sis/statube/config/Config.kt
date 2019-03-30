package ru.sis.statube.config

import com.google.gson.annotations.SerializedName

class Config {

    @SerializedName("youtube_data_api_key")
    lateinit var youtubeDataApiKey: String

    @SerializedName("social_blade_email")
    lateinit var socialBladeEmail: String

    @SerializedName("social_blade_token")
    lateinit var socialBladeToken: String

}