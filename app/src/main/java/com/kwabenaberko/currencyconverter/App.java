package com.kwabenaberko.currencyconverter;

import android.app.Application;

import com.kwabenaberko.currencyconverter.dependencyinjection.AppComponent;
import com.kwabenaberko.currencyconverter.dependencyinjection.DaggerAppComponent;
import com.kwabenaberko.currencyconverter.dependencyinjection.modules.ApplicationModule;
import com.kwabenaberko.currencyconverter.dependencyinjection.modules.NetworkModule;
import com.kwabenaberko.currencyconverter.util.Constants;

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(Constants.BASE_URL))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
