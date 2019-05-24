package com.kwabenaberko.currencyconverter;

import com.kwabenaberko.currencyconverter.model.Conversion;
import com.kwabenaberko.currencyconverter.model.ConversionResponse;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
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
    MainContract.View mockView;

    @Mock
    CurrencyConverterApi mockConverterApi;


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
        mPresenter = new MainPresenter(mockConverterApi);
        mPresenter.attachView(mockView);
    }

    @Test
    public void getCurrencies_IfSuccessful_ReturnsListOfCurrencies() {

        ArgumentCaptor<List<Currency>> captor = ArgumentCaptor.forClass(List.class);

        Call<CurrenciesResponse> mockCall = mock(Call.class);
        when(mockConverterApi.getCurrencies(anyString())).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<CurrenciesResponse> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.success(mCurrenciesResponse));
            return null;
        }).when(mockCall).enqueue(any(Callback.class));

        mPresenter.loadCurrencies();

        verify(mockView, times(1)).showProgress();
        verify(mockView, times(1)).hideProgress();
        verify(mockView).onCurrenciesLoaded(anyList());

        //verifying that the list returned has been sorted.
        verify(mockView).onCurrenciesLoaded(captor.capture());
        assertEquals(captor.getValue().size(), 3);
        assertEquals(captor.getValue().get(0).getCurrencyName(), "Euro");
    }

    @Test
    public void convertCurrency_IfSuccessful_ReturnsFormattedConvertedAmount(){

        Currency usDollars = new Currency("USD", "United States Dollar", "$");

        Currency ghanaCedis = new Currency("GHS", "Ghanaian Cedi", "GHS");

        String from = usDollars.getId();
        String to = ghanaCedis.getId();
        String query = from+"_"+to;

        Map<String, Conversion> results = new HashMap<String, Conversion>(){{
            put(query, new Conversion(query, BigDecimal.valueOf(5.192702), to, from));
        }};
        ConversionResponse conversionResponse = new ConversionResponse(results);

        ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
        Call<ConversionResponse> mockCall = mock(Call.class);

        when(mockConverterApi.convert(anyString(), anyString())).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<ConversionResponse> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.success(conversionResponse));
            return null;
        }).when(mockCall).enqueue(any(Callback.class));


        mPresenter.convertCurrency(usDollars, ghanaCedis, 1799.0);
        verify(mockView, times(1)).showProgress();
        verify(mockView, times(1)).onCurrencyConverted(anyDouble());
        verify(mockView, times(1)).hideProgress();
        verify(mockView).onCurrencyConverted(captor.capture());
        assertEquals(captor.getValue().toString(), String.valueOf(9341.67));

    }

}
