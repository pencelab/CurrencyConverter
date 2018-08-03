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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    public void shouldRetrieveNoElements() {

        String baseCode1 = "FGH";
        String baseName1 = "Fgh";
        String baseCode2 = "IJK";
        String baseName2 = "Ijk";
        String targetCode1 = "ABC";
        String targetName1 = "Abc";
        String targetCode2 = "LMN";
        String targetName2 = "Lmn";

        String baseCodeNotPresent = "ZZZ";

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

        this.currenciesDatabase.currencyConversionPlusDao().getMaybeLatestDistinctCurrencyConversionsByBase(baseCodeNotPresent)
                .test()
                .assertValue(list -> list.size() == 0);
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

        this.currenciesDatabase.currencyConversionPlusDao().getMaybeCurrencyConversions()
                .test()
                .assertValue(list -> list.size() == 2
                    && (list.get(0).getBaseCurrencyName().equals(baseName1) || list.get(0).getBaseCurrencyName().equals(baseName2))
                    && (list.get(0).getTargetCurrencyName().equals(targetName1) || list.get(0).getTargetCurrencyName().equals(targetName2))
                    && (list.get(1).getBaseCurrencyName().equals(baseName1) || list.get(1).getBaseCurrencyName().equals(baseName2))
                    && (list.get(1).getTargetCurrencyName().equals(targetName1) || list.get(1).getTargetCurrencyName().equals(targetName2))
                );
    }

    @Test
    public void shouldRetrieveThreeElementsByBaseCodeAndTargetCode() {

        String baseCode1 = "ABC";
        String baseName1 = "Abc";
        String baseCode2 = "DEF";
        String baseName2 = "Def";
        String baseCode3 = "GHI";
        String baseName3 = "Ghi";
        String targetCode1 = "JKL";
        String targetName1 = "Jkl";
        String targetCode2 = "MNO";
        String targetName2 = "Mno";
        String targetCode3 = "PQR";
        String targetName3 = "Pqr";

        Date date = new Date();
        String source = "ThisSource";

        Currency[] currencies = {
                new Currency(baseCode1, baseName1, "лв"),
                new Currency(baseCode2, baseName2, "$"),
                new Currency(baseCode3, baseName3, "$"),
                new Currency(targetCode1, targetName1, "$"),
                new Currency(targetCode2, targetName2, "$"),
                new Currency(targetCode3, targetName3, "$")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        long now = date.getTime();

        long latestTime1 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 1 day
        long latestTime2 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 14 hours
        long latestTime3 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 47 minutes

        long earliestTime1 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 1 day
        long earliestTime2 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 14 hours
        long earliestTime3 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 47 minutes

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode1, BigDecimalFactory.getBigDecimal(10.123), new Date(now), source),
                new CurrencyConversion(baseCode1, targetCode1, BigDecimalFactory.getBigDecimal(116.92), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(11.231), new Date(latestTime1), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(23.123), new Date(now), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(32.143), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(22.222), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(98.12), new Date(now), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(1.9923), new Date(now), source),
                new CurrencyConversion(baseCode2, targetCode1, BigDecimalFactory.getBigDecimal(111.222), new Date(earliestTime2), source),
                new CurrencyConversion(baseCode2, targetCode2, BigDecimalFactory.getBigDecimal(13.245), new Date(latestTime2), source),
                new CurrencyConversion(baseCode2, targetCode3, BigDecimalFactory.getBigDecimal(9.11), new Date(now), source),
                new CurrencyConversion(baseCode3, targetCode1, BigDecimalFactory.getBigDecimal(27.21), new Date(latestTime3), source),
                new CurrencyConversion(baseCode3, targetCode2, BigDecimalFactory.getBigDecimal(10.901), new Date(earliestTime3), source),
                new CurrencyConversion(baseCode3, targetCode3, BigDecimalFactory.getBigDecimal(100.023), new Date(now), source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionPlusDao().getMaybeCurrencyConversionsByBaseAndTarget(baseCode1, targetCode2)
                .test()
                .assertValue(list -> list.size() == 3
                        && (list.get(0).equals(currencyConversions[2]) || list.get(0).equals(currencyConversions[3]) || list.get(0).equals(currencyConversions[4]))
                        && (list.get(1).equals(currencyConversions[2]) || list.get(1).equals(currencyConversions[3]) || list.get(1).equals(currencyConversions[4]))
                        && (list.get(2).equals(currencyConversions[2]) || list.get(2).equals(currencyConversions[3]) || list.get(2).equals(currencyConversions[4]))
                );
    }

    @Test
    public void shouldRetrieveThreeDistinctElementsByBaseCode() {

        String baseCode1 = "ABC";
        String baseName1 = "Abc";
        String baseCode2 = "DEF";
        String baseName2 = "Def";
        String baseCode3 = "GHI";
        String baseName3 = "Ghi";
        String targetCode1 = "JKL";
        String targetName1 = "Jkl";
        String targetCode2 = "MNO";
        String targetName2 = "Mno";
        String targetCode3 = "PQR";
        String targetName3 = "Pqr";

        Date date = new Date();
        String source = "ThisSource";

        Currency[] currencies = {
                new Currency(baseCode1, baseName1, "лв"),
                new Currency(baseCode2, baseName2, "$"),
                new Currency(baseCode3, baseName3, "$"),
                new Currency(targetCode1, targetName1, "$"),
                new Currency(targetCode2, targetName2, "$"),
                new Currency(targetCode3, targetName3, "$")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        long now = date.getTime();

        long latestTime1 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 1 day
        long latestTime2 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 14 hours
        long latestTime3 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 47 minutes

        long earliestTime1 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 1 day
        long earliestTime2 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 14 hours
        long earliestTime3 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 47 minutes

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode1, BigDecimalFactory.getBigDecimal(10.123), new Date(now), source),
                new CurrencyConversion(baseCode1, targetCode1, BigDecimalFactory.getBigDecimal(116.92), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(11.231), new Date(latestTime1), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(23.123), new Date(now), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(32.143), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(22.222), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(98.12), new Date(now), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(1.9923), new Date(latestTime1), source),
                new CurrencyConversion(baseCode2, targetCode1, BigDecimalFactory.getBigDecimal(111.222), new Date(earliestTime2), source),
                new CurrencyConversion(baseCode2, targetCode2, BigDecimalFactory.getBigDecimal(13.245), new Date(latestTime2), source),
                new CurrencyConversion(baseCode2, targetCode3, BigDecimalFactory.getBigDecimal(9.11), new Date(now), source),
                new CurrencyConversion(baseCode3, targetCode1, BigDecimalFactory.getBigDecimal(27.21), new Date(latestTime3), source),
                new CurrencyConversion(baseCode3, targetCode2, BigDecimalFactory.getBigDecimal(10.901), new Date(earliestTime3), source),
                new CurrencyConversion(baseCode3, targetCode3, BigDecimalFactory.getBigDecimal(100.023), new Date(now), source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionPlusDao().getMaybeLatestDistinctCurrencyConversionsByBase(baseCode1)
                .test()
                .assertValue(list -> list.size() == 3
                        && (list.get(0).equals(currencyConversions[0]) || list.get(0).equals(currencyConversions[2]) || list.get(0).equals(currencyConversions[7]))
                        && (list.get(1).equals(currencyConversions[0]) || list.get(1).equals(currencyConversions[2]) || list.get(1).equals(currencyConversions[7]))
                        && (list.get(2).equals(currencyConversions[0]) || list.get(2).equals(currencyConversions[2]) || list.get(2).equals(currencyConversions[7]))
                );
    }

    @Test
    public void shouldRetrieveAllLatestElements() {

        String baseCode1 = "ABC";
        String baseName1 = "Abc";
        String baseCode2 = "DEF";
        String baseName2 = "Def";
        String baseCode3 = "GHI";
        String baseName3 = "Ghi";
        String targetCode1 = "JKL";
        String targetName1 = "Jkl";
        String targetCode2 = "MNO";
        String targetName2 = "Mno";
        String targetCode3 = "PQR";
        String targetName3 = "Pqr";

        Date date = new Date();
        String source = "ThisSource";

        Currency[] currencies = {
                new Currency(baseCode1, baseName1, "лв"),
                new Currency(baseCode2, baseName2, "$"),
                new Currency(baseCode3, baseName3, "$"),
                new Currency(targetCode1, targetName1, "$"),
                new Currency(targetCode2, targetName2, "$"),
                new Currency(targetCode3, targetName3, "$")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        long now = date.getTime();

        long latestTime1 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 1 day
        long latestTime2 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 14 hours
        long latestTime3 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 47 minutes

        long earliestTime1 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 1 day
        long earliestTime2 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 14 hours
        long earliestTime3 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 47 minutes

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode1, BigDecimalFactory.getBigDecimal(10.123), new Date(latestTime1), source),
                new CurrencyConversion(baseCode1, targetCode1, BigDecimalFactory.getBigDecimal(116.92), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(11.231), new Date(latestTime1), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(23.123), new Date(now), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(32.143), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(22.222), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(98.12), new Date(now), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(1.9923), new Date(latestTime1), source),
                new CurrencyConversion(baseCode2, targetCode1, BigDecimalFactory.getBigDecimal(111.222), new Date(earliestTime2), source),
                new CurrencyConversion(baseCode2, targetCode2, BigDecimalFactory.getBigDecimal(13.245), new Date(latestTime2), source),
                new CurrencyConversion(baseCode2, targetCode3, BigDecimalFactory.getBigDecimal(9.11), new Date(now), source),
                new CurrencyConversion(baseCode3, targetCode1, BigDecimalFactory.getBigDecimal(27.21), new Date(latestTime3), source),
                new CurrencyConversion(baseCode3, targetCode2, BigDecimalFactory.getBigDecimal(10.901), new Date(earliestTime3), source),
                new CurrencyConversion(baseCode3, targetCode3, BigDecimalFactory.getBigDecimal(100.023), new Date(now), source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionPlusDao().getMaybeLatestDistinctCurrencyConversions()
                .test()
                .assertValue(list -> list.size() == 9
                        && (list.contains(currencyConversions[0]))
                        && (list.contains(currencyConversions[2]))
                        && (list.contains(currencyConversions[7]))
                        && (list.contains(currencyConversions[8]))
                        && (list.contains(currencyConversions[9]))
                        && (list.contains(currencyConversions[10]))
                        && (list.contains(currencyConversions[11]))
                        && (list.contains(currencyConversions[12]))
                        && (list.contains(currencyConversions[13]))
                );
    }

    @Test
    public void shouldRetrieveTheLatestElementByBaseCodeAndTargetCode() {

        String baseCode1 = "ABC";
        String baseName1 = "Abc";
        String baseCode2 = "DEF";
        String baseName2 = "Def";
        String baseCode3 = "GHI";
        String baseName3 = "Ghi";
        String targetCode1 = "JKL";
        String targetName1 = "Jkl";
        String targetCode2 = "MNO";
        String targetName2 = "Mno";
        String targetCode3 = "PQR";
        String targetName3 = "Pqr";

        Date date = new Date();
        String source = "ThisSource";

        Currency[] currencies = {
                new Currency(baseCode1, baseName1, "лв"),
                new Currency(baseCode2, baseName2, "$"),
                new Currency(baseCode3, baseName3, "$"),
                new Currency(targetCode1, targetName1, "$"),
                new Currency(targetCode2, targetName2, "$"),
                new Currency(targetCode3, targetName3, "$")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        long now = date.getTime();

        long latestTime1 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 1 day
        long latestTime2 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 14 hours
        long latestTime3 = now + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now + 47 minutes

        long earliestTime1 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 1 day
        long earliestTime2 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 14 hours
        long earliestTime3 = now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);//Now - 47 minutes

        CurrencyConversion[] currencyConversions = {
                new CurrencyConversion(baseCode1, targetCode1, BigDecimalFactory.getBigDecimal(10.123), new Date(latestTime1), source),
                new CurrencyConversion(baseCode1, targetCode1, BigDecimalFactory.getBigDecimal(116.92), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(11.231), new Date(latestTime1), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(23.123), new Date(now), source),
                new CurrencyConversion(baseCode1, targetCode2, BigDecimalFactory.getBigDecimal(32.143), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(22.222), new Date(earliestTime1), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(98.12), new Date(now), source),
                new CurrencyConversion(baseCode1, targetCode3, BigDecimalFactory.getBigDecimal(1.9923), new Date(latestTime1), source),
                new CurrencyConversion(baseCode2, targetCode1, BigDecimalFactory.getBigDecimal(111.222), new Date(earliestTime2), source),
                new CurrencyConversion(baseCode2, targetCode2, BigDecimalFactory.getBigDecimal(13.245), new Date(latestTime2), source),
                new CurrencyConversion(baseCode2, targetCode3, BigDecimalFactory.getBigDecimal(9.11), new Date(now), source),
                new CurrencyConversion(baseCode3, targetCode1, BigDecimalFactory.getBigDecimal(27.21), new Date(latestTime3), source),
                new CurrencyConversion(baseCode3, targetCode2, BigDecimalFactory.getBigDecimal(10.901), new Date(earliestTime3), source),
                new CurrencyConversion(baseCode3, targetCode3, BigDecimalFactory.getBigDecimal(100.023), new Date(now), source)
        };

        this.currenciesDatabase.currencyConversionDao().insertOrUpdateCurrencyConversions(currencyConversions);

        this.currenciesDatabase.currencyConversionPlusDao().getMaybeLatestCurrencyConversionByBaseAndTarget(baseCode1, targetCode2)
                .test()
                .assertValue(cc -> cc.equals(currencyConversions[2])
                );
    }
}