package com.pencelab.currencyconverter.dependencyinjection;

import android.app.Application;

import com.pencelab.currencyconverter.http.ServicesModule;
import com.pencelab.currencyconverter.model.db.PersistenceModule;
import com.pencelab.currencyconverter.model.db.PersistencyModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

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

}
