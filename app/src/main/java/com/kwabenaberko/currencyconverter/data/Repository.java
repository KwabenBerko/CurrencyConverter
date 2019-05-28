package com.kwabenaberko.currencyconverter.data;

import com.kwabenaberko.currencyconverter.model.Currency;

import java.util.List;

import io.reactivex.Observable;

public interface Repository {
    Observable<List<Currency>> fetchCurrencies();
    Observable<Double> convert(Currency from, Currency to, Double amount);
}
