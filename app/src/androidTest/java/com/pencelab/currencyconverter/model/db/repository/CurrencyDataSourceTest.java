package com.pencelab.currencyconverter.model.db.repository;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pencelab.currencyconverter.model.db.data.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.disposables.CompositeDisposable;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CurrencyDataSourceTest {

    private CurrenciesDatabase currenciesDatabase;

    private CompositeDisposable disposables;

    private Context context;

    private CurrencyDataSource currencyDataSource;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        this.context = InstrumentationRegistry.getContext();
        this.disposables = new CompositeDisposable();
        this.currenciesDatabase = Room.inMemoryDatabaseBuilder(this.context, CurrenciesDatabase.class)
                .allowMainThreadQueries()
                .build();
        this.currencyDataSource = new CurrencyDataSource(this.currenciesDatabase.currencyDao());
    }

    @After
    public void tearDown() {
        this.currenciesDatabase.close();
        this.disposables.dispose();
    }

    @Test
    public void insertOrUpdateCurrency() {
        Currency currency = new Currency("ABC", "Abc", "$");
        this.currencyDataSource.insertOrUpdateCurrency(currency);
        this.currencyDataSource.getMaybeCurrencies()
                .test()
                .assertValue(list -> list.size() > 0);

    }
}