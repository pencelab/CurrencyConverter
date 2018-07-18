package com.pencelab.currencyconverter.dependencyinjection;

import android.app.Application;

import com.pencelab.currencyconverter.http.ServicesModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        this.component = DaggerApplicationComponent.builder()
                //.applicationModule(new ApplicationModule(this))
                //.servicesModule(new ServicesModule())
                .build();
    }

    public ApplicationComponent getComponent(){
        return this.component;
    }

}
