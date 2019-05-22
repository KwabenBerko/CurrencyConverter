package com.kwabenaberko.currencyconverter.model

import com.google.gson.annotations.SerializedName

data class Conversion(
        val id: String,

        @SerializedName("val")
        val value: Number,

        val to: String,

        @SerializedName("fr")
        val from: String
)