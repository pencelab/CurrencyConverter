package com.pencelab.currencyconverter.dependencyinjection;

import com.pencelab.currencyconverter.http.ServicesModule;
import com.pencelab.currencyconverter.model.db.PersistenceModule;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionDao;
import com.pencelab.currencyconverter.model.db.data.CurrencyDao;
import com.pencelab.currencyconverter.model.db.repository.CurrenciesDatabase;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionRepository;
import com.pencelab.currencyconverter.model.db.repository.CurrencyRepository;
import com.pencelab.currencyconverter.view.CurrencyMonitorActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ServicesModule.class, PersistenceModule.class})
public interface ApplicationComponent {

    void inject(CurrencyMonitorActivity target);

    CurrenciesDatabase currenciesDatabase();
    CurrencyDao CurrencyDao();
    CurrencyRepository currencyRepository();
    CurrencyConversionDao CurrencyConversionDao();
    CurrencyConversionRepository currencyConversionRepository();

}
