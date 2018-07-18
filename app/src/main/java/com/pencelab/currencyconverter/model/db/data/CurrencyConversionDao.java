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
     * @param baseSymbol: the base currency symbol.
     * @param targetSymbol: the target currency symbol.
     * @return the CurrencyConversions from the table that matches with the base symbol and target symbol
     * in a list ordered by date starting from the latest inserted or updated.
     */
    @Query("SELECT * FROM CurrencyConversion WHERE base_symbol = :baseSymbol AND target_symbol = :targetSymbol ORDER BY date DESC")
    Flowable<List<CurrencyConversion>> getFlowableCurrencyConversionsByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol);

    /**
     * Gets the latest CurrencyConversion from the table.
     * @param baseSymbol: the base currency symbol.
     * @param targetSymbol: the target currency symbol.
     * @return the latest CurrencyConversion from the table that matches with the base symbol and target symbol.
     */
    @Query("SELECT * FROM CurrencyConversion WHERE base_symbol = :baseSymbol AND target_symbol = :targetSymbol ORDER BY date DESC LIMIT 1")
    Flowable<List<CurrencyConversion>> getFlowableLatestCurrencyConversionByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol);

    /**
     * Gets the latest distinct CurrencyConversions from the table.
     * @return the latest distinct CurrencyConversions from the table.
     */
    @Query("SELECT * FROM CurrencyConversion GROUP BY base_symbol, target_symbol ORDER BY date DESC")
    Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversions();

    /**
     * Gets the latest distinct CurrencyConversions from the table.
     * @param baseSymbol: the base currency symbol.
     * @return the latest distinct CurrencyConversions from the table that match with the base symbol.
     */
    @Query("SELECT * FROM CurrencyConversion WHERE base_symbol = :baseSymbol GROUP BY target_symbol ORDER BY date DESC")
    Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversionsByBase(@NonNull String baseSymbol);

    /**
     * Gets all the CurrencyConversions from the table.
     * @return all the CurrencyConversions from the table in a list.
     */
    @Query("SELECT * FROM CurrencyConversion")
    Maybe<List<CurrencyConversion>> getMaybeCurrencyConversions();

    /**
     * Gets the CurrencyConversions from the table.
     * @param baseSymbol: the base currency symbol.
     * @param targetSymbol: the target currency symbol.
     * @return the CurrencyConversions from the table that matches with the base symbol and target symbol
     * in a list ordered by date starting from the latest inserted or updated.
     */
    @Query("SELECT * FROM CurrencyConversion WHERE base_symbol = :baseSymbol AND target_symbol = :targetSymbol ORDER BY date DESC")
    Maybe<List<CurrencyConversion>> getMaybeCurrencyConversionsByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol);

    /**
     * Gets the latest CurrencyConversion from the table.
     * @param baseSymbol: the base currency symbol.
     * @param targetSymbol: the target currency symbol.
     * @return the latest CurrencyConversion from the table that matches with the base symbol and target symbol.
     */
    @Query("SELECT * FROM CurrencyConversion WHERE base_symbol = :baseSymbol AND target_symbol = :targetSymbol ORDER BY date DESC LIMIT 1")
    Maybe<List<CurrencyConversion>> getMaybeLatestCurrencyConversionByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol);

    /**
     * Gets the latest distinct CurrencyConversions from the table.
     * @return the latest distinct CurrencyConversions from the table.
     */
    @Query("SELECT * FROM CurrencyConversion GROUP BY base_symbol, target_symbol ORDER BY date DESC")
    Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversions();

    /**
     * Gets the latest distinct CurrencyConversions from the table.
     * @param baseSymbol: the base currency symbol.
     * @return the latest distinct CurrencyConversions from the table that match with the base symbol.
     */
    @Query("SELECT * FROM CurrencyConversion WHERE base_symbol = :baseSymbol GROUP BY target_symbol ORDER BY date DESC")
    Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversionsByBase(@NonNull String baseSymbol);

    /**
     * Inserts a CurrencyConversion into the database. If the Currency already exists, replaces it.
     * @param currencyConversion the CurrencyConversion to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateCurrencyConversion(@NonNull CurrencyConversion currencyConversion);

    /**
     * Delete all CurrencyConversions.
     */
    @Query("DELETE FROM CurrencyConversion")
    void deleteAllCurrencyConversions();

}
