package com.kwabenaberko.currencyconverter.di.modules;

import com.kwabenaberko.currencyconverter.service.CurrencyConverterApi;
import com.kwabenaberko.currencyconverter.view.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {NetworkModule.class})
public class PresenterModule {
    @Singleton
    @Provides
    MainPresenter provideMainPresenter(CurrencyConverterApi converterApi){
        return new MainPresenter(converterApi);
    }
}
