package com.kwabenaberko.currencyconverter.dependencyinjection.modules;

import com.kwabenaberko.currencyconverter.RxSchedulers;
import com.kwabenaberko.currencyconverter.data.PrefManager;
import com.kwabenaberko.currencyconverter.data.Repository;
import com.kwabenaberko.currencyconverter.ui.currencies.CurrencyListPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class, DataModule.class})
public class PresenterModule {

    @Singleton
    @Provides
    CurrencyListPresenter provideCurrencyListPresenter(Repository repository, PrefManager prefManager, RxSchedulers rxSchedulers){
        return new CurrencyListPresenter(repository, prefManager, rxSchedulers);
    }
}
