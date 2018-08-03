package com.pencelab.currencyconverter.model.db.repository;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.Weather;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface WeatherRepository {

    Flowable<List<Weather>> getFlowableAllWeather();
    Maybe<List<Weather>> getMaybeAllWeather();
    Single<List<Weather>> getSingleAllWeather();

    Flowable<Weather> getFlowableWeatherById(Integer id);
    Maybe<Weather> getMaybeWeatherById(Integer id);
    Single<Weather> getSingleWeatherById(Integer id);

    Flowable<List<Weather>> getFlowableWeatherByState(String state);
    Maybe<List<Weather>> getMaybeWeatherByState(String state);
    Single<List<Weather>> getSingleWeatherByState(String state);

    Flowable<List<Weather>> getFlowableWeatherByLocation(String location);
    Maybe<List<Weather>> getMaybeWeatherByLocation(String location);
    Single<List<Weather>> getSingleWeatherByLocation(String location);

    Flowable<List<Weather>> getFlowableLatestDistinctLocationsWeather();
    Maybe<List<Weather>> getMaybeLatestDistinctLocationsWeather();
    Single<List<Weather>> getSingleLatestDistinctLocationsWeather();

    void insertOrUpdateWeather(@NonNull Weather weather);
    void insertOrUpdateWeather(Weather... weather);

    void deleteAllWeather();
    void deleteWeather(@NonNull Integer id);

}
