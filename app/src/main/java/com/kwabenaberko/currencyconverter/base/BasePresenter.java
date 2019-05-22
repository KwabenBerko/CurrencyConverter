package com.kwabenaberko.currencyconverter.base;

public interface BasePresenter {
    void attachView(BaseView baseView);
    void detachView();
}
