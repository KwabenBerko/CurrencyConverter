package com.kwabenaberko.currencyconverter.service;

import com.kwabenaberko.currencyconverter.model.CurrenciesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyConverterApi {

    @GET("currencies")
    Call<CurrenciesResponse> getCurrencies(@Query("apiKey") String apiKey);
}
