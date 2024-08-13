package eu.glitch.mywarehouse.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Barcode(
    var code: String,
)
