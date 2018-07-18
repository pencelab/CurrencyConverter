package com.pencelab.currencyconverter.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pencelab.currencyconverter.R;
import com.pencelab.currencyconverter.common.Utils;
import com.pencelab.currencyconverter.dependencyinjection.App;
import com.pencelab.currencyconverter.http.CurrencyLayerService;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_monitor);

        ((App) getApplication()).getComponent().inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.disposables = new CompositeDisposable();

        this.observeCurrencyLayerData();
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

    @Override
    protected void onStop() {
        super.onStop();

        if(this.disposables != null) {
            this.disposables.dispose();
            this.disposables = null;
        }
    }
}
