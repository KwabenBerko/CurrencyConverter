package com.kwabenaberko.currencyconverter.ui.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {

    private V view;
    private CompositeDisposable compositeDisposable;

    protected BasePresenterImpl(){
        compositeDisposable = new CompositeDisposable();
    }

    protected void addDisposable(Disposable disposable){
        compositeDisposable.add(disposable);
    }


    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public boolean isViewAttached() {
        return view != null;
    }

    @Override
    public V getView() {
        return view;
    }

    @Override
    public void detachView() {
        compositeDisposable.dispose();
        view = null;
    }
}
