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
    public Flowable<List<CurrencyConversion>> getFlowableCurrencyConversionsByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode) {
        return this.currencyConversionDao.getFlowableCurrencyConversionsByBaseAndTarget(baseCode, targetCode);
    }

    @Override
    public Flowable<List<CurrencyConversion>> getFlowableLatestCurrencyConversionByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode) {
        return this.currencyConversionDao.getFlowableLatestCurrencyConversionByBaseAndTarget(baseCode, targetCode);
    }

    @Override
    public Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversions() {
        return this.currencyConversionDao.getFlowableLatestDistinctCurrencyConversions();
    }

    @Override
    public Flowable<List<CurrencyConversion>> getFlowableLatestDistinctCurrencyConversionsByBase(@NonNull String baseCode) {
        return this.currencyConversionDao.getFlowableLatestDistinctCurrencyConversionsByBase(baseCode);
    }

    @Override
    public Maybe<List<CurrencyConversion>> getMaybeCurrencyConversions() {
        return this.currencyConversionDao.getMaybeCurrencyConversions();
    }

    @Override
    public Maybe<List<CurrencyConversion>> getMaybeCurrencyConversionsByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode) {
        return this.currencyConversionDao.getMaybeCurrencyConversionsByBaseAndTarget(baseCode, targetCode);
    }

    @Override
    public Maybe<List<CurrencyConversion>> getMaybeLatestCurrencyConversionByBaseAndTarget(@NonNull String baseCode, @NonNull String targetCode) {
        return this.currencyConversionDao.getMaybeLatestCurrencyConversionByBaseAndTarget(baseCode, targetCode);
    }

    @Override
    public Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversions() {
        return this.currencyConversionDao.getMaybeLatestDistinctCurrencyConversions();
    }

    @Override
    public Maybe<List<CurrencyConversion>> getMaybeLatestDistinctCurrencyConversionsByBase(@NonNull String baseCode) {
        return this.currencyConversionDao.getMaybeLatestDistinctCurrencyConversionsByBase(baseCode);
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
