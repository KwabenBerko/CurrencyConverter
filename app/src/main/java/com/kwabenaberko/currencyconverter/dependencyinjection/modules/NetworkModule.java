package com.kwabenaberko.currencyconverter.dependencyinjection.modules;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kwabenaberko.currencyconverter.BuildConfig;
import com.kwabenaberko.currencyconverter.data.remote.RemoteDataSource;
import com.kwabenaberko.currencyconverter.util.network.NetworkHelper;
import com.kwabenaberko.currencyconverter.util.network.NetworkHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {ApplicationModule.class})
public class NetworkModule {

    private String baseUrl;

    public NetworkModule(String baseUrl){
        this.baseUrl = baseUrl;
    }


    @Singleton
    @Provides
    Gson provideGson(){
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor(){
        return new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor){
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    RemoteDataSource provideCurrencyConverterApi(Retrofit retrofit){
        return retrofit.create(RemoteDataSource.class);
    }

    @Singleton
    @Provides
    NetworkHelper provideNetworkHelper(Application application){
        return new NetworkHelperImpl(application.getApplicationContext());
    }
}
