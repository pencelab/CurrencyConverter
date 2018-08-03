package com.pencelab.currencyconverter.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.model.db.repository.CurrencyRepository;

import javax.inject.Inject;

public class CurrencyViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    CurrencyRepository currencyRepository;

    @Inject
    public CurrencyViewModelFactory(){
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CurrencyViewModel.class)){
            return (T) new CurrencyViewModel(this.currencyRepository);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
