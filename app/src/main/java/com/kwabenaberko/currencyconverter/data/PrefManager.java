package com.kwabenaberko.currencyconverter.data;

import com.kwabenaberko.currencyconverter.model.Currency;

public interface PrefManager {
    void setFromCurrency(Currency fromCurrency);
    void setToCurrency(Currency toCurrency);
    Currency getFromCurrency();
    Currency getToCurrency();
}
