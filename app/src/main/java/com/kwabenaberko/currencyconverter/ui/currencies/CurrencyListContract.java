package com.kwabenaberko.currencyconverter.ui.currencies;

import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.ui.base.BasePresenter;
import com.kwabenaberko.currencyconverter.ui.base.BaseView;

import java.util.List;

public interface CurrencyListContract {

    interface View extends BaseView {
        void onFromCurrenciesLoaded(List<Currency> currencies, Currency selectedCurrency);
        void onToCurrenciesLoaded(List<Currency> currencies, Currency selectedCurrency);
        void onCurrencyConverted(Double amount);
    }


    interface Presenter extends BasePresenter<View> {
        void loadCurrencies();
        void convertCurrency(Currency from, Currency to, Double amount);
    }

}
