package com.pencelab.currencyconverter.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.pencelab.currencyconverter.http.CurrencyLayerService;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionPlus;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionPlusRepository;

import java.util.List;

import io.reactivex.Maybe;

public class CurrencyConversionViewModel extends ViewModel {

    private CurrencyConversionPlusRepository currencyConversionPlusRepository;
    private CurrencyLayerService currencyLayerService;

    public CurrencyConversionViewModel(CurrencyConversionPlusRepository currencyConversionPlusRepository, CurrencyLayerService currencyLayerService){
        this.currencyConversionPlusRepository = currencyConversionPlusRepository;
        this.currencyLayerService = currencyLayerService;
    }

    public Maybe<List<CurrencyConversionPlus>> getLatestConversions(){
        return this.currencyConversionPlusRepository.getMaybeLatestCurrencyConversions();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
