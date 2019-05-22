package com.kwabenaberko.currencyconverter;

import android.app.Application;

import com.kwabenaberko.currencyconverter.di.AppComponent;
import com.kwabenaberko.currencyconverter.di.DaggerAppComponent;
import com.kwabenaberko.currencyconverter.di.modules.NetworkModule;
import com.kwabenaberko.currencyconverter.util.Constants;

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .networkModule(new NetworkModule(Constants.BASE_URL))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
