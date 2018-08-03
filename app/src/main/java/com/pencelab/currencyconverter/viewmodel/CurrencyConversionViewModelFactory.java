package com.pencelab.currencyconverter.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.http.CurrencyLayerService;
import com.pencelab.currencyconverter.model.db.repository.CurrencyConversionPlusRepository;

import javax.inject.Inject;

public class CurrencyConversionViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    CurrencyConversionPlusRepository currencyConversionPlusRepository;

    @Inject
    CurrencyLayerService currencyLayerService;

    @Inject
    public CurrencyConversionViewModelFactory(){
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CurrencyConversionViewModel.class)){
            return (T) new CurrencyConversionViewModel(this.currencyConversionPlusRepository, this.currencyLayerService);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
