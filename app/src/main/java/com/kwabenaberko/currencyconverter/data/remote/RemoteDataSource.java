package com.kwabenaberko.currencyconverter.data.remote;

import com.kwabenaberko.currencyconverter.model.ConversionResponse;
import com.kwabenaberko.currencyconverter.model.CurrenciesResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemoteDataSource {

    @GET("currencies")
    Single<CurrenciesResponse> getCurrencies(@Query("apiKey") String apiKey);

    @GET("convert")
    Single<ConversionResponse> convert(@Query("q") String query, @Query("apiKey")String apiKey);
}
