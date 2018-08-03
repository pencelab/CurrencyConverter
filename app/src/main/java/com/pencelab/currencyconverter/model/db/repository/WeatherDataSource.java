package com.pencelab.currencyconverter.model.db.repository;

import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.Weather;
import com.pencelab.currencyconverter.model.db.data.WeatherDao;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class WeatherDataSource implements WeatherRepository{

    private final WeatherDao weatherDao;

    public WeatherDataSource(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    @Override
    public Flowable<List<Weather>> getFlowableAllWeather() {
        return this.weatherDao.getFlowableAllWeather();
    }

    @Override
    public Maybe<List<Weather>> getMaybeAllWeather() {
        return this.weatherDao.getMaybeAllWeather();
    }

    @Override
    public Single<List<Weather>> getSingleAllWeather() {
        return this.weatherDao.getSingleAllWeather();
    }

    @Override
    public Flowable<Weather> getFlowableWeatherById(Integer id) {
        return this.weatherDao.getFlowableWeatherById(id);
    }

    @Override
    public Maybe<Weather> getMaybeWeatherById(Integer id) {
        return this.weatherDao.getMaybeWeatherById(id);
    }

    @Override
    public Single<Weather> getSingleWeatherById(Integer id) {
        return this.weatherDao.getSingleWeatherById(id);
    }

    @Override
    public Flowable<List<Weather>> getFlowableWeatherByState(String state) {
        return this.weatherDao.getFlowableWeatherByState(state);
    }

    @Override
    public Maybe<List<Weather>> getMaybeWeatherByState(String state) {
        return this.weatherDao.getMaybeWeatherByState(state);
    }

    @Override
    public Single<List<Weather>> getSingleWeatherByState(String state) {
        return this.weatherDao.getSingleWeatherByState(state);
    }

    @Override
    public Flowable<List<Weather>> getFlowableWeatherByLocation(String location) {
        return this.weatherDao.getFlowableWeatherByLocation(location);
    }

    @Override
    public Maybe<List<Weather>> getMaybeWeatherByLocation(String location) {
        return this.weatherDao.getMaybeWeatherByLocation(location);
    }

    @Override
    public Single<List<Weather>> getSingleWeatherByLocation(String location) {
        return this.weatherDao.getSingleWeatherByLocation(location);
    }

    @Override
    public Flowable<List<Weather>> getFlowableLatestDistinctLocationsWeather() {
        return this.weatherDao.getFlowableLatestDistinctLocationsWeather();
    }

    @Override
    public Maybe<List<Weather>> getMaybeLatestDistinctLocationsWeather() {
        return this.weatherDao.getMaybeLatestDistinctLocationsWeather();
    }

    @Override
    public Single<List<Weather>> getSingleLatestDistinctLocationsWeather() {
        return this.weatherDao.getSingleLatestDistinctLocationsWeather();
    }

    @Override
    public void insertOrUpdateWeather(@NonNull Weather weather) {
        this.weatherDao.insertOrUpdateWeather(weather);
    }

    @Override
    public void insertOrUpdateWeather(Weather... weather) {
        this.weatherDao.insertOrUpdateWeather(weather);
    }

    @Override
    public void deleteAllWeather() {
        this.weatherDao.deleteAllWeather();
    }

    @Override
    public void deleteWeather(@NonNull Integer id) {
        this.weatherDao.deleteWeather(id);
    }
}
