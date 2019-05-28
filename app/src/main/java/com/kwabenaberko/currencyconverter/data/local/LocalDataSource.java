package com.kwabenaberko.currencyconverter.data.local;

import com.kwabenaberko.currencyconverter.model.Currency;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Observable;

@Dao
public interface LocalDataSource {

    @Query("SELECT * FROM currencies")
    Observable<List<Currency>> getCurrencies();

    @Insert
    void insertAll(List<Currency> currencies);

    @Query("DELETE FROM currencies")
    void deleteAll();
}
