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
public interface CurrencyDao {

    /**
     * Gets all the Currencies from the table.
     * @return all the Currencies from the table in a list.
     */
    @Query("SELECT * FROM Currency")
    Flowable<List<Currency>> getFlowableCurrencies();

    /**
     * Gets the Currency from the table.
     * @param code: the currency code.
     * @return the Currency from the table that matches with the code.
     */
    @Query("SELECT * FROM Currency WHERE code = :code")
    Flowable<Currency> getFlowableCurrencyByCode(@NonNull String code);

    /**
     * Gets the Currencies from the table.
     * @param name: the currency name.
     * @return all the Currencies from the table that match with the name in a list.
     */
    @Query("SELECT * FROM Currency WHERE name = :name")
    Flowable<List<Currency>> getFlowableCurrencyByName(@NonNull String name);

    /**
     * Gets all the Currencies from the table.
     * @return all the Currencies from the table in a list.
     */
    @Query("SELECT * FROM Currency")
    Maybe<List<Currency>> getMaybeCurrencies();

    /**
     * Gets the Currency from the table.
     * @param code: the currency code.
     * @return the Currency from the table that matches with the code.
     */
    @Query("SELECT * FROM Currency WHERE code = :code")
    Maybe<Currency> getMaybeCurrencyByCode(@NonNull String code);

    /**
     * Gets the Currencies from the table.
     * @param name: the currency name.
     * @return all the Currencies from the table that match with the name in a list.
     */
    @Query("SELECT * FROM Currency WHERE name = :name")
    Maybe<List<Currency>> getMaybeCurrencyByName(@NonNull String name);

    /**
     * Inserts a Currency into the database. If the Currency already exists, replaces it.
     * @param currency the Currency to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateCurrency(@NonNull Currency currency);

    /**
     * Delete all Currencies.
     */
    @Query("DELETE FROM Currency")
    void deleteAllCurrencies();

}
