package com.kwabenaberko.currencyconverter;

import com.kwabenaberko.currencyconverter.data.PrefManager;
import com.kwabenaberko.currencyconverter.data.Repository;
import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.view.MainContract;
import com.kwabenaberko.currencyconverter.view.MainPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    private MainPresenter mPresenter;


    @Mock
    MainContract.View mockView;

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
        mPresenter = new MainPresenter(mockRepository, mockPrefManager, new TestRxSchedulers());
        mPresenter.attachView(mockView);
    }

    @Test
    public void loadCurrencies_IfSuccessful_shouldInvokeViewsCurrenciesLoadedMethods() {

        when(mockRepository.fetchCurrencies()).thenReturn(Observable.just(mCurrencies));

        mPresenter.loadCurrencies();

        verify(mockView, times(1)).showProgress();
        verify(mockRepository, times(1)).fetchCurrencies();
        verify(mockView, times(1)).hideProgress();
        verify(mockView, times(1)).onFromCurrenciesLoaded(anyList());
        verify(mockView, times(1)).onToCurrenciesLoaded(anyList());
    }

    @Test
    public void loadCurrencies_IfSuccessful_shouldSortCurrenciesInAscendingOrder() {

        ArgumentCaptor<List<Currency>> captor = ArgumentCaptor.forClass(List.class);

        when(mockRepository.fetchCurrencies()).thenReturn(Observable.just(mCurrencies));

        mPresenter.loadCurrencies();


        verify(mockView, times(1)).onFromCurrenciesLoaded(captor.capture());

        assertEquals(captor.getValue().get(0).getId(), "GHS");

        verify(mockView, times(1)).onToCurrenciesLoaded(captor.capture());

        assertEquals(captor.getValue().get(0).getId(), "GHS");
    }

    @Test
    public void loadCurrencies_IfSuccessful_ShouldSetDefaultCurrenciesFromPreferences(){
        when(mockRepository.fetchCurrencies()).thenReturn(Observable.just(mCurrencies));

        mPresenter.loadCurrencies();


    }
}
