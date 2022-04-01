package com.example.exchangeapp.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency")
    fun getAll(): Flow<List<CurrencyDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currency: CurrencyDto)

    @Delete
    suspend fun delete(currency: CurrencyDto)

    @Query("SELECT * FROM currency WHERE name = :name AND baseName = :baseName")
    fun getCurrencyByKeys(name: String, baseName: String): CurrencyDto
}