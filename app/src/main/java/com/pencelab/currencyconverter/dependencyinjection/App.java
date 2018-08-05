package com.pencelab.currencyconverter.dependencyinjection;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.pencelab.currencyconverter.common.Settings;
import com.pencelab.currencyconverter.model.db.PersistenceModule;
import com.pencelab.currencyconverter.model.db.PersistencyModule;
import com.pencelab.currencyconverter.services.CurrencyLayerRequesterService;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class App extends Application implements LifecycleObserver {

    private ApplicationComponent component;

    private CurrencyLayerRequesterService currencyLayerRequesterService;
    private Intent serviceIntent;
    private boolean isBound = false;
    private boolean keepServiceRunningWhenOnBackgrounded = true;
    private CompositeDisposable disposables;

    @Override
    public void onCreate() {
        super.onCreate();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        this.component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                //.servicesModule(new ServicesModule())
                .persistenceModule(new PersistenceModule(this))
                .persistencyModule(new PersistencyModule(this))
                .build();
    }

    public ApplicationComponent getComponent(){
        return this.component;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        if(this.disposables == null)
            this.disposables = new CompositeDisposable();

        this.observeSharedPreferencesForService();
        this.doBindService();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        if(this.disposables != null) {
            this.disposables.dispose();
            this.disposables.clear();
            this.disposables = null;
        }

        if (this.currencyLayerRequesterService != null && !this.keepServiceRunningWhenOnBackgrounded)
                this.currencyLayerRequesterService.stopService(this.serviceIntent);

        this.doUnbindService();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            currencyLayerRequesterService = ((CurrencyLayerRequesterService.CurrencyLayerRequesterBinder)service).getService();

            if(!currencyLayerRequesterService.isStarted())
                currencyLayerRequesterService.startService(serviceIntent);
        }

        public void onServiceDisconnected(ComponentName className) {
            currencyLayerRequesterService = null;
        }
    };

    private void doBindService() {
        this.serviceIntent = new Intent(this, CurrencyLayerRequesterService.class);
        this.bindService(this.serviceIntent, this.serviceConnection, Context.BIND_AUTO_CREATE);
        this.isBound = true;
    }

    private void doUnbindService() {
        if (this.isBound) {
            this.unbindService(this.serviceConnection);
            this.isBound = false;
        }
    }

    private void observeSharedPreferencesForService(){
        this.disposables.add(
                Settings.get(this).getMonitoredServiceRunPreference()
                        .subscribeOn(Schedulers.single())
                        .subscribe(pref -> this.keepServiceRunningWhenOnBackgrounded = pref.booleanValue())
        );
    }
}
