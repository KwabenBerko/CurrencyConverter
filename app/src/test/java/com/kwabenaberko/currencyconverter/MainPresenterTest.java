package com.kwabenaberko.currencyconverter;

import com.kwabenaberko.currencyconverter.model.CurrenciesResponse;
import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.service.CurrencyConverterApi;
import com.kwabenaberko.currencyconverter.view.MainContract;
import com.kwabenaberko.currencyconverter.view.MainPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    private MainPresenter mPresenter;

    @Mock
    MainContract.View mView;

    @Mock
    CurrencyConverterApi mConverterApi;


    private CurrenciesResponse mCurrenciesResponse = new CurrenciesResponse(
            new HashMap<String, Currency>() {{
                put("GHS", new Currency("GHS", "Ghanaian Cedi", "GHS"));
                put("USD", new Currency("USD", "United States Dollar", "$"));
                put("EUR", new Currency("EUR", "Euro", "€"));
            }}
    );

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new MainPresenter(mConverterApi);
        mPresenter.attachView(mView);
    }

    @Test
    public void getCurrencies_IfSuccessful_ReturnsListOfCurrencies() {

        ArgumentCaptor<List<Currency>> captor = ArgumentCaptor.forClass(List.class);

        Call<CurrenciesResponse> mockCall = mock(Call.class);
        when(mConverterApi.getCurrencies(anyString())).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<CurrenciesResponse> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.success(mCurrenciesResponse));
            return null;
        }).when(mockCall).enqueue(any(Callback.class));

        mPresenter.loadCurrencies();

        verify(mView, times(1)).onLoading(true);
        verify(mView, times(1)).onLoading(false);
        verify(mView).onCurrenciesLoaded(anyList());

        //verifying that the list returned has been sorted.
        verify(mView).onCurrenciesLoaded(captor.capture());
        assertEquals(captor.getValue().size(), 3);
        assertEquals(captor.getValue().get(0).getCurrencyName(), "Euro");
    }

}
