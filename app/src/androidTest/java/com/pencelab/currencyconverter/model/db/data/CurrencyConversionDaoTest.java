package com.pencelab.currencyconverter.model.db.data;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pencelab.currencyconverter.common.BigDecimalFactory;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CurrencyConversionDaoTest {

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
    public void shouldInsertOneElement() {

        String baseCode = "ABC";
        String targetCode = "DEF";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency baseCurrency = new Currency(baseCode, "Abc", "A");
        Currency targetCurrency = new Currency(targetCode, "Def", "D");

        Currency[] currencies = {baseCurrency, targetCurrency};

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        CurrencyConversion currencyConversion = new CurrencyConversion(baseCode, targetCode, value, date, source);

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversion(currencyConversion);

        this.currenciesDatabase.currencyConversionDao().getMaybeCurrencyConversions()
                .test()
                .assertValue(list -> list.size() == 1 && list.get(0).equals(currencyConversion));
    }

    @Test
    public void shouldInsertThreeEqualElements() {

        String baseCode = "ABC";
        String targetCode = "DEF";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency baseCurrency = new Currency(baseCode, "Abc", "A");
        Currency targetCurrency = new Currency(targetCode, "Def", "D");

        Currency[] currencies = {baseCurrency, targetCurrency};

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode, targetCode, value, date, source),
                new CurrencyConversion(baseCode, targetCode, value, date, source),
                new CurrencyConversion(baseCode, targetCode, value, date, source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionDao().getMaybeCurrencyConversions()
                .test()
                .assertValue(list -> list.size() == 3);
    }

    @Test(expected = SQLiteConstraintException.class)
    public void shouldFailInsertingOneElementWithTargetCodeNotRegistered() {

        String baseCode = "ABC";
        String targetCode = "DEF";
        String unsavedCode = "GHI";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency baseCurrency = new Currency(baseCode, "Abc", "A");
        Currency targetCurrency = new Currency(targetCode, "Def", "D");

        Currency[] currencies = {baseCurrency, targetCurrency};

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversion(new CurrencyConversion(baseCode, unsavedCode, value, date, source));
    }

    @Test
    public void shouldRetrieveNoElements() {

        String baseCode1 = "FGH";
        String baseCode2 = "IJK";
        String targetCode1 = "ABC";
        String targetCode2 = "OPQ";
        String targetCode3 = "RST";

        String baseCodeNotPresent = "ZZZ";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency[] currencies = {
                new Currency(targetCode1, "Abc", "лв"),
                new Currency("DEF", "Def", "$"),
                new Currency(baseCode1, "Fgh", "$"),
                new Currency(baseCode2, "Ijk", "$"),
                new Currency("LMN", "Lmn", "$b"),
                new Currency(targetCode2, "Opq", "$"),
                new Currency(targetCode3, "Rst", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode1, value, date, source),
                new CurrencyConversion(baseCode1, targetCode2, value, date, source),
                new CurrencyConversion(baseCode1, targetCode3, value, date, source),
                new CurrencyConversion(baseCode2, targetCode1, value, date, source),
                new CurrencyConversion(baseCode2, targetCode2, value, date, source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionDao().getMaybeLatestDistinctCurrencyConversionsByBase(baseCodeNotPresent)
                .test()
                .assertValue(list -> list.size() == 0);
    }

    @Test
    public void shouldRetrieveThreeElementsByBaseCodeAndTargetCode() {

        String baseCode1 = "FGH";
        String baseCode2 = "IJK";
        String targetCode1 = "ABC";
        String targetCode2 = "OPQ";
        String targetCode3 = "RST";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency[] currencies = {
                new Currency(targetCode1, "Abc", "лв"),
                new Currency("DEF", "Def", "$"),
                new Currency(baseCode1, "Fgh", "$"),
                new Currency(baseCode2, "Ijk", "$"),
                new Currency("LMN", "Lmn", "$b"),
                new Currency(targetCode2, "Opq", "$"),
                new Currency(targetCode3, "Rst", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        long now = date.getTime();

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode3, value, new Date(now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)), source),
                new CurrencyConversion(baseCode1, targetCode1, value, date, source),
                new CurrencyConversion(baseCode1, targetCode2, value, date, source),
                new CurrencyConversion(baseCode1, targetCode3, value, date, source),
                new CurrencyConversion(baseCode2, targetCode1, value, date, source),
                new CurrencyConversion(baseCode1, targetCode3, value, new Date(now - TimeUnit.MILLISECONDS.convert(14, TimeUnit.HOURS)), source),
                new CurrencyConversion(baseCode2, targetCode2, value, date, source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionDao().getMaybeCurrencyConversionsByBaseAndTarget(baseCode1, targetCode3)
                .test()
                .assertValue(list -> list.size() == 3 && list.contains(currencyConversions[0]) && list.contains(currencyConversions[3]) && list.contains(currencyConversions[5]));
    }

    @Test
    public void shouldRetrieveThreeDistinctElementsByBaseCode() {
        String baseCode1 = "FGH";
        String baseCode2 = "IJK";
        String targetCode1 = "ABC";
        String targetCode2 = "OPQ";
        String targetCode3 = "RST";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency[] currencies = {
                new Currency(targetCode1, "Abc", "лв"),
                new Currency("DEF", "Def", "$"),
                new Currency(baseCode1, "Fgh", "$"),
                new Currency(baseCode2, "Ijk", "$"),
                new Currency("LMN", "Lmn", "$b"),
                new Currency(targetCode2, "Opq", "$"),
                new Currency(targetCode3, "Rst", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        long now = date.getTime();

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode3, value, new Date(now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)), source),
                new CurrencyConversion(baseCode1, targetCode1, value, date, source),
                new CurrencyConversion(baseCode1, targetCode2, value, date, source),
                new CurrencyConversion(baseCode1, targetCode1, value, new Date(now - TimeUnit.MILLISECONDS.convert(47, TimeUnit.MINUTES)), source),
                new CurrencyConversion(baseCode1, targetCode3, value, date, source),
                new CurrencyConversion(baseCode2, targetCode1, value, date, source),
                new CurrencyConversion(baseCode1, targetCode3, value, new Date(now - TimeUnit.MILLISECONDS.convert(14, TimeUnit.HOURS)), source),
                new CurrencyConversion(baseCode2, targetCode2, value, date, source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionDao().getMaybeLatestDistinctCurrencyConversionsByBase(baseCode1)
                .test()
                .assertValue(list -> list.size() == 3 && list.contains(currencyConversions[1]) && list.contains(currencyConversions[2]) && list.contains(currencyConversions[4]));
    }

    @Test
    public void shouldRetrieveAllLatestElements() {
        String baseCode1 = "FGH";
        String baseCode2 = "IJK";
        String targetCode1 = "ABC";
        String targetCode2 = "OPQ";
        String targetCode3 = "RST";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency[] currencies = {
                new Currency(targetCode1, "Abc", "лв"),
                new Currency("DEF", "Def", "$"),
                new Currency(baseCode1, "Fgh", "$"),
                new Currency(baseCode2, "Ijk", "$"),
                new Currency("LMN", "Lmn", "$b"),
                new Currency(targetCode2, "Opq", "$"),
                new Currency(targetCode3, "Rst", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        long now = date.getTime();

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode3, value, new Date(now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)), source),
                new CurrencyConversion(baseCode1, targetCode1, value, date, source),
                new CurrencyConversion(baseCode1, targetCode2, value, date, source),
                new CurrencyConversion(baseCode1, targetCode1, value, new Date(now - TimeUnit.MILLISECONDS.convert(47, TimeUnit.MINUTES)), source),
                new CurrencyConversion(baseCode1, targetCode3, value, date, source),
                new CurrencyConversion(baseCode2, targetCode1, value, new Date(now - TimeUnit.MILLISECONDS.convert(32, TimeUnit.SECONDS)), source),
                new CurrencyConversion(baseCode2, targetCode1, value, date, source),
                new CurrencyConversion(baseCode1, targetCode3, value, new Date(now - TimeUnit.MILLISECONDS.convert(14, TimeUnit.HOURS)), source),
                new CurrencyConversion(baseCode2, targetCode2, value, date, source),
                new CurrencyConversion(baseCode2, targetCode2, value, new Date(now - TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS)), source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionDao().getMaybeLatestDistinctCurrencyConversions()
                .test()
                .assertValue(list ->
                    list.size() == 5
                        && list.contains(currencyConversions[1]) && currencyConversions[1].getDate().equals(date)
                        && list.contains(currencyConversions[2]) && currencyConversions[2].getDate().equals(date)
                        && list.contains(currencyConversions[4]) && currencyConversions[4].getDate().equals(date)
                        && list.contains(currencyConversions[6]) && currencyConversions[6].getDate().equals(date)
                        && list.contains(currencyConversions[8]) && currencyConversions[8].getDate().equals(date)
                );
    }

    @Test
    public void shouldRetrieveTheLatestElementByBaseCodeAndTargetCode() {
        String baseCode1 = "FGH";
        String baseCode2 = "IJK";
        String targetCode1 = "ABC";
        String targetCode2 = "OPQ";
        String targetCode3 = "RST";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency[] currencies = {
                new Currency(targetCode1, "Abc", "лв"),
                new Currency("DEF", "Def", "$"),
                new Currency(baseCode1, "Fgh", "$"),
                new Currency(baseCode2, "Ijk", "$"),
                new Currency("LMN", "Lmn", "$b"),
                new Currency(targetCode2, "Opq", "$"),
                new Currency(targetCode3, "Rst", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        long now = date.getTime();

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode3, value, new Date(now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)), source),
                new CurrencyConversion(baseCode1, targetCode1, value, date, source),
                new CurrencyConversion(baseCode1, targetCode2, value, date, source),
                new CurrencyConversion(baseCode1, targetCode1, value, new Date(now - TimeUnit.MILLISECONDS.convert(47, TimeUnit.MINUTES)), source),
                new CurrencyConversion(baseCode1, targetCode3, value, date, source),
                new CurrencyConversion(baseCode2, targetCode1, value, new Date(now - TimeUnit.MILLISECONDS.convert(32, TimeUnit.SECONDS)), source),
                new CurrencyConversion(baseCode2, targetCode1, value, date, source),
                new CurrencyConversion(baseCode1, targetCode3, value, new Date(now - TimeUnit.MILLISECONDS.convert(14, TimeUnit.HOURS)), source),
                new CurrencyConversion(baseCode2, targetCode2, value, date, source),
                new CurrencyConversion(baseCode2, targetCode2, value, new Date(now - TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS)), source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionDao().getMaybeLatestCurrencyConversionByBaseAndTarget(baseCode1, targetCode3)
                .test()
                .assertValue(cc -> cc.equals(currencyConversions[4]) && currencyConversions[4].getDate().equals(date));
    }

    @Test
    public void shouldRetrieveTheLatestElementThenInsertANewOneAndRetrieveTheNewElement() {
        String baseCode = "FGH";
        String targetCode = "ABC";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency[] currencies = {
                new Currency(baseCode, "Fgh", "$"),
                new Currency(targetCode, "Opq", "$")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        long now = date.getTime();

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode, targetCode, value, new Date(now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)), source),
                new CurrencyConversion(baseCode, targetCode, value, date, source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionDao().getMaybeLatestCurrencyConversionByBaseAndTarget(baseCode, targetCode)
                .test()
                .assertValue(cc -> cc.equals(currencyConversions[1]) && currencyConversions[1].getDate().equals(date));

        CurrencyConversion currencyConversion = new CurrencyConversion(baseCode, targetCode, BigDecimalFactory.getBigDecimal(12.54), new Date(), source);

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversion);

        this.currenciesDatabase.currencyConversionDao().getMaybeLatestCurrencyConversionByBaseAndTarget(baseCode, targetCode)
                .test()
                .assertValue(cc -> cc.equals(currencyConversion));

    }

    @Test
    public void shouldUpdateSecondElement() {

        String baseCode1 = "FGH";
        String baseCode2 = "IJK";
        String targetCode1 = "ABC";
        String targetCode2 = "OPQ";
        String targetCode3 = "RST";

        Date date = new Date();
        String oldSource = "ThisSource";
        String newSource = "NewSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency[] currencies = {
                new Currency(targetCode1, "Abc", "лв"),
                new Currency("DEF", "Def", "$"),
                new Currency(baseCode1, "Fgh", "$"),
                new Currency(baseCode2, "Ijk", "$"),
                new Currency("LMN", "Lmn", "$b"),
                new Currency(targetCode2, "Opq", "$"),
                new Currency(targetCode3, "Rst", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode1, value, date, oldSource),
                new CurrencyConversion(baseCode1, targetCode2, value, date, oldSource),
                new CurrencyConversion(baseCode1, targetCode3, value, date, oldSource),
                new CurrencyConversion(baseCode2, targetCode1, value, date, oldSource),
                new CurrencyConversion(baseCode2, targetCode2, value, date, oldSource)
        };

        CurrencyConversion oldCurrencyConversion = currencyConversions[1];

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        AtomicReference<CurrencyConversion> currencyConversion = new AtomicReference<>();

        this.currenciesDatabase.currencyConversionDao().getMaybeLatestCurrencyConversionByBaseAndTarget(oldCurrencyConversion.getBaseCode(), oldCurrencyConversion.getTargetCode())
                .test()
                .assertValue(cc -> {
                        currencyConversion.set(cc);
                        return oldCurrencyConversion.equals(cc);
                });

        CurrencyConversion newCurrencyConversion = new CurrencyConversion(currencyConversion.get().getId(), currencyConversion.get().getBaseCode(), currencyConversion.get().getTargetCode(), value, date, newSource);

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversion(newCurrencyConversion);

        this.currenciesDatabase.currencyConversionDao().getMaybeLatestCurrencyConversionByBaseAndTarget(baseCode1, targetCode2)
                .test()
                .assertValue(cc -> newCurrencyConversion.equals(cc));
    }

    @Test
    public void shouldDeleteAllElements() {

        String baseCode1 = "FGH";
        String baseCode2 = "IJK";
        String targetCode1 = "ABC";
        String targetCode2 = "OPQ";
        String targetCode3 = "RST";

        Date date = new Date();
        String source = "ThisSource";
        BigDecimal value = BigDecimalFactory.getBigDecimal(10.123);

        Currency[] currencies = {
                new Currency(targetCode1, "Abc", "лв"),
                new Currency("DEF", "Def", "$"),
                new Currency(baseCode1, "Fgh", "$"),
                new Currency(baseCode2, "Ijk", "$"),
                new Currency("LMN", "Lmn", "$b"),
                new Currency(targetCode2, "Opq", "$"),
                new Currency(targetCode3, "Rst", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode1, value, date, source),
                new CurrencyConversion(baseCode1, targetCode2, value, date, source),
                new CurrencyConversion(baseCode1, targetCode3, value, date, source),
                new CurrencyConversion(baseCode2, targetCode1, value, date, source),
                new CurrencyConversion(baseCode2, targetCode2, value, date, source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionDao().getMaybeCurrencyConversions()
                .test()
                .assertValue(list -> list.size() == currencyConversions.length);

        this.currenciesDatabase.currencyConversionDao().deleteAllCurrencyConversions();

        this.currenciesDatabase.currencyConversionDao().getMaybeCurrencyConversions()
                .test()
                .assertValue(list -> list.size() == 0);
    }

}