package com.pencelab.currencyconverter.model.db.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface CurrencyConversionPlusDao {

    /**
     * Gets all the CurrencyConversions from the table CurrencyConversion joined with the currency names from the table Currency.
     * @return all the CurrencyConversions joined with the currency names in a list.
     */
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

    /**
     * Gets the CurrencyConversions from the table CurrencyConversion joined with the currency names from the table Currency.
     * @param baseCode: the base currency code.
     * @param targetCode: the target currency code.
     * @return the CurrencyConversions joined with the currency names in a list that matches
     * with the base code and target code in a list ordered by date starting from the latest inserted or updated.
     */
    @Query("SELECT cc._id, " +
            "cc.base_code, " +
            "bc.name AS " + CurrencyConversionPlus.COLUMN_INFO_BASE_CURRENCY_NAME + ", " +
            "cc.target_code, " +
            "tc.name AS " + CurrencyConversionPlus.COLUMN_INFO_TARGET_CURRENCY_NAME + ", " +
            "cc.value, cc.date, cc.source " +
            "FROM CurrencyConversion cc " +
            "INNER JOIN Currency bc ON cc.base_code = :baseCode AND cc.target_code = :targetCode AND cc.base_code = bc.code " +
            "INNER JOIN Currency tc ON cc.target_code = tc.code ORDER BY cc.date DESC")
    Flowable<List<CurrencyConversionPlus>> getFlowableCurrencyConversionsByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);

    /**
     * Gets the latest CurrencyConversion from the table CurrencyConversion joined with the currency names from the table Currency.
     * @param baseCode: the base currency code.
     * @param targetCode: the target currency code.
     * @return the latest CurrencyConversions joined with the currency names in a list that matches
     * with the base code and target code in a list ordered by date starting from the latest inserted or updated.
     */
    @Query("SELECT cc._id, " +
            "cc.base_code, " +
            "bc.name AS " + CurrencyConversionPlus.COLUMN_INFO_BASE_CURRENCY_NAME + ", " +
            "cc.target_code, " +
            "tc.name AS " + CurrencyConversionPlus.COLUMN_INFO_TARGET_CURRENCY_NAME + ", " +
            "cc.value, cc.date, cc.source " +
            "FROM CurrencyConversion cc " +
            "INNER JOIN Currency bc ON cc.base_code = :baseCode AND cc.target_code = :targetCode AND cc.base_code = bc.code " +
            "INNER JOIN Currency tc ON cc.target_code = tc.code ORDER BY cc.date DESC LIMIT 1")
    Flowable<CurrencyConversionPlus> getFlowableLatestCurrencyConversionByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);

    /**
     * Gets the latest distinct CurrencyConversions from the table CurrencyConversion joined with the currency names from the table Currency.
     * @return the latest distinct CurrencyConversions joined with the currency names in a list.
     */
    @Query("SELECT MAX(cc.date)," +
            "cc._id, " +
            "cc.base_code, " +
            "bc.name AS " + CurrencyConversionPlus.COLUMN_INFO_BASE_CURRENCY_NAME + ", " +
            "cc.target_code, " +
            "tc.name AS " + CurrencyConversionPlus.COLUMN_INFO_TARGET_CURRENCY_NAME + ", " +
            "cc.value, cc.date, cc.source " +
            "FROM CurrencyConversion cc " +
            "INNER JOIN Currency bc ON cc.base_code = bc.code " +
            "INNER JOIN Currency tc ON cc.target_code = tc.code " +
            "GROUP BY cc.base_code, cc.target_code ")
    Flowable<List<CurrencyConversionPlus>> getFlowableLatestDistinctCurrencyConversions();

    /**
     * Gets the latest distinct CurrencyConversions from the table CurrencyConversion joined with the currency names from the table Currency.
     * @param baseCode: the base currency code.
     * @return the latest distinct CurrencyConversions joined with the currency names in a list.
     */
    @Query("SELECT MAX(cc.date)," +
            "cc._id, " +
            "cc.base_code, " +
            "bc.name AS " + CurrencyConversionPlus.COLUMN_INFO_BASE_CURRENCY_NAME + ", " +
            "cc.target_code, " +
            "tc.name AS " + CurrencyConversionPlus.COLUMN_INFO_TARGET_CURRENCY_NAME + ", " +
            "cc.value, cc.date, cc.source " +
            "FROM CurrencyConversion cc " +
            "INNER JOIN Currency bc ON cc.base_code = :baseCode AND cc.base_code = bc.code " +
            "INNER JOIN Currency tc ON cc.target_code = tc.code " +
            "GROUP BY cc.target_code")
    Flowable<List<CurrencyConversionPlus>> getFlowableLatestDistinctCurrencyConversionsByBase(@NonNull String baseCode);

    /**
     * Gets all the CurrencyConversions from the table CurrencyConversion joined with the currency names from the table Currency.
     * @return all the CurrencyConversions joined with the currency names in a list.
     */
    @Query("SELECT cc._id, " +
            "cc.base_code, " +
            "bc.name AS " + CurrencyConversionPlus.COLUMN_INFO_BASE_CURRENCY_NAME + ", " +
            "cc.target_code, " +
            "tc.name AS " + CurrencyConversionPlus.COLUMN_INFO_TARGET_CURRENCY_NAME + ", " +
            "cc.value, cc.date, cc.source " +
            "FROM CurrencyConversion cc " +
            "INNER JOIN Currency bc ON cc.base_code = bc.code " +
            "INNER JOIN Currency tc ON cc.target_code = tc.code")
    Maybe<List<CurrencyConversionPlus>> getMaybeCurrencyConversions();

    /**
     * Gets the CurrencyConversions from the table CurrencyConversion joined with the currency names from the table Currency.
     * @param baseCode: the base currency code.
     * @param targetCode: the target currency code.
     * @return the CurrencyConversions joined with the currency names in a list that matches
     * with the base code and target code in a list ordered by date starting from the latest inserted or updated.
     */
    @Query("SELECT cc._id, " +
            "cc.base_code, " +
            "bc.name AS " + CurrencyConversionPlus.COLUMN_INFO_BASE_CURRENCY_NAME + ", " +
            "cc.target_code, " +
            "tc.name AS " + CurrencyConversionPlus.COLUMN_INFO_TARGET_CURRENCY_NAME + ", " +
            "cc.value, cc.date, cc.source " +
            "FROM CurrencyConversion cc " +
            "INNER JOIN Currency bc ON cc.base_code = :baseCode AND cc.target_code = :targetCode AND cc.base_code = bc.code " +
            "INNER JOIN Currency tc ON cc.target_code = tc.code ORDER BY cc.date DESC")
    Maybe<List<CurrencyConversionPlus>> getMaybeCurrencyConversionsByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);

    /**
     * Gets the latest CurrencyConversion from the table CurrencyConversion joined with the currency names from the table Currency.
     * @param baseCode: the base currency code.
     * @param targetCode: the target currency code.
     * @return the latest CurrencyConversions joined with the currency names in a list that matches
     * with the base code and target code in a list ordered by date starting from the latest inserted or updated.
     */
    @Query("SELECT cc._id, " +
            "cc.base_code, " +
            "bc.name AS " + CurrencyConversionPlus.COLUMN_INFO_BASE_CURRENCY_NAME + ", " +
            "cc.target_code, " +
            "tc.name AS " + CurrencyConversionPlus.COLUMN_INFO_TARGET_CURRENCY_NAME + ", " +
            "cc.value, cc.date, cc.source " +
            "FROM CurrencyConversion cc " +
            "INNER JOIN Currency bc ON cc.base_code = :baseCode AND cc.target_code = :targetCode AND cc.base_code = bc.code " +
            "INNER JOIN Currency tc ON cc.target_code = tc.code ORDER BY cc.date DESC LIMIT 1")
    Maybe<CurrencyConversionPlus> getMaybeLatestCurrencyConversionByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);

    /**
     * Gets the latest distinct CurrencyConversions from the table CurrencyConversion joined with the currency names from the table Currency.
     * @return the latest distinct CurrencyConversions joined with the currency names in a list.
     */
    @Query("SELECT MAX(cc.date)," +
            "cc._id, " +
            "cc.base_code, " +
            "bc.name AS " + CurrencyConversionPlus.COLUMN_INFO_BASE_CURRENCY_NAME + ", " +
            "cc.target_code, " +
            "tc.name AS " + CurrencyConversionPlus.COLUMN_INFO_TARGET_CURRENCY_NAME + ", " +
            "cc.value, cc.date, cc.source " +
            "FROM CurrencyConversion cc " +
            "INNER JOIN Currency bc ON cc.base_code = bc.code " +
            "INNER JOIN Currency tc ON cc.target_code = tc.code " +
            "GROUP BY cc.base_code, cc.target_code ")
    Maybe<List<CurrencyConversionPlus>> getMaybeLatestDistinctCurrencyConversions();

    /**
     * Gets the latest distinct CurrencyConversions from the table CurrencyConversion joined with the currency names from the table Currency.
     * @param baseCode: the base currency code.
     * @return the latest distinct CurrencyConversions joined with the currency names in a list.
     */
    @Query("SELECT MAX(cc.date)," +
            "cc._id, " +
            "cc.base_code, " +
            "bc.name AS " + CurrencyConversionPlus.COLUMN_INFO_BASE_CURRENCY_NAME + ", " +
            "cc.target_code, " +
            "tc.name AS " + CurrencyConversionPlus.COLUMN_INFO_TARGET_CURRENCY_NAME + ", " +
            "cc.value, cc.date, cc.source " +
            "FROM CurrencyConversion cc " +
            "INNER JOIN Currency bc ON cc.base_code = :baseCode AND cc.base_code = bc.code " +
            "INNER JOIN Currency tc ON cc.target_code = tc.code " +
            "GROUP BY cc.target_code")
    Maybe<List<CurrencyConversionPlus>> getMaybeLatestDistinctCurrencyConversionsByBase(@NonNull String baseCode);

}
