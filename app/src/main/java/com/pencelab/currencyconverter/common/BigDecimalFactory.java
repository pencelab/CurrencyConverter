package com.pencelab.currencyconverter.common;

import java.math.BigDecimal;

public class BigDecimalFactory {

    private static final int PRECISION = 4;

    public static BigDecimal getBigDecimal(double value){
        return getBigDecimal(new BigDecimal(value).scaleByPowerOfTen(4).longValue());
    }

    public static BigDecimal getBigDecimal(long value){
        return new BigDecimal(value).scaleByPowerOfTen(-1 * PRECISION);
    }

    public static long bigDecimalToLong(BigDecimal value){
        return value.scaleByPowerOfTen(PRECISION).longValue();
    }

}
