package com.kwabenaberko.currencyconverter.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    @BindView(R.id.from_amount) EditText fromAmountEditText;
    @BindView(R.id.to_amount) EditText toAmountEditText;
    @BindView(R.id.convert_btn) Button convertBtn;

    private CurrencyAdapter fromCurrencyAdapter, toCurrencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        fromCurrencyAdapter = new CurrencyAdapter();
        fromCurrencySpinner.setAdapter(fromCurrencyAdapter);

        toCurrencyAdapter = new CurrencyAdapter();
        toCurrencySpinner.setAdapter(toCurrencyAdapter);

        mPresenter.attachView(this);
        mPresenter.loadCurrencies();

        convertBtn.setOnClickListener(v -> {
            Currency fromCurrency = (Currency) fromCurrencySpinner.getSelectedItem();
            Currency toCurrency = (Currency) toCurrencySpinner.getSelectedItem();
            mPresenter.convertCurrency(
                    fromCurrency,
                    toCurrency,
                    Double.parseDouble(fromAmountEditText.getText().toString()));
        });
    }

    @Override
    public void onCurrenciesLoaded(List<Currency> currencies) {
        loadingViewLayout.setVisibility(View.GONE);
        fromCurrencyAdapter.setCurrencies(currencies);
        toCurrencyAdapter.setCurrencies(currencies);
    }

    @Override
    public void onCurrencyConverted(Double amount) {
        toAmountEditText.setText(String.valueOf(amount));
    }

    @Override
    public void showProgress() {
        loadingViewLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loadingViewLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
