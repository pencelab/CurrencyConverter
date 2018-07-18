package com.pencelab.currencyconverter.model.db.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.pencelab.currencyconverter.model.db.Converters;
import com.pencelab.currencyconverter.model.db.data.Currency;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversion;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionDao;
import com.pencelab.currencyconverter.model.db.data.CurrencyDao;

@Database(entities = {Currency.class, CurrencyConversion.class}, version = CurrenciesDatabase.VERSION)
@TypeConverters({Converters.class})
public abstract class CurrenciesDatabase extends RoomDatabase {

    public static final int VERSION = 1;

    public abstract CurrencyDao currencyDao();
    public abstract CurrencyConversionDao currencyConversionDao();

}
