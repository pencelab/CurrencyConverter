package com.pencelab.currencyconverter.dependencyinjection;

import com.pencelab.currencyconverter.http.ServicesModule;
import com.pencelab.currencyconverter.view.CurrencyMonitorActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ServicesModule.class})
public interface ApplicationComponent {

    void inject(CurrencyMonitorActivity target);

}
