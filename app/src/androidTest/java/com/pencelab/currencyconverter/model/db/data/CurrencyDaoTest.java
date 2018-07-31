package com.pencelab.currencyconverter.model.db.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pencelab.currencyconverter.common.Utils;
import com.pencelab.currencyconverter.common.file.FileUtils;
import com.pencelab.currencyconverter.common.file.TextFileAssetReaderObservableSource;
import com.pencelab.currencyconverter.common.file.TextFileAssetReaderObservableTransformer;
import com.pencelab.currencyconverter.model.db.repository.CurrenciesDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CurrencyDaoTest {

    private CurrenciesDatabase currenciesDatabase;

    private CompositeDisposable disposables;

    private String currenciesTextFilename = "currencies.txt";

    private Context context;

    @Mock
    Currency currency;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp(){
        this.context = InstrumentationRegistry.getContext();
        this.disposables = new CompositeDisposable();
        this.currenciesDatabase = Room.inMemoryDatabaseBuilder(this.context, CurrenciesDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void tearDown() {
        this.currenciesDatabase.close();
        this.disposables.dispose();
    }

    @Test
    public void shouldContainDataLoadedFromFileWithObservableSource() {
        this.disposables.add(
                Observable.defer(() -> TextFileAssetReaderObservableSource.readFromAssetTextFile(this.currenciesTextFilename, this.context))
                        .subscribe(
                                line -> currenciesDatabase.currencyDao().insertOrUpdateCurrency(FileUtils.createCurrencyFromTextLine(line)),
                                Utils::log
                        )
        );

        this.disposables.add(
                this.currenciesDatabase.currencyDao().getMaybeCurrencies()
                        .test()
                        .assertValue(list -> list.size() == 192)
        );
    }

    @Test
    public void shouldContainDataLoadedFromFileWithObservableTransformer() {
        Observable.empty()
                .compose(TextFileAssetReaderObservableTransformer.readFromAssetTextFile(this.currenciesTextFilename, this.context))
                .blockingSubscribe(line -> currenciesDatabase.currencyDao().insertOrUpdateCurrency(FileUtils.createCurrencyFromTextLine(line.toString())));

        this.disposables.add(
                this.currenciesDatabase.currencyDao().getMaybeCurrencies()
                        .test()
                        .assertValue(list -> list.size() == 192)
        );
    }

    @Test
    public void shouldInsertOneElement() {
        when(this.currency.getCode()).thenReturn("ABC");
        when(this.currency.getName()).thenReturn("Abc");
        when(this.currency.getSymbol()).thenReturn("A");
        this.currenciesDatabase.currencyDao().insertOrUpdateCurrency(this.currency);

        this.currenciesDatabase.currencyDao().getMaybeCurrencies()
                .test()
                .assertValue(list -> list.size() == 1);
    }

    @Test
    public void shouldInsertThreeElements() {

        Currency[] currencies = {
                new Currency("ABC", "Abc", "лв"),
                new Currency("DEF", "Def", "$b"),
                new Currency("GHI", "Ghi", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        this.currenciesDatabase.currencyDao().getMaybeCurrencies()
                .test()
                .assertValue(list -> list.size() == currencies.length);
    }

    @Test
    public void shouldRetrieveNoElements() {

        String codeNotPresent = "ZZZ";

        Currency[] currencies = {
                new Currency("DEF", "Def", "$"),
                new Currency("IJK", "Ijk", "$"),
                new Currency("RST", "Rst", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        this.currenciesDatabase.currencyDao().getMaybeCurrencyByCode(codeNotPresent)
                .test()
                .assertNoValues();
    }

    @Test
    public void shouldRetrieveThreeElementsByName() {

        String name = "ThisName";

        Currency[] currencies = {
                new Currency("ABC", name, "лв"),
                new Currency("DEF", "Def", "$"),
                new Currency("FGH", name, "$"),
                new Currency("IJK", "Ijk", "$"),
                new Currency("LMN", name, "$b"),
                new Currency("OPQ", "Opq", "$"),
                new Currency("RST", "Rst", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        this.currenciesDatabase.currencyDao().getMaybeCurrencyByName(name)
                .test()
                .assertValue(list -> list.size() == 3);
    }

    @Test
    public void shouldUpdateSecondElement() {

        String code = "DEF";
        String name = "Def";
        String newName = "MyNewName";

        Currency[] currencies = {
                new Currency("ABC", "Abc", "лв"),
                new Currency(code, name, "$b"),
                new Currency("GHI", "Ghi", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        AtomicReference<Currency> currency = new AtomicReference<>();

        this.currenciesDatabase.currencyDao().getMaybeCurrencyByCode(code)
                .test()
                .assertValue(curr -> {
                    currency.set(curr);
                    return curr.getName().equals(name);
                });

        assertEquals(name, currency.get().getName());

        Currency editedCurrency = new Currency(currency.get().getCode(), newName, currency.get().getSymbol());

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(editedCurrency);

        this.currenciesDatabase.currencyDao().getMaybeCurrencyByCode(code)
                .test()
                .assertValue(curr -> curr.getName().equals(newName));
    }

    @Test
    public void shouldDeleteAllElements() {
        Currency[] currencies = {
                new Currency("ABC", "Abc", "лв"),
                new Currency("DEF", "Def", "$b"),
                new Currency("GHI", "Ghi", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        this.currenciesDatabase.currencyDao().getMaybeCurrencies()
                .test()
                .assertValue(list -> list.size() == currencies.length);

        this.currenciesDatabase.currencyDao().deleteAllCurrencies();

        this.currenciesDatabase.currencyDao().getMaybeCurrencies()
                .test()
                .assertValue(list -> list.size() == 0);
    }

    @Test
    public void shouldDeleteSecondElement() {

        Currency currency = new Currency("DEF", "Def", "$b");

        Currency[] currencies = {
                new Currency("ABC", "Abc", "лв"),
                currency,
                new Currency("GHI", "Ghi", "؋")
        };

        this.currenciesDatabase.currencyDao().insertOrUpdateCurrencies(currencies);

        this.currenciesDatabase.currencyDao().getMaybeCurrencies()
                .test()
                .assertValue(list -> list.contains(currency));

        this.currenciesDatabase.currencyDao().deleteCurrency(currency.getCode());

        this.currenciesDatabase.currencyDao().getMaybeCurrencies()
                .test()
                .assertValue(list -> !list.contains(currency));
    }
}