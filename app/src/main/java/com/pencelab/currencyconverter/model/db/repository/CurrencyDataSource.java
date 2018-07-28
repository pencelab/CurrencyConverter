package com.pencelab.currencyconverter.model.db.repository;

import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.data.Currency;
import com.pencelab.currencyconverter.model.db.data.CurrencyDao;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class CurrencyDataSource implements CurrencyRepository {

    private final CurrencyDao currencyDao;

    public CurrencyDataSource(@NonNull CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    @Override
    public Flowable<List<Currency>> getFlowableCurrencies() {
        return this.currencyDao.getFlowableCurrencies();
    }

    @Override
    public Flowable<Currency> getFlowableCurrencyByCode(@NonNull String code) {
        return this.currencyDao.getFlowableCurrencyByCode(code);
    }

    @Override
    public Flowable<List<Currency>> getFlowableCurrencyByName(@NonNull String name) {
        return this.currencyDao.getFlowableCurrencyByName(name);
    }

    @Override
    public Maybe<List<Currency>> getMaybeCurrencies() {
        return this.currencyDao.getMaybeCurrencies();
    }

    @Override
    public Maybe<Currency> getMaybeCurrencyByCode(@NonNull String code) {
        return this.currencyDao.getMaybeCurrencyByCode(code);
    }

    @Override
    public Maybe<List<Currency>> getMaybeCurrencyByName(@NonNull String name) {
        return this.currencyDao.getMaybeCurrencyByName(name);
    }

    @Override
    public void insertOrUpdateCurrency(@NonNull Currency currency) {
        this.currencyDao.insertOrUpdateCurrency(currency);
    }

    @Override
    public void insertOrUpdateCurrencies(Currency... currencies) {
        this.currencyDao.insertOrUpdateCurrencies(currencies);
    }

    @Override
    public void deleteAllCurrencies() {
        this.currencyDao.deleteAllCurrencies();
    }

    @Override
    public void deleteCurrencyByCode(@NonNull String code) {
        this.currencyDao.deleteCurrency(code);
    }
}
