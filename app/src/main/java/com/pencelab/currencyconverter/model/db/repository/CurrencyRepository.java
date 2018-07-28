package com.pencelab.currencyconverter.model.db.repository;

import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.Currency;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface CurrencyRepository {
    Flowable<List<Currency>> getFlowableCurrencies();
    Flowable<Currency> getFlowableCurrencyByCode(@NonNull String code);
    Flowable<List<Currency>> getFlowableCurrencyByName(@NonNull String name);

    Maybe<List<Currency>> getMaybeCurrencies();
    Maybe<Currency> getMaybeCurrencyByCode(@NonNull String code);
    Maybe<List<Currency>> getMaybeCurrencyByName(@NonNull String name);

    void insertOrUpdateCurrency(@NonNull Currency currency);
    void insertOrUpdateCurrencies(Currency... currencies);
    void deleteAllCurrencies();
    void deleteCurrencyByCode(@NonNull String code);
}
