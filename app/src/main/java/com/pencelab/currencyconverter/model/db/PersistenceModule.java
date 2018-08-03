package com.pencelab.currencyconverter.model.db;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.common.Utils;
import com.pencelab.currencyconverter.common.file.FileUtils;
import com.pencelab.currencyconverter.common.file.TextFileAssetReaderObservableSource;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionDao;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionPlusDao;
import com.pencelab.currencyconverter.model.db.data.CurrencyDao;
import com.pencelab.currencyconverter.model.db.repository.CurrenciesDatabase;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionDataSource;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionPlusDataSource;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionPlusRepository;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionRepository;
import com.pencelab.currencyconverter.model.db.repository.CurrencyDataSource;
import com.pencelab.currencyconverter.model.db.repository.CurrencyRepository;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

@Module
public class PersistenceModule {

    private static final String FILENAME_CURRENCIES = "currencies.txt";

    private static final String DATABASE_FILENAME = "currencies.db";

    private CurrenciesDatabase currenciesDatabase;

    public PersistenceModule(Application application){
        this.currenciesDatabase = Room.databaseBuilder(application.getApplicationContext(), CurrenciesDatabase.class, DATABASE_FILENAME).addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                populateDatabase(application.getApplicationContext());
            }
        }).build();

        //Dummy request to force onCreate() for database population on the first run
        this.currenciesDatabase.currencyDao().getMaybeCurrencies()
                .doOnComplete(() -> Utils.log("Dummy request completed!!!"))
                .doOnSuccess(list -> Utils.log("Dummy request succeeded!!!"))
                .subscribeOn(Schedulers.io())
                .subscribe();

        //this.currenciesDatabase = Room.databaseBuilder(application.getApplicationContext(), CurrenciesDatabase.class, "currencies.db").build();
    }

    private void populateDatabase(@NonNull Context context){
        Observable.defer(() -> TextFileAssetReaderObservableSource.readFromAssetTextFile(FILENAME_CURRENCIES, context))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        line -> currenciesDatabase.currencyDao().insertOrUpdateCurrency(FileUtils.createCurrencyFromTextLine(line)),
                        Utils::log,
                        () -> Utils.log("Population Completed!!!")
                );

        /*Observable.empty()
                .compose(TextFileAssetReaderObservableTransformer.readFromAssetTextFile(FILE_NAME_CURRENCIES, context))
                .doOnComplete(() -> Utils.log("Population Completed!!!"))
                //.blockingSubscribe(line -> currenciesDatabase.currencyDao().insertOrUpdateCurrency(FileUtils.createCurrencyFromTextLine(line.toString())));

                .subscribe(line -> currenciesDatabase.currencyDao().insertOrUpdateCurrency(FileUtils.createCurrencyFromTextLine(line.toString())));*/
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

    @Singleton
    @Provides
    public CurrencyConversionPlusDao provideCurrencyConversionPlusDao(CurrenciesDatabase currenciesDatabase) {
        return currenciesDatabase.currencyConversionPlusDao();
    }

    @Singleton
    @Provides
    public CurrencyConversionPlusRepository provideCurrencyConversionPlusRepository(CurrencyConversionPlusDao currencyConversionPlusDao) {
        return new CurrencyConversionPlusDataSource(currencyConversionPlusDao);
    }

}
