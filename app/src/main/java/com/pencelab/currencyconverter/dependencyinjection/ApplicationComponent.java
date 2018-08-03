package com.pencelab.currencyconverter.dependencyinjection;

import com.pencelab.currencyconverter.http.ServicesModule;
import com.pencelab.currencyconverter.model.db.PersistenceModule;
import com.pencelab.currencyconverter.model.db.PersistencyModule;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionDao;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionPlusDao;
import com.pencelab.currencyconverter.model.db.data.CurrencyDao;
import com.pencelab.currencyconverter.model.db.data.WeatherDao;
import com.pencelab.currencyconverter.model.db.repository.CurrenciesDatabase;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionPlusRepository;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionRepository;
import com.pencelab.currencyconverter.model.db.repository.CurrencyRepository;
import com.pencelab.currencyconverter.model.db.repository.WeatherDatabase;
import com.pencelab.currencyconverter.model.db.repository.WeatherRepository;
import com.pencelab.currencyconverter.view.CurrencyMonitorActivity;
import com.pencelab.currencyconverter.viewmodel.CurrencyConversionViewModelFactory;
import com.pencelab.currencyconverter.viewmodel.CurrencyViewModelFactory;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ServicesModule.class, PersistenceModule.class, PersistencyModule.class})
public interface ApplicationComponent {

    void inject(CurrencyMonitorActivity target);

    CurrenciesDatabase currenciesDatabase();
    CurrencyDao currencyDao();
    CurrencyRepository currencyRepository();
    CurrencyConversionDao currencyConversionDao();
    CurrencyConversionRepository currencyConversionRepository();
    CurrencyConversionPlusDao currencyConversionPlusDao();
    CurrencyConversionPlusRepository currencyConversionPlusRepository();

    WeatherDatabase weatherDatabase();
    WeatherDao weatherDao();
    WeatherRepository weatherRepository();

}
