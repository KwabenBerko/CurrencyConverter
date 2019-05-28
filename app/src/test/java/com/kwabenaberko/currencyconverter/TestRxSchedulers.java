package com.kwabenaberko.currencyconverter;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class TestRxSchedulers extends RxSchedulers {
    @Override
    public Scheduler mainThread() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }
}
