package com.pencelab.currencyconverter.model.db.repository;

import com.pencelab.currencyconverter.model.db.data.CurrencyConversionPlus;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface CurrencyConversionPlusRepository {

    Flowable<List<CurrencyConversionPlus>> getFlowableLatestCurrencyConversions();
    Maybe<List<CurrencyConversionPlus>> getMaybeLatestCurrencyConversions();

}
