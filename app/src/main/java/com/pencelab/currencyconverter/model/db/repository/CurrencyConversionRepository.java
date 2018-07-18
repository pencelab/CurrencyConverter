package com.pencelab.currencyconverter.model.db.repository;

import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.CurrencyConversion;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface CurrencyConversionRepository {
    Flowable<List<CurrencyConversion>> getFlowableCurrencyConversions();
    Flowable<List<CurrencyConversion>> getFlowableCurrencyConversionsByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol);
    Flowable<List<CurrencyConversion>> getFlowableLatestCurrencyConversionByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol);
    Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversions();
    Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversionsByBase(@NonNull String baseSymbol);

    Maybe<List<CurrencyConversion>> getMaybeCurrencyConversions();
    Maybe<List<CurrencyConversion>> getMaybeCurrencyConversionsByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol);
    Maybe<List<CurrencyConversion>> getMaybeLatestCurrencyConversionByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol);
    Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversions();
    Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversionsByBase(@NonNull String baseSymbol);

    void insertOrUpdateCurrencyConversion(@NonNull CurrencyConversion currencyConversion);
    void deleteAllCurrencyConversions();
}
