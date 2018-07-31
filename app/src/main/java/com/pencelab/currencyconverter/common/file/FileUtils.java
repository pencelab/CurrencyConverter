package com.pencelab.currencyconverter.common.file;

import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.Currency;

public class FileUtils {

    public static Currency createCurrencyFromTextLine(@NonNull final String line){
        String[] data = line.split(",");
        return new Currency(data[0], data[1], data[2]);
    }

}
