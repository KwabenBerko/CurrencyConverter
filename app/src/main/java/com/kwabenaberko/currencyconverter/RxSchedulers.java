package com.kwabenaberko.currencyconverter;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulers {

    public Scheduler mainThread(){
        return AndroidSchedulers.mainThread();
    }

    public Scheduler io(){
        return Schedulers.io();
    }

}
