package com.kwabenaberko.currencyconverter.ui.currencies;

import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.ui.base.BasePresenter;
import com.kwabenaberko.currencyconverter.ui.base.BaseView;

import java.util.List;

public interface CurrencyListContract {

    interface View extends BaseView {
        void showFromCurrenciesLoaded(List<Currency> currencies, Currency selectedCurrency);
        void showCurrenciesLoaded(List<Currency> currencies, Currency selectedCurrency);
        void showCurrencyConverted(Double amount);
    }


    interface Presenter extends BasePresenter<View> {
        void loadCurrencies();
        void convertCurrency(Currency from, Currency to, Double amount);
    }

}
