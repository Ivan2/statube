package ru.sis.statube.net.response.mapper.youtube

import ru.sis.statube.model.BrandingSettings
import ru.sis.statube.net.response.json.youtube.channel.BrandingSettingsResponse

class BrandingSettingsResponseMapper {

    fun map(from: BrandingSettingsResponse?): BrandingSettings? {
        if (from == null)
            return null
        val brandingSettings = BrandingSettings()
        brandingSettings.keywords = from.channel?.keywords
        brandingSettings.bannerImageUrl = from.image?.bannerImageUrl
        brandingSettings.bannerMobileImageUrl = from.image?.bannerMobileImageUrl
        brandingSettings.bannerMobileLowImageUrl = from.image?.bannerMobileLowImageUrl
        brandingSettings.bannerMobileMediumHdImageUrl = from.image?.bannerMobileMediumHdImageUrl
        brandingSettings.bannerMobileHdImageUrl = from.image?.bannerMobileHdImageUrl
        brandingSettings.bannerMobileExtraHdImageUrl = from.image?.bannerMobileExtraHdImageUrl
        return brandingSettings
    }

}