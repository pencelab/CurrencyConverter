package com.pencelab.currencyconverter.model.db.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface CurrencyConversionDao {

    /**
     * Gets all the CurrencyConversions from the table.
     * @return all the CurrencyConversions from the table in a list.
     */
    @Query("SELECT * FROM CurrencyConversion")
    Flowable<List<CurrencyConversion>> getFlowableCurrencyConversions();

    /**
     * Gets the CurrencyConversions from the table.
     * @param baseCode: the base currency code.
     * @param targetCode: the target currency code.
     * @return the CurrencyConversions from the table that matches with the base code and target code
     * in a list ordered by date starting from the latest inserted or updated.
     */
    @Query("SELECT * FROM CurrencyConversion WHERE base_code = :baseCode AND target_code = :targetCode ORDER BY date DESC")
    Flowable<List<CurrencyConversion>> getFlowableCurrencyConversionsByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);

    /**
     * Gets the latest CurrencyConversion from the table.
     * @param baseCode: the base currency code.
     * @param targetCode: the target currency code.
     * @return the latest CurrencyConversion from the table that matches with the base code and target code.
     */
    @Query("SELECT * FROM CurrencyConversion WHERE base_code = :baseCode AND target_code = :targetCode ORDER BY date DESC LIMIT 1")
    Flowable<CurrencyConversion> getFlowableLatestCurrencyConversionByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);

    /**
     * Gets the latest distinct CurrencyConversions from the table.
     * @return the latest distinct CurrencyConversions from the table.
     */
    @Query("SELECT *, MAX(date) FROM CurrencyConversion GROUP BY base_code, target_code")
    Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversions();

    /**
     * Gets the latest distinct CurrencyConversions from the table.
     * @param baseCode: the base currency code.
     * @return the latest distinct CurrencyConversions from the table that match with the base code.
     */
    @Query("SELECT *, MAX(date) FROM CurrencyConversion WHERE base_code = :baseCode GROUP BY target_code")
    Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversionsByBase(@NonNull String baseCode);

    /**
     * Gets all the CurrencyConversions from the table.
     * @return all the CurrencyConversions from the table in a list.
     */
    @Query("SELECT * FROM CurrencyConversion")
    Maybe<List<CurrencyConversion>> getMaybeCurrencyConversions();

    /**
     * Gets the CurrencyConversions from the table.
     * @param baseCode: the base currency code.
     * @param targetCode: the target currency code.
     * @return the CurrencyConversions from the table that matches with the base code and target code
     * in a list ordered by date starting from the latest inserted or updated.
     */
    @Query("SELECT * FROM CurrencyConversion WHERE base_code = :baseCode AND target_code = :targetCode ORDER BY date DESC")
    Maybe<List<CurrencyConversion>> getMaybeCurrencyConversionsByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);

    /**
     * Gets the latest CurrencyConversion from the table.
     * @param baseCode: the base currency code.
     * @param targetCode: the target currency code.
     * @return the latest CurrencyConversion from the table that matches with the base code and target code.
     */
    @Query("SELECT * FROM CurrencyConversion WHERE base_code = :baseCode AND target_code = :targetCode ORDER BY date DESC LIMIT 1")
    Maybe<CurrencyConversion> getMaybeLatestCurrencyConversionByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);

    /**
     * Gets the latest distinct CurrencyConversions from the table.
     * @return the latest distinct CurrencyConversions from the table.
     */
    @Query("SELECT *, MAX(date) FROM CurrencyConversion GROUP BY base_code, target_code")
    Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversions();

    /**
     * Gets the latest distinct CurrencyConversions from the table.
     * @param baseCode: the base currency code.
     * @return the latest distinct CurrencyConversions from the table that match with the base code.
     */
    @Query("SELECT *, MAX(date) FROM CurrencyConversion WHERE base_code = :baseCode GROUP BY target_code")
    Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversionsByBase(@NonNull String baseCode);

    /**
     * Inserts a CurrencyConversion into the database. If the CurrencyConversion already exists, replaces it.
     * @param currencyConversion the CurrencyConversion to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateCurrencyConversion(@NonNull CurrencyConversion currencyConversion);

    /**
     * Inserts all CurrencyConversions into the database. If any CurrencyConversion already exists, replaces it.
     * @param currencyConversions the CurrencyConversions to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateCurrencyConversions(CurrencyConversion... currencyConversions);

    /**
     * Delete all CurrencyConversions.
     */
    @Query("DELETE FROM CurrencyConversion")
    void deleteAllCurrencyConversions();

}
