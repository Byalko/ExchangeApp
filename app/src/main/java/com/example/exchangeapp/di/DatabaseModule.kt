package com.example.exchangeapp.di

import android.content.Context
import androidx.room.Room
import com.example.exchangeapp.db.CurrencyDao
import com.example.exchangeapp.db.CurrencyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): CurrencyDatabase {
        return Room.databaseBuilder(
            appContext,
            CurrencyDatabase::class.java,
            "currency_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(currencyDatabase: CurrencyDatabase): CurrencyDao {
        return currencyDatabase.currencyDao()
    }

}