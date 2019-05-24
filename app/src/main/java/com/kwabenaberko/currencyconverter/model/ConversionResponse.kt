package com.kwabenaberko.currencyconverter.model

data class ConversionResponse (
        val results: Map<String, Conversion>
)