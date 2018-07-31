package com.pencelab.currencyconverter.model.db.data;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pencelab.currencyconverter.common.BigDecimalFactory;
import com.pencelab.currencyconverter.common.Utils;
import com.pencelab.currencyconverter.model.db.repository.CurrenciesDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CurrencyConversionPlusDaoTest {

    private CurrenciesDatabase currenciesDatabase;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    //Make sure that Room executes all the database operations instantly
    @Rule public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        this.currenciesDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), CurrenciesDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void tearDown() {
        this.currenciesDatabase.close();
    }

    @Test
    public void shouldRetrieveAllElements() {

        String baseCode1 = "FGH";
        String baseName1 = "Fgh";
        String baseCode2 = "IJK";
        String baseName2 = "Ijk";
        String targetCode1 = "ABC";
        String targetName1 = "Abc";
        String targetCode2 = "LMN";
        String targetName2 = "Lmn";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency[] currencies = {
                new Currency(baseCode1, baseName1, "лв"),
                new Currency(baseCode2, baseName2, "$"),
                new Currency(targetCode1, targetName1, "$"),
                new Currency(targetCode2, targetName2, "$")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode1, value, date, source),
                new CurrencyConversion(baseCode2, targetCode2, value, date, source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionPlusDao().getFlowableCurrencyConversions()
                .test()
                .assertValue(list -> list.size() == 2
                    && (list.get(0).getBaseCurrencyName().equals(baseName1) || list.get(0).getBaseCurrencyName().equals(baseName2))
                    && (list.get(0).getTargetCurrencyName().equals(targetName1) || list.get(0).getTargetCurrencyName().equals(targetName2))
                    && (list.get(1).getBaseCurrencyName().equals(baseName1) || list.get(1).getBaseCurrencyName().equals(baseName2))
                    && (list.get(1).getTargetCurrencyName().equals(targetName1) || list.get(1).getTargetCurrencyName().equals(targetName2)));
    }
}