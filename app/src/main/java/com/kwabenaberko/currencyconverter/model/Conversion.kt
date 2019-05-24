package com.kwabenaberko.currencyconverter.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Conversion(
        val id: String,

        @SerializedName("val")
        val value: BigDecimal,

        val to: String,

        @SerializedName("fr")
        val from: String
)