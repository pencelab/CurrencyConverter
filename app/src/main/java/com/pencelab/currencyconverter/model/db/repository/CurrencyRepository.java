package com.pencelab.currencyconverter.model.db.repository;

import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.Currency;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface CurrencyRepository {
    Flowable<List<Currency>> getFlowableCurrencies();
    Flowable<Currency> getFlowableCurrencyById(@NonNull String symbol);
    Flowable<List<Currency>> getFlowableCurrencyByLocation(@NonNull String location);
    Flowable<List<Currency>> getFlowableCurrencyByName(@NonNull String name);

    Maybe<List<Currency>> getMaybeCurrencies();
    Maybe<Currency> getMaybeCurrencyById(@NonNull String symbol);
    Maybe<List<Currency>> getMaybeCurrencyByLocation(@NonNull String location);
    Maybe<List<Currency>> getMaybeCurrencyByName(@NonNull String name);

    void insertOrUpdateCurrency(@NonNull Currency currency);
    void deleteAllCurrencies();
}
