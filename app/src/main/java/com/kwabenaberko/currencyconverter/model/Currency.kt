package com.kwabenaberko.currencyconverter.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class Currency (
        @PrimaryKey
        val id: String,

        @ColumnInfo
        val currencyName: String,

        val currencySymbol: String? = null
)