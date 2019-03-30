package ru.sis.statube.net.response.json.youtube.channel

import com.google.gson.annotations.SerializedName

class BrandingSettingsImageResponse {

    @SerializedName("bannerImageUrl")
    var bannerImageUrl: String? = null

    @SerializedName("bannerMobileImageUrl")
    var bannerMobileImageUrl: String? = null

    @SerializedName("bannerMobileLowImageUrl")
    var bannerMobileLowImageUrl: String? = null

    @SerializedName("bannerMobileMediumHdImageUrl")
    var bannerMobileMediumHdImageUrl: String? = null

    @SerializedName("bannerMobileHdImageUrl")
    var bannerMobileHdImageUrl: String? = null

    @SerializedName("bannerMobileExtraHdImageUrl")
    var bannerMobileExtraHdImageUrl: String? = null

}