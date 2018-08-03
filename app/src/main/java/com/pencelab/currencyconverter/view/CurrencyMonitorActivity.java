package com.pencelab.currencyconverter.view;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pencelab.currencyconverter.R;
import com.pencelab.currencyconverter.common.Utils;
import com.pencelab.currencyconverter.dependencyinjection.App;
import com.pencelab.currencyconverter.model.db.data.Currency;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionPlus;
import com.pencelab.currencyconverter.viewmodel.CurrencyConversionViewModel;
import com.pencelab.currencyconverter.viewmodel.CurrencyConversionViewModelFactory;
import com.pencelab.currencyconverter.viewmodel.CurrencyViewModel;
import com.pencelab.currencyconverter.viewmodel.CurrencyViewModelFactory;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CurrencyMonitorActivity extends AppCompatActivity {

    private String accessKey = "8602e06a75aa63372e10373e989426b5";

    private CompositeDisposable disposables;

    @Inject
    CurrencyConversionViewModelFactory currencyConversionViewModelFactory;

    @Inject
    CurrencyViewModelFactory currencyViewModelFactory;

    private CurrencyConversionViewModel currencyConversionViewModel;
    private CurrencyViewModel currencyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_monitor);

        ((App) getApplication()).getComponent().inject(this);

        this.currencyConversionViewModel = ViewModelProviders.of(this, currencyConversionViewModelFactory).get(CurrencyConversionViewModel.class);
        this.currencyViewModel = ViewModelProviders.of(this, currencyViewModelFactory).get(CurrencyViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.disposables = new CompositeDisposable();

        this.disposables.add(
            this.currencyConversionViewModel.getLatestConversions()
                    .subscribeOn(Schedulers.io())
                    .subscribe(list -> {
                        if(list.isEmpty())
                            Utils.log("LIST IS EMPTY");

                        for(CurrencyConversionPlus ccp : list)
                            Utils.log("ITEM: " + ccp);
                    })
        );

        this.disposables.add(
                this.currencyViewModel.getCurrencies()
                        .subscribeOn(Schedulers.io())
                        .subscribe(list -> {
                            if(list.isEmpty())
                                Utils.log("LIST IS EMPTY");

                            for(Currency c : list)
                                Utils.log("Currency: " + c);
                        })
        );

        //this.observeCurrencyLayerData();
    }

    /*public void observeCurrencyLayerData(){

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
    }*/

    @Override
    protected void onStop() {
        super.onStop();

        if(this.disposables != null) {
            this.disposables.dispose();
            this.disposables = null;
        }
    }
}
