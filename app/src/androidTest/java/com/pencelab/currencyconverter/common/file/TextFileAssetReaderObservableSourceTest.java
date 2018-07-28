package com.pencelab.currencyconverter.common.file;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pencelab.currencyconverter.common.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TextFileAssetReaderObservableSourceTest {

    private Context context;
    private CompositeDisposable disposables;

    private String currenciesTextFilename = "currencies.txt";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp(){
        this.context = InstrumentationRegistry.getContext();
        this.disposables = new CompositeDisposable();
    }

    @Test
    public void shouldReadFromAssetCurrenciesTextFile() {

        final int[] counter = {0};

        this.disposables.add(
                Observable.defer(() -> TextFileAssetReaderObservableSource.readFromAssetTextFile(this.currenciesTextFilename, this.context))
                        .subscribe(
                                line -> Utils.log("Line " + (++counter[0]) + ": " + line),
                                Utils::log
                        )
        );

        assertEquals(192, counter[0]);

    }
}