package com.kwabenaberko.currencyconverter.ui.currencies;

import com.kwabenaberko.currencyconverter.RxSchedulers;
import com.kwabenaberko.currencyconverter.data.PrefManager;
import com.kwabenaberko.currencyconverter.data.Repository;
import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.ui.base.BasePresenterImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class CurrencyListPresenter extends BasePresenterImpl<CurrencyListContract.View> implements CurrencyListContract.Presenter {

    private Repository repository;
    private PrefManager prefManager;
    private RxSchedulers rxSchedulers;

    @Inject
    public CurrencyListPresenter(Repository repository, PrefManager prefManager, RxSchedulers rxSchedulers) {
        this.repository = repository;
        this.prefManager = prefManager;
        this.rxSchedulers = rxSchedulers;
    }

    @Override
    public void loadCurrencies() {

        getView().showProgress();
        addDisposable(repository.fetchCurrencies()
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
                .subscribe(currencies -> {
                    getView().hideProgress();
                    //Sorting In Ascending Order By Currency Name
                    Collections.sort(currencies, (currencyOne, currencyTwo) ->
                            currencyOne.getCurrencyName().compareToIgnoreCase(currencyTwo.getCurrencyName())
                    );

                    List<Currency> toCurrencies = new ArrayList<>(currencies);
                    List<Currency> fromCurrencies = new ArrayList<>(currencies);

                    Currency fromCurrency = prefManager.getFromCurrency();
                    Currency toCurrency = prefManager.getToCurrency();


                    if(isViewAttached()){
                        getView().onFromCurrenciesLoaded(fromCurrencies, fromCurrency);
                        getView().onToCurrenciesLoaded(toCurrencies, toCurrency);
                    }

                }, (throwable) -> {
                    throwable.printStackTrace();
                    getView().hideProgress();
                }));
    }

    @Override
    public void convertCurrency(Currency from, Currency to, Double amount) {

        getView().showProgress();
        addDisposable(repository.convert(from, to, amount)
                .observeOn(rxSchedulers.mainThread())
                .subscribeOn(rxSchedulers.io())
                .subscribe(convertedAmount -> {
                    prefManager.setFromCurrency(from);
                    prefManager.setToCurrency(to);
                    getView().hideProgress();
                    getView().onCurrencyConverted(convertedAmount);
                }, throwable -> getView().hideProgress()));
    }


}
