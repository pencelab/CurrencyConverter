package com.pencelab.currencyconverter.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CurrencyLayerRequesterServiceTest {

    @Rule
    public final ServiceTestRule serviceRule = new ServiceTestRule();

    private CurrencyLayerRequesterService currencyLayerRequesterService;
    private Intent serviceIntent;
    private IBinder binder;

    @Before
    public void setUp() throws TimeoutException {
        this.serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), CurrencyLayerRequesterService.class);
        this.binder = this.serviceRule.bindService(this.serviceIntent);
        this.currencyLayerRequesterService = ((CurrencyLayerRequesterService.CurrencyLayerRequesterBinder) binder).getService();
    }

    @After
    public void tearDown() {
        if(this.currencyLayerRequesterService.isStarted())
            this.currencyLayerRequesterService.stopService(this.serviceIntent);
    }

    @Test
    public void onCreate() {
    }

    @Test
    public void onDestroy() {
    }

    @Test
    public void onStartCommand() {
        assertEquals(Service.START_STICKY, this.currencyLayerRequesterService.onStartCommand(this.serviceIntent, 0, 0));
    }

    @Test
    public void shouldAddMillisecondsTimesFrequency(){
        long timestamp = 1514764800;
        int frecuency = 24;
        long timeUnit = TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);
        long expected = timestamp + 24000;
        long nextUpdateTimestamp = this.currencyLayerRequesterService.calculateNextUpdateTimestamp(timestamp, frecuency, timeUnit);
        assertEquals(expected, nextUpdateTimestamp);
    }

    /*@Test
    public void testWithBoundService() throws TimeoutException {
        // Create the service Intent.
        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(),
                        CurrencyLayerRequesterService.class);

        // Bind the service and grab a reference to the binder.
        IBinder binder = this.serviceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call
        // public methods on the binder directly.
        CurrencyLayerRequesterService service =
                ((CurrencyLayerRequesterService.CurrencyLayerRequesterBinder) binder).getService();

        service.startService(serviceIntent);

        // Verify that the service is working correctly.
        assertTrue(service.isStarted());
    }*/

}