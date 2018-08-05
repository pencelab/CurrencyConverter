package com.pencelab.currencyconverter.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.pencelab.currencyconverter.common.BigDecimalFactory;
import com.pencelab.currencyconverter.common.Utils;
import com.pencelab.currencyconverter.http.CurrencyLayerService;
import com.pencelab.currencyconverter.http.currencylayer.CurrencyLayerCurrencyValue;
import com.pencelab.currencyconverter.http.currencylayer.CurrencyLayerCurrencyValueFactory;
import com.pencelab.currencyconverter.http.currencylayer.model.CurrencyLayerData;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversion;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CurrencyLayerRequesterService extends Service {

    private CompositeDisposable disposable;

    private final String SOURCE = "currencylayer.com";
    private final String ACCESS_KEY = "8602e06a75aa63372e10373e989426b5";

    @Inject
    CurrencyLayerService currencyLayerService;

    @Inject
    CurrencyConversionRepository currencyConversionRepository;

    private boolean isStarted = false;

    private final IBinder binder = new CurrencyLayerRequesterBinder();

    public class CurrencyLayerRequesterBinder extends Binder {
        public CurrencyLayerRequesterService getService() {
            return CurrencyLayerRequesterService.this;
        }
    }

    public CurrencyLayerRequesterService() {
    }

    public boolean isStarted(){
        return this.isStarted;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.disposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(this.disposable != null){
            this.disposable.dispose();
            this.disposable = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        this.isStarted = true;
        this.observeCurrencyLayerService();

        return START_STICKY;
    }

    private void observeCurrencyLayerService(){

        this.disposable.add(
                Observable.interval(0, 5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .map(i -> "Service -> Second: " + i * 5)//TODO delete this
                        .subscribe(str -> Utils.log(str))
        );

        /*this.disposable.add(
                Observable.interval(0, 1, TimeUnit.DAYS)
                        .subscribeOn(Schedulers.newThread())
                        .flatMapSingle(i -> this.currencyLayerService.getCurrencyLayerDataSingle(ACCESS_KEY))
                        .subscribe(cld -> this.processCurrencyLayerData(cld))
        );*/
    }

    private void processCurrencyLayerData(CurrencyLayerData currencyLayerData){

        String baseCode = currencyLayerData.getSource();
        Date date = new Date(currencyLayerData.getTimestamp());

        List<CurrencyLayerCurrencyValue> list = CurrencyLayerCurrencyValueFactory.getListFromQuotes(currencyLayerData.getQuotes());

        CurrencyConversion[] currencyConversions = new CurrencyConversion[list.size()];

        int i = 0;
        for(CurrencyLayerCurrencyValue clcv : list)
            currencyConversions[i++] = new CurrencyConversion(baseCode, clcv.getCurrency(), BigDecimalFactory.getBigDecimal(clcv.getValue()), date, SOURCE);

        this.currencyConversionRepository.insertOrUpdateCurrencyConversions(currencyConversions);

        list.clear();

        /*this.disposable.add(
            Observable.fromIterable(list)
                    .subscribeOn(Schedulers.io())
                    .map(clcv -> new CurrencyConversion(baseCode, clcv.getCurrency(), BigDecimalFactory.getBigDecimal(clcv.getValue()), date, SOURCE))
                    .subscribe(cc -> this.currencyConversionRepository.insertOrUpdateCurrencyConversion(cc))
        );*/
    }
}
