package com.pencelab.currencyconverter.model.db;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.WeatherDao;
import com.pencelab.currencyconverter.model.db.repository.WeatherDataSource;
import com.pencelab.currencyconverter.model.db.repository.WeatherDatabase;
import com.pencelab.currencyconverter.model.db.repository.WeatherRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PersistencyModule {

    private static final String DATABASE_FILENAME = "weather.db";

    private WeatherDatabase weatherDatabase;

    public PersistencyModule(Application application){
        this.weatherDatabase = Room.databaseBuilder(application.getApplicationContext(), WeatherDatabase.class, DATABASE_FILENAME).addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                populateDatabase();
            }
        }).build();

        this.weatherDatabase = Room.databaseBuilder(application.getApplicationContext(), WeatherDatabase.class, DATABASE_FILENAME).build();
    }

    private void populateDatabase(){
        //Populate your database here. This method will execute the first time that your database is created.
    }

    @Singleton
    @Provides
    public WeatherDatabase provideRoomDatabase(Context context) {
        return this.weatherDatabase;
    }

    @Singleton
    @Provides
    public WeatherDao provideWeatherDao(WeatherDatabase weatherDatabase) {
        return weatherDatabase.weatherDao();
    }

    @Singleton
    @Provides
    public WeatherRepository provideWeatherRepository(WeatherDao weatherDao) {
        return new WeatherDataSource(weatherDao);
    }

}
