package com.pencelab.currencyconverter.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.pencelab.currencyconverter.R;
import com.pencelab.currencyconverter.common.Utils;
import com.pencelab.currencyconverter.dependencyinjection.App;
import com.pencelab.currencyconverter.http.CurrencyLayerService;
import com.pencelab.currencyconverter.common.file.TextFileAssetReaderObservableSource;
import com.pencelab.currencyconverter.common.file.TextFileAssetReaderObservableTransformer;
import com.pencelab.currencyconverter.model.db.data.Currency;
import com.pencelab.currencyconverter.model.db.repository.CurrenciesDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CurrencyMonitorActivity extends AppCompatActivity {

    @Inject
    CurrencyLayerService currencyLayerService;

    private String accessKey = "8602e06a75aa63372e10373e989426b5";

    private CompositeDisposable disposables;

    @Inject
    CurrenciesDatabase currenciesDatabase;

    private Button empty;
    private Button notEmpty;

    private Button howMany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_monitor);

        ((App) getApplication()).getComponent().inject(this);

        this.empty = findViewById(R.id.empty);
        this.notEmpty = findViewById(R.id.not_empty);

        this.howMany = findViewById(R.id.howmany);

        this.empty.setOnClickListener(v -> this.testCode(true, "currencies.txt"));
        this.notEmpty.setOnClickListener(v -> this.testCode(false, "currencies2.txt"));

        this.howMany.setOnClickListener(v -> this.howMany());
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.disposables = new CompositeDisposable();

        //this.observeCurrencyLayerData();
    }

    public void observeCurrencyLayerData(){

        this.disposables.add(
            Observable.interval(0, 1, TimeUnit.HOURS)
                    .subscribeOn(Schedulers.io())
                    .flatMapSingle(i -> this.currencyLayerService.getCurrencyLayerDataSingle(this.accessKey))
                    .doOnError(error -> Utils.log(error))
                    .onExceptionResumeNext(e -> Utils.log("OOPS!!! Exception: " + e))
                    .subscribe(currencyLayerData -> {
                        Utils.log(currencyLayerData.toString());
                    })
        );
    }

    private void howMany(){
        this.disposables.add(
                this.currenciesDatabase.currencyDao().getMaybeCurrencies()
                        .doOnComplete(() -> Utils.log("Dummy request completed!!!"))
                        //.doOnSuccess(list -> Utils.log("Dummy request succeeded!!!"))
                        .doOnError(error -> Utils.log(error))
                        .subscribeOn(Schedulers.io())
                        .subscribe(list -> Utils.log("LIST SIZE --> " + list.size()),
                                Utils::log)
        );
    }

    public void testCode(boolean isEmpty, String filename){

        final int[] counter = {0};


        this.disposables.add(
                Observable.defer(() -> TextFileAssetReaderObservableSource.readFromAssetTextFile(filename, this))
                        .subscribeOn(Schedulers.io())
                        .doOnComplete(() -> Utils.log("READING FILE IS COMPLETED!!!"))
                        .subscribe(
                            line -> Utils.log("Line " + (++counter[0]) + ": " + line),
                            Utils::log,
                            () -> Utils.log("READING FILE FINISHED!!!")
                        )
        );


        if(isEmpty) {
            this.disposables.add(
                    Observable.empty()
                            .compose(TextFileAssetReaderObservableTransformer.readFromAssetTextFile("currencies.txt", this))
                            //.doOnComplete(() -> Utils.log("Operation completed!"))
                            //.onExceptionResumeNext(e -> Utils.log("Operation not completed!"))
                            .subscribe(
                                    line -> Utils.log("Line " + (++counter[0]) + ": " + line.toString()),
                                    e -> Utils.log("Operation not completed!")/*,
                                    () -> Utils.log("READING FILE FINISHED!!!")*/
                            )
            );
        }else{
            this.disposables.add(
                    Observable.just("ABC")
                            .compose(TextFileAssetReaderObservableTransformer.readFromAssetTextFile("currencies.txt", this))
                            //.doOnComplete(() -> Utils.log("Operation completed!"))
                            //.onExceptionResumeNext(e -> Utils.log("Operation not completed!"))
                            .subscribe(
                                line -> Utils.log("Line " + (++counter[0]) + ": " + line.toString()),
                                e -> Utils.log("Operation not completed!")/*,
                                () -> Utils.log("READING FILE FINISHED!!!")*/
                            )
            );
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(this.disposables != null) {
            this.disposables.dispose();
            this.disposables = null;
        }
    }
}
