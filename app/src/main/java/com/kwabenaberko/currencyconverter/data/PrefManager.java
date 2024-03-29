package com.kwabenaberko.currencyconverter.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.util.Constants;

import javax.inject.Inject;

public class PrefManager {

    private SharedPreferences mPreferences;
    private Gson mGson;

    @Inject
    public PrefManager(Application application){
        mPreferences = application.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void setFromCurrency(Currency fromCurrency) {
        mPreferences.edit()
                .putString(Constants.PREF_FROM_CURRENCY, mGson.toJson(fromCurrency))
                .apply();
    }

    public void setToCurrency(Currency toCurrency) {
        mPreferences.edit()
                .putString(Constants.PREF_TO_CURRENCY, mGson.toJson(toCurrency))
                .apply();
    }

    public Currency getFromCurrency() {
        Currency currency = null;
        String json = mPreferences.getString(Constants.PREF_FROM_CURRENCY, null);

        if(json != null){
            currency = mGson.fromJson(json, Currency.class);
        }

        return currency;
    }

    public Currency getToCurrency() {
        Currency currency = null;
        String json = mPreferences.getString(Constants.PREF_TO_CURRENCY, null);

        if(json != null){
            currency = mGson.fromJson(json, Currency.class);
        }

        return currency;
    }
}
