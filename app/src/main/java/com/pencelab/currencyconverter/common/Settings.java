package com.pencelab.currencyconverter.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.pencelab.currencyconverter.R;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class Settings {

    private static Settings INSTANCE;

    @NonNull
    private Context context;

    private Subject<Boolean> serviceRunSubject = BehaviorSubject.create();
    private Preference<Boolean> prefServiceRun;

    private Subject<String> serviceFrequencySubject = BehaviorSubject.create();
    private Preference<String> prefServiceFrequency;

    private Settings(@NonNull Context context){
        this.context = context;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create(preferences);

        this.prefServiceRun = rxSharedPreferences.getBoolean("pref_service_run", true);
        this.prefServiceFrequency = rxSharedPreferences.getString("pref_service_frequency", this.context.getString(R.string.entry_value_update_frequency_preference_1_day));

        this.prefServiceRun.asObservable()
                .subscribeOn(Schedulers.single())
                .subscribe(this.serviceRunSubject);

        this.prefServiceFrequency.asObservable()
                .subscribeOn(Schedulers.single())
                .subscribe(this.serviceFrequencySubject);

    }

    public synchronized static Settings get(@NonNull Context context){
        if(INSTANCE == null)
            INSTANCE = new Settings(context);

        return INSTANCE;
    }

    public Observable<Boolean> getMonitoredServiceRunPreference(){
        return this.serviceRunSubject;
    }

    public Observable<String> getMonitoredServiceFrequencyPreference(){
        return this.serviceFrequencySubject;
    }

}
