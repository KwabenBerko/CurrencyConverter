package com.kwabenaberko.currencyconverter.data;

import com.kwabenaberko.currencyconverter.data.local.LocalDataSource;
import com.kwabenaberko.currencyconverter.data.remote.RemoteDataSource;
import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.util.Constants;
import com.kwabenaberko.currencyconverter.util.network.NetworkHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class Repository{

    private LocalDataSource mLocalDataSource;
    private RemoteDataSource mRemoteDataSource;
    private NetworkHelper mNetworkHelper;

    public Repository(
            LocalDataSource localDataSource,
            RemoteDataSource remoteDataSource,
            NetworkHelper networkHelper
    ) {
        this.mLocalDataSource = localDataSource;
        this.mRemoteDataSource = remoteDataSource;
        this.mNetworkHelper = networkHelper;
    }

    public Observable<List<Currency>> fetchCurrencies() {

        if(!mNetworkHelper.isNetworkAvailable()){
            return mLocalDataSource.getCurrencies();
        }

        return mRemoteDataSource.getCurrencies(Constants.API_KEY).toObservable()
                .map(currenciesResponse -> new ArrayList<>(currenciesResponse.getResults().values()))
                .doOnNext(currencies -> {
                    mLocalDataSource.deleteAll();
                    mLocalDataSource.insertAll(currencies);
                })
                .flatMap(currencies -> mLocalDataSource.getCurrencies());

    }

    public Observable<Double> convert(Currency from, Currency to, Double amount) {

        //Dealing With Errors, As Well As Error Cases In Tests
        //Final Piece Of The Puzzle.


        String query = String.format("%s_%s", from.getId().toUpperCase(), to.getId().toUpperCase());

        return mRemoteDataSource.convert(query, Constants.API_KEY).toObservable()
                .flatMap(conversionResponse -> Observable.just(conversionResponse.getResults().get(query)))
                .flatMap(conversion -> Observable.just(
                        BigDecimal.valueOf(amount).multiply(conversion.getValue())
                        .setScale(2, BigDecimal.ROUND_HALF_UP)
                        .doubleValue()
                ));
    }

}
