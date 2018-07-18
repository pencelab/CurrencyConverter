package com.pencelab.currencyconverter.model.db;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;
import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static BigDecimal currencyValueFromLongValue(long value){
        return new BigDecimal(value).scaleByPowerOfTen(-4);
    }

    @TypeConverter
    public static long currencyValueToLongValue(BigDecimal value){
        return value.scaleByPowerOfTen(4).longValue();
    }

}
