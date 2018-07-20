package com.pencelab.currencyconverter.model.db.repository;

import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.CurrencyConversion;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface CurrencyConversionRepository {
    Flowable<List<CurrencyConversion>> getFlowableCurrencyConversions();
    Flowable<List<CurrencyConversion>> getFlowableCurrencyConversionsByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);
    Flowable<List<CurrencyConversion>> getFlowableLatestCurrencyConversionByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);
    Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversions();
    Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversionsByBase(@NonNull String baseCode);

    Maybe<List<CurrencyConversion>> getMaybeCurrencyConversions();
    Maybe<List<CurrencyConversion>> getMaybeCurrencyConversionsByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);
    Maybe<List<CurrencyConversion>> getMaybeLatestCurrencyConversionByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode);
    Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversions();
    Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversionsByBase(@NonNull String baseCode);

    void insertOrUpdateCurrencyConversion(@NonNull CurrencyConversion currencyConversion);
    void deleteAllCurrencyConversions();
}
