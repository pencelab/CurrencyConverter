package com.pencelab.currencyconverter.model.db.data;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Date;

public class CurrencyConversionPlus extends CurrencyConversion {

    public static final String COLUMN_INFO_BASE_CURRENCY_NAME = "base_name";
    public static final String COLUMN_INFO_TARGET_CURRENCY_NAME = "target_name";

    @NonNull
    @ColumnInfo(name = COLUMN_INFO_BASE_CURRENCY_NAME)
    private String baseCurrencyName;

    @NonNull
    @ColumnInfo(name = COLUMN_INFO_TARGET_CURRENCY_NAME)
    private String targetCurrencyName;

    public CurrencyConversionPlus(@NonNull Integer id, @NonNull String baseCode, @NonNull String targetCode, @NonNull BigDecimal value, @NonNull Date date, @NonNull String source, @NonNull String baseCurrencyName, @NonNull String targetCurrencyName) {
        super(id, baseCode, targetCode, value, date, source);
        this.baseCurrencyName = baseCurrencyName;
        this.targetCurrencyName = targetCurrencyName;
    }

    @NonNull
    public String getBaseCurrencyName() {
        return this.baseCurrencyName;
    }

    @NonNull
    public String getTargetCurrencyName() {
        return this.targetCurrencyName;
    }

    @Override
    public String toString() {
        return super.toString() + " | Base Currency Name: " + this.baseCurrencyName + " | Target Currency Name: " + this.targetCurrencyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null) return false;
        if(this.getClass() != o.getClass()) {
            CurrencyConversion me = new CurrencyConversion(this.baseCode, this.targetCode, this.value, this.date, this.source);
            return me.equals(o);
        }else
            return super.equals(o);
    }
}


