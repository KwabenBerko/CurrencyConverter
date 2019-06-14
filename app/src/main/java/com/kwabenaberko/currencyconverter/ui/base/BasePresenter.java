package com.kwabenaberko.currencyconverter.ui.base;

public interface BasePresenter <V extends BaseView>{

    void attachView(V view);

    boolean isViewAttached();

    V getView();

    void detachView();

}
