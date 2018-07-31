package com.pencelab.currencyconverter.model.db.repository;

import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.CurrencyConversionPlus;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionPlusDao;

import java.util.List;

import io.reactivex.Flowable;

public class CurrencyConversionPlusDataSource implements CurrencyConversionPlusRepository {

    private final CurrencyConversionPlusDao currencyConversionPlusDao;

    public CurrencyConversionPlusDataSource(@NonNull CurrencyConversionPlusDao currencyConversionPlusDao) {
        this.currencyConversionPlusDao = currencyConversionPlusDao;
    }

    @Override
    public Flowable<List<CurrencyConversionPlus>> getFlowableCurrencyConversions() {
        return this.currencyConversionPlusDao.getFlowableCurrencyConversions();
    }
}
