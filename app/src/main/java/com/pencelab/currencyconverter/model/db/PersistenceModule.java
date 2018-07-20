package com.pencelab.currencyconverter.model.db;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.CurrencyConversionDao;
import com.pencelab.currencyconverter.model.db.data.CurrencyDao;
import com.pencelab.currencyconverter.model.db.repository.CurrenciesDatabase;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionDataSource;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionRepository;
import com.pencelab.currencyconverter.model.db.repository.CurrencyDataSource;
import com.pencelab.currencyconverter.model.db.repository.CurrencyRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

@Module
public class PersistenceModule {

    private CurrenciesDatabase currenciesDatabase;

    public PersistenceModule(Application application){
        this.currenciesDatabase = Room.databaseBuilder(application.getApplicationContext(), CurrenciesDatabase.class, "currencies.db").addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                populateDatabase();
            }
        }).build();

        //this.currenciesDatabase = Room.databaseBuilder(application.getApplicationContext(), CurrenciesDatabase.class, "currencies.db").build();
    }

    private void populateDatabase(){

    }

    @Singleton
    @Provides
    public CurrenciesDatabase provideRoomDatabase(Context context) {
        return this.currenciesDatabase;
    }

    @Singleton
    @Provides
    public CurrencyDao provideCurrencyDao(CurrenciesDatabase currenciesDatabase) {
        return currenciesDatabase.currencyDao();
    }

    @Singleton
    @Provides
    public CurrencyRepository provideCurrencyRepository(CurrencyDao currencyDao) {
        return new CurrencyDataSource(currencyDao);
    }

    @Singleton
    @Provides
    public CurrencyConversionDao provideCurrencyConversionDao(CurrenciesDatabase currenciesDatabase) {
        return currenciesDatabase.currencyConversionDao();
    }

    @Singleton
    @Provides
    public CurrencyConversionRepository provideCurrencyConversionRepository(CurrencyConversionDao currencyConversionDao) {
        return new CurrencyConversionDataSource(currencyConversionDao);
    }

}
