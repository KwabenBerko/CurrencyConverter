package com.kwabenaberko.currencyconverter.dependencyinjection.modules;

import android.app.Application;

import com.kwabenaberko.currencyconverter.R;
import com.kwabenaberko.currencyconverter.data.PrefManager;
import com.kwabenaberko.currencyconverter.data.PrefManagerImpl;
import com.kwabenaberko.currencyconverter.data.Repository;
import com.kwabenaberko.currencyconverter.data.RepositoryImpl;
import com.kwabenaberko.currencyconverter.data.local.LocalDataSource;
import com.kwabenaberko.currencyconverter.data.local.CurrencyDatabase;
import com.kwabenaberko.currencyconverter.data.remote.RemoteDataSource;
import com.kwabenaberko.currencyconverter.util.network.NetworkHelper;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class})
public class DataModule {

    @Singleton
    @Provides
    CurrencyDatabase provideCurrencyDatabase(Application application) {
        return Room.databaseBuilder(application.getApplicationContext(), CurrencyDatabase.class, application.getString(R.string.app_name))
                .build();
    }

    @Singleton
    @Provides
    LocalDataSource provideLocalDataSource(CurrencyDatabase currencyDatabase){
        return currencyDatabase.getCurrencyDao();
    }

    @Singleton
    @Provides
    Repository provideRepository(LocalDataSource localDataSource, RemoteDataSource remoteDataSource, NetworkHelper networkHelper){
        return new RepositoryImpl(localDataSource, remoteDataSource, networkHelper);
    }

    @Singleton
    @Provides
    PrefManager providePrefManager(Application application){
        return new PrefManagerImpl(application);
    }

}
