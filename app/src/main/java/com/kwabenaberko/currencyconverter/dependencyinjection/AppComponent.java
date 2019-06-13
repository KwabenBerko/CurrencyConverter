package com.kwabenaberko.currencyconverter.dependencyinjection;

import com.kwabenaberko.currencyconverter.dependencyinjection.modules.ApplicationModule;
import com.kwabenaberko.currencyconverter.dependencyinjection.modules.DataModule;
import com.kwabenaberko.currencyconverter.dependencyinjection.modules.NetworkModule;
import com.kwabenaberko.currencyconverter.dependencyinjection.modules.PresenterModule;
import com.kwabenaberko.currencyconverter.ui.currencies.CurrencyListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        DataModule.class,
        NetworkModule.class,
        PresenterModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder{
        Builder networkModule(NetworkModule networkModule);
        Builder applicationModule(ApplicationModule applicationModule);
        AppComponent build();
    }

    void inject(CurrencyListActivity currencyListActivity);
}
