package com.pencelab.currencyconverter.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.pencelab.currencyconverter.R;
import com.pencelab.currencyconverter.common.BigDecimalFactory;
import com.pencelab.currencyconverter.common.Settings;
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

    private static final String SERVICE_CONTROL_PREFERENCES = "pref_service_control";
    private static final String PREF_SERVICE_LAST_UPDATE = "pref_service_last_update";
    private static final String PREF_SERVICE_NEXT_UPDATE = "pref_service_next_update";

    private CompositeDisposable disposables;

    private static final String SOURCE = "currencylayer.com";
    private static final String ACCESS_KEY = "8602e06a75aa63372e10373e989426b5";

    @Inject
    CurrencyLayerService currencyLayerService;

    @Inject
    CurrencyConversionRepository currencyConversionRepository;

    private long lastUpdateTimestamp = 0;
    private long nextUpdateTimestamp = 0;
    private int updateFrequency = 1;
    private long timeUnitInterval = TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);//TODO Change this to HOURS

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
        this.disposables = new CompositeDisposable();
        try {
            this.updateFrequency = Integer.parseInt(getString(R.string.entry_value_update_frequency_preference_1_day));
        }catch(NumberFormatException nfe){
            Utils.log(nfe);
        }
        this.initTimestamps();
        this.observeServiceUpdateFrequencySharedPreference();
    }

    private void initTimestamps(){
        Utils.log("Initializing TimeStamps");//TODO delete this
        SharedPreferences sp = this.getSharedPreferences(SERVICE_CONTROL_PREFERENCES, Activity.MODE_PRIVATE);
        this.lastUpdateTimestamp = sp.getLong(PREF_SERVICE_LAST_UPDATE, new Date().getTime() - (this.updateFrequency * timeUnitInterval));
        this.nextUpdateTimestamp = this.lastUpdateTimestamp + (this.updateFrequency * timeUnitInterval);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(PREF_SERVICE_NEXT_UPDATE, this.nextUpdateTimestamp);
        editor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(this.disposables != null){
            this.disposables.dispose();
            this.disposables = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        this.isStarted = true;
        this.observeCurrencyLayerService();

        return START_STICKY;
    }

    private void observeServiceUpdateFrequencySharedPreference(){
        Utils.log("Observing Service Update Frequency Shared Preference...");//TODO delete this
        this.disposables.add(
                Settings.get(this).getMonitoredServiceFrequencyPreference()
                        .subscribeOn(Schedulers.single())
                        .subscribe(prefValue -> {
                            Utils.log("Frequency Shared Preference has been changed to --> " + prefValue);//TODO delete this
                            try{
                                this.updateFrequency = Integer.parseInt(prefValue);
                                this.setNextUpdateTimestampSharedPreference();
                            }catch (NumberFormatException nfe) {
                                Utils.log(nfe);
                            }
                        })
        );
    }

    private void observeCurrencyLayerService(){

        this.disposables.add(
                Observable.interval(0, 1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .doOnNext(i -> Utils.log("Second: " + i))//TODO delete this
                        .filter(i -> new Date().getTime() > this.nextUpdateTimestamp)
                        .map(i -> "Updating at second: " + i)//TODO delete this
                        .subscribe(str -> {
                            Utils.log(str);
                            this.setUpdateTimestampsSharedPreferences();
                        })
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

    private void setUpdateTimestampsSharedPreferences(){
        Utils.log("Updating TimeStamps Shared Preferences");//TODO delete this
        this.lastUpdateTimestamp = new Date().getTime();
        this.nextUpdateTimestamp = this.calculateNextUpdateTimestamp();

        SharedPreferences sp = this.getSharedPreferences(SERVICE_CONTROL_PREFERENCES, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(PREF_SERVICE_LAST_UPDATE, this.lastUpdateTimestamp);
        editor.putLong(PREF_SERVICE_NEXT_UPDATE, this.nextUpdateTimestamp);
        editor.commit();
    }

    private void setNextUpdateTimestampSharedPreference(){
        Utils.log("Updating Next Update TimeStamp Shared Preference");//TODO delete this
        this.nextUpdateTimestamp = this.calculateNextUpdateTimestamp();

        SharedPreferences sp = this.getSharedPreferences(SERVICE_CONTROL_PREFERENCES, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(PREF_SERVICE_NEXT_UPDATE, this.nextUpdateTimestamp);
        editor.commit();
    }

    private long calculateNextUpdateTimestamp(){
        return this.lastUpdateTimestamp + this.updateFrequency * this.timeUnitInterval;
    }

}
