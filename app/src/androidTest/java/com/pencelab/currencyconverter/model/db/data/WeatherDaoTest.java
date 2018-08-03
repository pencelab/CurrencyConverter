package com.pencelab.currencyconverter.model.db.data;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.EmptyResultSetException;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pencelab.currencyconverter.model.db.repository.WeatherDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class WeatherDaoTest {

    private WeatherDatabase weatherDatabase;

    //Make sure that Room executes all the database operations instantly
    @Rule public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        this.weatherDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), WeatherDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void tearDown() {
        this.weatherDatabase.close();
    }

    @Test
    public void shouldInsertWeatherAndGetWeatherListWithOnlyThatElement() {

        Weather weather = new Weather("Rainy", "New York", 23.5, new Date());

        // Given that we have a weather in the data source
        this.weatherDatabase.weatherDao().insertOrUpdateWeather(weather);

        // When subscribing to the emissions of weather
        this.weatherDatabase.weatherDao()
                .getFlowableAllWeather()
                .test()
                // assertValue asserts that there was only one emission
                .assertValue(weatherList -> weatherList.size() == 1 && weatherList.contains(weather));
    }

    @Test
    public void shouldInsertWeatherAndGetWeatherListWithOnlyThatElementThenUpdatesIt() {

        String oldState = "Rainy";
        String newState = "Sunny";

        Weather weather = new Weather(oldState, "New York", 23.5, new Date());

        this.weatherDatabase.weatherDao().insertOrUpdateWeather(weather);

        AtomicReference<Weather> w = new AtomicReference<>();

        this.weatherDatabase.weatherDao()
                .getMaybeAllWeather()
                .test()
                .assertValue(weatherList -> {
                    boolean result = false;

                    if(weatherList.size() == 1){
                        w.set(weatherList.get(0));
                        result = weatherList.contains(weather);
                    }

                    return result;
                });

        Weather updatedWeather = new Weather(w.get().getId(), newState, w.get().getLocation(), w.get().getDegrees(), new Date());

        this.weatherDatabase.weatherDao().insertOrUpdateWeather(updatedWeather);

        this.weatherDatabase.weatherDao()
                .getMaybeAllWeather()
                .test()
                .assertValue(weatherList -> weatherList.size() == 1 && weatherList.contains(updatedWeather));
    }

    @Test
    public void shouldThrowAnErrorWhenGettingWeatherWithEmptyResult() {
        // When the query returns no rows, Single will trigger onError(EmptyResultSetException.class)
        this.weatherDatabase.weatherDao()
                .getSingleWeatherById(1)
                .test()
                .assertError(EmptyResultSetException.class);
    }

    @Test
    public void shouldInsertEightWeatherObjectsAndGetTheLatestOneForEveryLocation() {

        long now = new Date().getTime();

        Weather weatherNYEarliest = new Weather("Rainy", "New York", 23.5, new Date(now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))); // Now minus 1 day
        Weather weatherNYMiddle = new Weather("Cloudy", "New York", 21.8, new Date(now - TimeUnit.MILLISECONDS.convert(5, TimeUnit.HOURS))); // Now minus 5 hours
        Weather weatherNYLatest = new Weather("Sunny", "New York", 28.1, new Date(now - TimeUnit.MILLISECONDS.convert(27, TimeUnit.MINUTES))); // Now minus 27 minutes

        Weather weatherSFEarliest = new Weather("Foggy", "San Francisco", 13.1, new Date(now - TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS))); // Now minus 2 days
        Weather weatherSFMiddle = new Weather("Windy", "San Francisco", 15.9, new Date(now - TimeUnit.MILLISECONDS.convert(11, TimeUnit.HOURS))); // Now minus 11 hours
        Weather weatherSFLatest = new Weather("Sunny", "San Francisco", 17.6, new Date(now - TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS))); // Now minus 3 hours

        Weather weatherLVEarliest = new Weather("Foggy", "Las Vegas", 26.2, new Date(now - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))); // Now minus 1 day
        Weather weatherLVLatest = new Weather("Sunny", "Las Vegas", 21.4, new Date(now - TimeUnit.MILLISECONDS.convert(8, TimeUnit.HOURS))); // Now minus 8 hours

        this.weatherDatabase.weatherDao().insertOrUpdateWeather(new Weather[]{weatherNYMiddle, weatherNYLatest, weatherNYEarliest, weatherSFMiddle, weatherSFLatest, weatherSFEarliest, weatherLVLatest, weatherLVEarliest});

        this.weatherDatabase.weatherDao()
                .getMaybeLatestDistinctLocationsWeather()
                .test()
                .assertValue(latestWeatherList -> latestWeatherList.size() == 3
                                               && latestWeatherList.contains(weatherNYLatest)
                                               && latestWeatherList.contains(weatherSFLatest)
                                               && latestWeatherList.contains(weatherLVLatest));
    }
}