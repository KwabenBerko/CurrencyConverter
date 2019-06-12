package com.kwabenaberko.currencyconverter.ui.currencies;

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

public class CurrencyListActivity extends AppCompatActivity implements CurrencyListContract.View {

    @Inject
    CurrencyListPresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.loading_view) RelativeLayout loadingViewLayout;
    @BindView(R.id.from_currency) Spinner fromCurrencySpinner;
    @BindView(R.id.to_currency) Spinner toCurrencySpinner;
    @BindView(R.id.from_amount) EditText fromAmountEditText;
    @BindView(R.id.to_amount) EditText toAmountEditText;
    @BindView(R.id.convert_btn) Button convertBtn;

    private CurrencyListAdapter fromCurrencyListAdapter, toCurrencyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        fromCurrencyListAdapter = new CurrencyListAdapter();
        fromCurrencySpinner.setAdapter(fromCurrencyListAdapter);

        toCurrencyListAdapter = new CurrencyListAdapter();
        toCurrencySpinner.setAdapter(toCurrencyListAdapter);

        mPresenter.attachView(this);

        convertBtn.setOnClickListener(v -> {
            Currency fromCurrency = (Currency) fromCurrencySpinner.getSelectedItem();
            Currency toCurrency = (Currency) toCurrencySpinner.getSelectedItem();
            mPresenter.convertCurrency(
                    fromCurrency,
                    toCurrency,
                    Double.parseDouble(fromAmountEditText.getText().toString()));
        });


        mPresenter.loadCurrencies();
    }


    @Override
    public void onFromCurrenciesLoaded(List<Currency> currencies, Currency selectedCurrency) {
        fromCurrencyListAdapter.setCurrencies(currencies);
        int index = currencies != null? currencies.indexOf(selectedCurrency) : -1;
        if(index > -1){
            fromCurrencySpinner.setSelection(index);
        }
    }

    @Override
    public void onToCurrenciesLoaded(List<Currency> currencies, Currency selectedCurrency) {
        toCurrencyListAdapter.setCurrencies(currencies);
        int index = currencies != null? currencies.indexOf(selectedCurrency) : -1;
        if(index > -1){
            toCurrencySpinner.setSelection(index);
        }
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
