package com.pencelab.currencyconverter.model.db.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM Weather")
    Flowable<List<Weather>> getFlowableAllWeather();

    @Query("SELECT * FROM Weather")
    Maybe<List<Weather>> getMaybeAllWeather();

    @Query("SELECT * FROM Weather")
    Single<List<Weather>> getSingleAllWeather();

    @Query("SELECT * FROM Weather WHERE _id = :id")
    Flowable<Weather> getFlowableWeatherById(Integer id);

    @Query("SELECT * FROM Weather WHERE _id = :id")
    Maybe<Weather> getMaybeWeatherById(Integer id);

    @Query("SELECT * FROM Weather WHERE _id = :id")
    Single<Weather> getSingleWeatherById(Integer id);

    @Query("SELECT * FROM Weather WHERE state = :state")
    Flowable<List<Weather>> getFlowableWeatherByState(String state);

    @Query("SELECT * FROM Weather WHERE state = :state")
    Maybe<List<Weather>> getMaybeWeatherByState(String state);

    @Query("SELECT * FROM Weather WHERE state = :state")
    Single<List<Weather>> getSingleWeatherByState(String state);

    @Query("SELECT * FROM Weather WHERE location = :location")
    Flowable<List<Weather>> getFlowableWeatherByLocation(String location);

    @Query("SELECT * FROM Weather WHERE location = :location")
    Maybe<List<Weather>> getMaybeWeatherByLocation(String location);

    @Query("SELECT * FROM Weather WHERE location = :location")
    Single<List<Weather>> getSingleWeatherByLocation(String location);

    @Query("SELECT *, MAX(datetime) FROM Weather GROUP BY location")
    Flowable<List<Weather>> getFlowableLatestDistinctLocationsWeather();

    @Query("SELECT *, MAX(datetime) FROM Weather GROUP BY location")
    Maybe<List<Weather>> getMaybeLatestDistinctLocationsWeather();

    @Query("SELECT *, MAX(datetime) FROM Weather GROUP BY location")
    Single<List<Weather>> getSingleLatestDistinctLocationsWeather();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateWeather(@NonNull Weather weather);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateWeather(Weather... weather);

    @Query("DELETE FROM Weather")
    void deleteAllWeather();

    @Query("DELETE FROM Weather WHERE _id = :id")
    void deleteWeather(Integer id);

}
