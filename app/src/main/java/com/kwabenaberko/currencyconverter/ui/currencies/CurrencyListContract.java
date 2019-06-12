package com.kwabenaberko.currencyconverter.ui.currencies;

import com.kwabenaberko.currencyconverter.base.BasePresenter;
import com.kwabenaberko.currencyconverter.base.BaseView;
import com.kwabenaberko.currencyconverter.model.Currency;

import java.util.List;

public interface CurrencyListContract {

    interface Presenter extends BasePresenter {
        void loadCurrencies();
        void convertCurrency(Currency from, Currency to, Double amount);
    }

    interface View extends BaseView {
        void onFromCurrenciesLoaded(List<Currency> currencies, Currency selectedCurrency);
        void onToCurrenciesLoaded(List<Currency> currencies, Currency selectedCurrency);
        void onCurrencyConverted(Double amount);
    }

}