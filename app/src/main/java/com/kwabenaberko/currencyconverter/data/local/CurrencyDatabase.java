package com.kwabenaberko.currencyconverter.data.local;

import com.kwabenaberko.currencyconverter.model.Currency;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Currency.class}, version = 1, exportSchema = false)
public abstract class CurrencyDatabase extends RoomDatabase {
    public abstract LocalDataSource getCurrencyDao();
}
