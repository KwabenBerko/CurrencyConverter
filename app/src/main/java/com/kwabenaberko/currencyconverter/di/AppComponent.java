package com.kwabenaberko.currencyconverter.di;

import com.kwabenaberko.currencyconverter.di.modules.NetworkModule;
import com.kwabenaberko.currencyconverter.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        NetworkModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder{
        Builder networkModule(NetworkModule networkModule);
        AppComponent build();
    }

    void inject(MainActivity mainActivity);
}
