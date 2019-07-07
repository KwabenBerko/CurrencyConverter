package com.kwabenaberko.currencyconverter;

import com.kwabenaberko.currencyconverter.data.PrefManager;
import com.kwabenaberko.currencyconverter.data.Repository;
import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.ui.currencies.CurrencyListContract;
import com.kwabenaberko.currencyconverter.ui.currencies.CurrencyListPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CurrencyListPresenterTest {

    private CurrencyListPresenter mPresenter;


    @Mock
    CurrencyListContract.View mockView;

    @Mock
    Repository mockRepository;

    @Mock
    PrefManager mockPrefManager;

    private List<Currency> mCurrencies = Arrays.asList(
            new Currency("USD", "United States Dollar", "$"),
            new Currency("GHS", "Ghanaian Cedi", "GHS")
    );


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new CurrencyListPresenter(mockRepository, mockPrefManager, new TestRxSchedulers());
        mPresenter.attachView(mockView);
    }

    @Test
    public void loadCurrencies_IfSuccessful_shouldInvokeViewsCurrenciesLoadedMethods() {

        when(mockRepository.fetchCurrencies()).thenReturn(Observable.just(mCurrencies));

        mPresenter.loadCurrencies();

        verify(mockView, times(1)).showProgress();
        verify(mockRepository, times(1)).fetchCurrencies();
        verify(mockView, times(1)).hideProgress();
        verify(mockView, times(1)).showFromCurrenciesLoaded(anyList(), any());
        verify(mockView, times(1)).showCurrenciesLoaded(anyList(), any());
    }

    @Test
    public void loadCurrencies_IfSuccessful_shouldSortCurrenciesInAscendingOrder() {

        ArgumentCaptor<List<Currency>> captor = ArgumentCaptor.forClass(List.class);

        when(mockRepository.fetchCurrencies()).thenReturn(Observable.just(mCurrencies));

        mPresenter.loadCurrencies();

        verify(mockView, times(1)).showFromCurrenciesLoaded(captor.capture(), any());

        assertEquals(captor.getValue().get(0).getId(), "GHS");

        verify(mockView, times(1)).showCurrenciesLoaded(captor.capture(), any());

        assertEquals(captor.getValue().get(0).getId(), "GHS");
    }

    @Test
    public void loadCurrencies_IfSuccessful_ShouldSetDefaultCurrenciesFromPreferences(){

        ArgumentCaptor<Currency> captor = ArgumentCaptor.forClass(Currency.class);

        when(mockRepository.fetchCurrencies()).thenReturn(Observable.just(mCurrencies));

        when(mockPrefManager.getFromCurrency()).thenReturn(mCurrencies.get(0));
        when(mockPrefManager.getToCurrency()).thenReturn(mCurrencies.get(1));

        mPresenter.loadCurrencies();

        verify(mockView, times(1)).showFromCurrenciesLoaded(anyList(), captor.capture());

        assertEquals(captor.getValue().getId(), "USD");

        verify(mockView, times(1)).showCurrenciesLoaded(anyList(), captor.capture());

        assertEquals(captor.getValue().getId(), "GHS");

    }
}
