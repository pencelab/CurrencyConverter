package com.pencelab.currencyconverter.model.db.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CurrencyConversionPlusDao {

    @Query("SELECT cc._id, " +
            "cc.base_code, " +
            "bc.name AS " + CurrencyConversionPlus.COLUMN_INFO_BASE_CURRENCY_NAME + ", " +
            "cc.target_code, " +
            "tc.name AS " + CurrencyConversionPlus.COLUMN_INFO_TARGET_CURRENCY_NAME + ", " +
            "cc.value, cc.date, cc.source " +
            "FROM CurrencyConversion cc " +
            "INNER JOIN Currency bc ON cc.base_code = bc.code " +
            "INNER JOIN Currency tc ON cc.target_code = tc.code")
    Flowable<List<CurrencyConversionPlus>> getFlowableCurrencyConversions();

}
