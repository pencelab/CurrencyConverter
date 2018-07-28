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

    //TODO this method doesn't work
    @Test
    public void shouldReadFromAssetCurrenciesTextFileAndThrowFileNotFoundException() {
        /*this.disposables.add(
                Observable.empty()
                        .compose(TextFileAssetReaderObservableTransformer.readFromAssetTextFile(this.currenciesTextFilename + "abc", this.context))
                        .test()
                        .assertFailure(FileNotFoundException.class)
                        .assertNotComplete()
        );*/

        /*Observable.<String>error(new RuntimeException("WHOOOSS!!!"))
                .onErrorResumeNext(Observable.<String>error(new RuntimeException("SECOND EXCEPTION!!!")))
                .test()
                .assertFailureAndMessage(RuntimeException.class, "WHOOOSS!!!");*/


        Observable.empty()
                .compose(TextFileAssetReaderObservableTransformer.readFromAssetTextFile(this.currenciesTextFilename + "abc", this.context))
                //.doOnError(error -> Utils.log(error.toString()))
                .onErrorResumeNext(error -> {
                    Utils.log("ERROR!!!! ---> " + error.toString());
                    return Observable.<String>error(new RuntimeException("OOOPSS!!!"));
                }).test()
                .assertFailureAndMessage(RuntimeException.class, "OOOPSS!!!")
                .assertNotComplete();

        /*TestObserver<String> observer = new TestObserver<>();

        Observable.empty()
                .compose(TextFileAssetReaderObservableTransformer.readFromAssetTextFile(this.currenciesTextFilename + "abc", this.context))
                .subscribe(observer);

        //observer.assertError(RuntimeException.class);
        observer.assertFailure(RuntimeException.class);
        observer.assertNotComplete();*/

    }

    //TODO delete this method
    @Test
    public void newTest(){
        List<String> letters = Arrays.asList("A", "B", "C", "D", "E");
        TestObserver<String> subscriber = new TestObserver<>();

        Observable<String> observable = Observable
                .fromIterable(letters)
                .zipWith(Observable.range(1, Integer.MAX_VALUE), ((string, index) -> index + "-" + string))
                .concatWith(Observable.error(new RuntimeException("error in Observable")));

        observable.subscribe(subscriber);

        subscriber.assertError(RuntimeException.class);
        subscriber.assertNotComplete();

        ObservableTransformer<Long, String> t = new ObservableTransformer<Long, String>() {
            @Override
            public ObservableSource<String> apply(Observable<Long> upstream) {
                return null;
            }
        };
    }
}