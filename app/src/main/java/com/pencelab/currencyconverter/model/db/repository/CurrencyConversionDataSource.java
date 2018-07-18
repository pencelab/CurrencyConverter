package com.pencelab.currencyconverter.model.db.repository;

import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.CurrencyConversion;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionDao;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class CurrencyConversionDataSource implements CurrencyConversionRepository {

    private final CurrencyConversionDao currencyConversionDao;

    public CurrencyConversionDataSource(@NonNull CurrencyConversionDao currencyConversionDao) {
        this.currencyConversionDao = currencyConversionDao;
    }

    @Override
    public Flowable<List<CurrencyConversion>> getFlowableCurrencyConversions() {
        return this.currencyConversionDao.getFlowableCurrencyConversions();
    }

    @Override
    public Flowable<List<CurrencyConversion>> getFlowableCurrencyConversionsByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol) {
        return this.currencyConversionDao.getFlowableCurrencyConversionsByBaseAndTarget(baseSymbol, targetSymbol);
    }

    @Override
    public Flowable<List<CurrencyConversion>> getFlowableLatestCurrencyConversionByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol) {
        return this.currencyConversionDao.getFlowableLatestCurrencyConversionByBaseAndTarget(baseSymbol, targetSymbol);
    }

    @Override
    public Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversions() {
        return this.currencyConversionDao.getFlowableLatestDistinctCurrencyConversions();
    }

    @Override
    public Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversionsByBase(@NonNull String baseSymbol) {
        return this.currencyConversionDao.getFlowableLatestDistinctCurrencyConversionsByBase(baseSymbol);
    }

    @Override
    public Maybe<List<CurrencyConversion>> getMaybeCurrencyConversions() {
        return this.currencyConversionDao.getMaybeCurrencyConversions();
    }

    @Override
    public Maybe<List<CurrencyConversion>> getMaybeCurrencyConversionsByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol) {
        return this.currencyConversionDao.getMaybeCurrencyConversionsByBaseAndTarget(baseSymbol, targetSymbol);
    }

    @Override
    public Maybe<List<CurrencyConversion>> getMaybeLatestCurrencyConversionByBaseAndTarget(@NonNull String baseSymbol, @NonNull String targetSymbol) {
        return this.currencyConversionDao.getMaybeLatestCurrencyConversionByBaseAndTarget(baseSymbol, targetSymbol);
    }

    @Override
    public Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversions() {
        return this.currencyConversionDao.getMaybeLatestDistinctCurrencyConversions();
    }

    @Override
    public Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversionsByBase(@NonNull String baseSymbol) {
        return this.currencyConversionDao.getMaybeLatestDistinctCurrencyConversionsByBase(baseSymbol);
    }

    @Override
    public void insertOrUpdateCurrencyConversion(@NonNull CurrencyConversion currencyConversion) {
        this.currencyConversionDao.insertOrUpdateCurrencyConversion(currencyConversion);
    }

    @Override
    public void deleteAllCurrencyConversions() {
        this.currencyConversionDao.deleteAllCurrencyConversions();
    }
}
