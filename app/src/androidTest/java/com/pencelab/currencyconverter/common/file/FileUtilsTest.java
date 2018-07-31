package com.pencelab.currencyconverter.common.file;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pencelab.currencyconverter.model.db.data.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class FileUtilsTest {

    @Before
    public void setUp(){

    }

    @After
    public void tearDown(){

    }

    @Test
    public void shouldCreateCurrenciesFromTextLine() {
        String line = "AZN,Azerbaijani Manat,ман.";
        Currency actual = FileUtils.createCurrencyFromTextLine(line);
        Currency expected = new Currency("AZN", "Azerbaijani Manat", "ман.");

        assertEquals(expected, actual);
    }
}