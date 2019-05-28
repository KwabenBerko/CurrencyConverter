package com.kwabenaberko.currencyconverter.view;

import com.kwabenaberko.currencyconverter.RxSchedulers;
import com.kwabenaberko.currencyconverter.base.BaseView;
import com.kwabenaberko.currencyconverter.data.PrefManager;
import com.kwabenaberko.currencyconverter.data.Repository;
import com.kwabenaberko.currencyconverter.model.Currency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MainPresenter implements MainContract.Presenter {

    private Repository mRepository;
    private PrefManager mPrefManager;
    private RxSchedulers mRxSchedulers;
    private MainContract.View mView;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public MainPresenter(Repository repository, PrefManager prefManager, RxSchedulers rxSchedulers) {
        this.mRepository = repository;
        this.mPrefManager = prefManager;
        this.mRxSchedulers = rxSchedulers;
    }

    @Override
    public void loadCurrencies() {

        mView.showProgress();
        mDisposable.add(mRepository.fetchCurrencies()
                .subscribeOn(mRxSchedulers.io())
                .observeOn(mRxSchedulers.mainThread())
                .subscribe(currencies -> {
                    mView.hideProgress();
                    //Sorting In Ascending Order By Currency Name
                    Collections.sort(currencies, (currencyOne, currencyTwo) ->
                            currencyOne.getCurrencyName().compareToIgnoreCase(currencyTwo.getCurrencyName())
                    );

                    List<Currency> toCurrencies = new ArrayList<>(currencies);
                    List<Currency> fromCurrencies = new ArrayList<>(currencies);

                    Currency fromCurrency = mPrefManager.getFromCurrency();
                    Currency toCurrency = mPrefManager.getToCurrency();

                    if (fromCurrency != null && fromCurrencies.indexOf(fromCurrency) > -1) {
                        fromCurrencies.remove(fromCurrency);
                        fromCurrencies.add(0, fromCurrency);
                    }

                    if (toCurrency != null && toCurrencies.indexOf(toCurrency) > -1) {
                        toCurrencies.remove(toCurrency);
                        toCurrencies.add(0, toCurrency);
                    }


                    mView.onFromCurrenciesLoaded(fromCurrencies);
                    mView.onToCurrenciesLoaded(toCurrencies);

                }, (throwable) -> {
                    throwable.printStackTrace();
                    mView.hideProgress();
                }));
    }

    @Override
    public void convertCurrency(Currency from, Currency to, Double amount) {

        mView.showProgress();
        mDisposable.add(mRepository.convert(from, to, amount)
                .observeOn(mRxSchedulers.mainThread())
                .subscribeOn(mRxSchedulers.io())
                .subscribe(convertedAmount -> {
                    mPrefManager.setFromCurrency(from);
                    mPrefManager.setToCurrency(to);
                    mView.hideProgress();
                    mView.onCurrencyConverted(convertedAmount);
                }, throwable -> mView.hideProgress()));
    }

    @Override
    public void attachView(BaseView baseView) {
        mView = (MainContract.View) baseView;
    }

    @Override
    public void detachView() {
        mDisposable.dispose();
        mView = null;
    }
}
