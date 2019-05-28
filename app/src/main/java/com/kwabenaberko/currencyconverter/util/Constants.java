package com.kwabenaberko.currencyconverter.util;

import com.kwabenaberko.currencyconverter.view.MainActivity;

public class Constants {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String BASE_URL = "https://free.currconv.com/api/v7/";
    public static final String API_KEY = "YOUR_API_KEY";
    public static final String PREF_NAME = "currency_converter";
    public static final String PREF_FROM_CURRENCY = "pref_from_currency";
    public static final String PREF_TO_CURRENCY = "pref_to_currency";
}
