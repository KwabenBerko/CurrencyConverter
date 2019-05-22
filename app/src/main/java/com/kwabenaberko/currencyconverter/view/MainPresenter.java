package com.kwabenaberko.currencyconverter.view;

import com.kwabenaberko.currencyconverter.base.BaseView;
import com.kwabenaberko.currencyconverter.model.CurrenciesResponse;
import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.service.CurrencyConverterApi;

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
        mView.onLoading(true);
        mConverterApi.getCurrencies("YOUR_API_KEY")
                .enqueue(new Callback<CurrenciesResponse>() {
                    @Override
                    public void onResponse(Call<CurrenciesResponse> call, Response<CurrenciesResponse> response) {
                        mView.onLoading(false);
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
                        mView.onLoading(false);
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
