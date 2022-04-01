package com.example.exchangeapp.db

import androidx.room.Entity

@Entity(tableName = "currency", primaryKeys = ["name","baseName"])
data class CurrencyDto(
    val name: String,
    val value: Double,
    val baseName: String,
    val baseValue: Double = 1.0
)