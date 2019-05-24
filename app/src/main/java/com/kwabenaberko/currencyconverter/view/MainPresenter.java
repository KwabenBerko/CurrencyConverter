package com.kwabenaberko.currencyconverter.view;

import com.kwabenaberko.currencyconverter.base.BaseView;
import com.kwabenaberko.currencyconverter.model.Conversion;
import com.kwabenaberko.currencyconverter.model.ConversionResponse;
import com.kwabenaberko.currencyconverter.model.CurrenciesResponse;
import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.service.CurrencyConverterApi;
import com.kwabenaberko.currencyconverter.util.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter {

    private CurrencyConverterApi mConverterApi;
    private MainContract.View mView;

    @Inject
    public MainPresenter(CurrencyConverterApi converterApi) {
        this.mConverterApi = converterApi;
    }

    @Override
    public void loadCurrencies() {
        //Normally, this operation should be delegated to another layer, say a Repository Layer.
        mView.showProgress();
        mConverterApi.getCurrencies(Constants.API_KEY)
                .enqueue(new Callback<CurrenciesResponse>() {
                    @Override
                    public void onResponse(Call<CurrenciesResponse> call, Response<CurrenciesResponse> response) {
                        mView.hideProgress();
                        if (response.isSuccessful() && response.body() != null) {
                            List<Currency> currencies = new ArrayList<>(response.body().getResults().values());

                            //Sorting In Ascending Order By Currency Name
                            Collections.sort(currencies, (currencyOne, currencyTwo) ->
                                    currencyOne.getCurrencyName().compareToIgnoreCase(currencyTwo.getCurrencyName())
                            );

                            mView.onCurrenciesLoaded(currencies);
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrenciesResponse> call, Throwable t) {
                        mView.hideProgress();
                    }
                });
    }

    @Override
    public void convertCurrency(Currency from, Currency to, Double amount){
        String query = String.format("%s_%s", from.getId().toUpperCase(), to.getId().toUpperCase());

        mView.showProgress();
        mConverterApi.convert(query,Constants.API_KEY)
                .enqueue(new Callback<ConversionResponse>() {
                    @Override
                    public void onResponse(Call<ConversionResponse> call, Response<ConversionResponse> response) {
                        mView.hideProgress();
                        if(response.isSuccessful() && response.body() != null){
                            Conversion conversion = response.body().getResults().get(query);
                            mView.onCurrencyConverted(
                                    BigDecimal.valueOf(amount).multiply(conversion.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue()
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<ConversionResponse> call, Throwable t) {
                        mView.hideProgress();
                    }
                });
    }

    @Override
    public void attachView(BaseView baseView) {
        mView = (MainContract.View) baseView;
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }
}
