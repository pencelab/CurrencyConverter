package com.pencelab.currencyconverter.common.file;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pencelab.currencyconverter.common.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TextFileAssetReaderObservableTransformerTest {

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

    @After
    public void tearDown(){
        this.disposables.dispose();
        this.disposables = null;
    }

    @Test
    public void shouldReadFromAssetCurrenciesTextFile() {

        List<String> lines = new ArrayList<>();

        this.disposables.add(
            Observable.zip(
                    Observable.empty()
                            .compose(TextFileAssetReaderObservableTransformer.readFromAssetTextFile(this.currenciesTextFilename, this.context)),
                    Observable.range(1, Integer.MAX_VALUE),
                    (line, index) -> index + " - " + line
            ).subscribe(line -> lines.add(line.toString()))
        );

        assertEquals(192, lines.size());
    }

    @Test
    public void shouldReadFromAssetCurrenciesTextFileAndThrowRuntimeException() {
        this.disposables.add(
                Observable.just("A", "B", "C")
                        .compose(TextFileAssetReaderObservableTransformer.readFromAssetTextFile(this.currenciesTextFilename, this.context))
                        .test()
                        .assertFailureAndMessage(RuntimeException.class, "Upstream is not empty!")
        );
    }
}