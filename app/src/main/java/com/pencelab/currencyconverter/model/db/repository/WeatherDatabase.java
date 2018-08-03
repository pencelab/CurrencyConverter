package com.pencelab.currencyconverter.model.db.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.pencelab.currencyconverter.model.db.Converters;
import com.pencelab.currencyconverter.model.db.data.Weather;
import com.pencelab.currencyconverter.model.db.data.WeatherDao;

@Database(entities = {Weather.class}, version = WeatherDatabase.VERSION)
@TypeConverters({Converters.class})
public abstract class WeatherDatabase extends RoomDatabase {

    public static final int VERSION = 1;

    public abstract WeatherDao weatherDao();

}

