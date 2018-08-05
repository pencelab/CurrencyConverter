package com.pencelab.currencyconverter.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class Settings {

    private static Settings INSTANCE;

    private Subject<Boolean> serviceSubject = BehaviorSubject.create();
    private Preference<Boolean> prefService;

    private Settings(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create(preferences);

        this.prefService = rxSharedPreferences.getBoolean("pref_service_run", true);

        this.prefService.asObservable()
                .subscribeOn(Schedulers.single())
                .subscribe(this.serviceSubject);

    }

    public synchronized static Settings get(Context context){
        if(INSTANCE == null)
            INSTANCE = new Settings(context);

        return INSTANCE;
    }

    public Observable<Boolean> getMonitoredServiceRunPreference(){
        return this.serviceSubject;
    }

}
