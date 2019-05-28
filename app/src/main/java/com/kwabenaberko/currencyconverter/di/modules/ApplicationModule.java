package com.kwabenaberko.currencyconverter.di.modules;

import android.app.Application;

import com.kwabenaberko.currencyconverter.RxSchedulers;
import com.kwabenaberko.currencyconverter.data.PrefManager;
import com.kwabenaberko.currencyconverter.data.PrefManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private Application mApplication;

    public ApplicationModule(Application application){
        mApplication = application;
    }

    @Singleton
    @Provides
    Application provideApplication(){
        return mApplication;
    }

    @Singleton
    @Provides
    PrefManager providePrefManager(Application application){
        return new PrefManagerImpl(application);
    }

    @Singleton
    @Provides
    RxSchedulers provideRxSchedulers(){
        return new RxSchedulers();
    }
}
