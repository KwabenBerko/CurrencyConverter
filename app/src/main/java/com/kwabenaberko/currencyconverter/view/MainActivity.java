package com.kwabenaberko.currencyconverter.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.kwabenaberko.currencyconverter.App;
import com.kwabenaberko.currencyconverter.R;
import com.kwabenaberko.currencyconverter.model.Currency;

import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @Inject
    MainPresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.loading_view) RelativeLayout loadingViewLayout;

    @BindView(R.id.from_currency) Spinner fromCurrencySpinner;

    @BindView(R.id.to_currency) Spinner toCurrencySpinner;

    @BindView(R.id.from_amount) EditText amountEditText;

    @BindView(R.id.to_amount) EditText toAmountEditText;

    private CurrencyAdapter mCurrencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mCurrencyAdapter = new CurrencyAdapter();
        fromCurrencySpinner.setAdapter(mCurrencyAdapter);
        toCurrencySpinner.setAdapter(mCurrencyAdapter);

        mPresenter.attachView(this);
        mPresenter.loadCurrencies();
    }

    @Override
    public void onCurrenciesLoaded(List<Currency> currencies) {
        loadingViewLayout.setVisibility(View.GONE);
        mCurrencyAdapter.setCurrencies(currencies);
    }

    @Override
    public void onLoading(boolean isLoading) {
        if(isLoading){
            loadingViewLayout.setVisibility(View.VISIBLE);
            return;
        }

        loadingViewLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
