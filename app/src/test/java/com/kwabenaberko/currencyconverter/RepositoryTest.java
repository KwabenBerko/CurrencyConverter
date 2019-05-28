package com.kwabenaberko.currencyconverter;

import com.kwabenaberko.currencyconverter.data.Repository;
import com.kwabenaberko.currencyconverter.data.RepositoryImpl;
import com.kwabenaberko.currencyconverter.data.local.LocalDataSource;
import com.kwabenaberko.currencyconverter.data.remote.RemoteDataSource;
import com.kwabenaberko.currencyconverter.model.Conversion;
import com.kwabenaberko.currencyconverter.model.ConversionResponse;
import com.kwabenaberko.currencyconverter.model.CurrenciesResponse;
import com.kwabenaberko.currencyconverter.model.Currency;
import com.kwabenaberko.currencyconverter.util.network.NetworkHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepositoryTest {

    private Repository mRepository;

    @Mock
    public RemoteDataSource mockRemoteDataSource;

    @Mock
    public LocalDataSource mockLocalDataSource;

    @Mock
    public NetworkHelper mNetworkHelper;

    private Map<String, Currency> mCurrencyMap = new HashMap<String, Currency>() {{

    }};
    private CurrenciesResponse mCurrenciesResponse = new CurrenciesResponse(mCurrencyMap);


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mRepository = new RepositoryImpl(mockLocalDataSource, mockRemoteDataSource, mNetworkHelper);
    }

    @Test
    public void getCurrencies_IfNoNetworkAvailable_ReturnLocalData() {
        when(mNetworkHelper.isNetworkAvailable()).thenReturn(false);
        when(mockLocalDataSource.getCurrencies()).thenReturn(Observable.just(new ArrayList<>(mCurrencyMap.values())));

        mRepository.fetchCurrencies()
                .test()
                .assertSubscribed()
                .dispose();

        verify(mockLocalDataSource, times(1)).getCurrencies();
        verify(mockRemoteDataSource, never()).getCurrencies(anyString());
    }

    @Test
    public void getCurrencies_IfNetworkIsAvailable_SaveRemoteDataAndReturnLocalData() {
        when(mNetworkHelper.isNetworkAvailable()).thenReturn(true);
        when(mockLocalDataSource.getCurrencies()).thenReturn(Observable.just(new ArrayList<>(mCurrencyMap.values())));
        when(mockRemoteDataSource.getCurrencies(anyString())).thenReturn(Single.just(mCurrenciesResponse));


        mRepository.fetchCurrencies()
                .test()
                .assertSubscribed()
                .dispose();

        verify(mockRemoteDataSource, times(1)).getCurrencies(anyString());
        verify(mockLocalDataSource, times(1)).deleteAll();
        verify(mockLocalDataSource, times(1)).insertAll(anyList());
        verify(mockLocalDataSource, times(1)).getCurrencies();
    }

    @Test
    public void convertCurrency_IfSuccessful_returnsConvertedAmount() {

        Currency usDollars = new Currency("USD", "United States Dollar", "$");

        Currency ghanaCedis = new Currency("GHS", "Ghanaian Cedi", "GHS");

        String from = usDollars.getId();
        String to = ghanaCedis.getId();
        String query = from + "_" + to;

        Map<String, Conversion> results = new HashMap<String, Conversion>() {{
            put(query, new Conversion(query, BigDecimal.valueOf(5.192702), to, from));
        }};
        ConversionResponse conversionResponse = new ConversionResponse(results);

        when(mockRemoteDataSource.convert(anyString(), anyString())).thenReturn(Single.just(conversionResponse));

        mRepository.convert(usDollars, ghanaCedis, 1799.0)
                .test()
                .assertSubscribed()
                .assertResult(9341.67)
                .dispose();

        verify(mockRemoteDataSource, times(1)).convert(anyString(), anyString());

    }

}
