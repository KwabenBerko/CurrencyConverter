package com.kwabenaberko.currencyconverter.di.modules;

import com.kwabenaberko.currencyconverter.RxSchedulers;
import com.kwabenaberko.currencyconverter.data.PrefManager;
import com.kwabenaberko.currencyconverter.data.Repository;
import com.kwabenaberko.currencyconverter.ui.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class, DataModule.class})
public class PresenterModule {

    @Singleton
    @Provides
    MainPresenter provideMainPresenter(Repository repository, PrefManager prefManager, RxSchedulers rxSchedulers){
        return new MainPresenter(repository, prefManager, rxSchedulers);
    }
}
