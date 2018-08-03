package com.pencelab.currencyconverter.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.pencelab.currencyconverter.model.db.data.Currency;
import com.pencelab.currencyconverter.model.db.repository.CurrencyRepository;

import java.util.List;

import io.reactivex.Maybe;

public class CurrencyViewModel extends ViewModel {

    private CurrencyRepository currencyRepository;

    public CurrencyViewModel(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Maybe<List<Currency>> getCurrencies(){
        return this.currencyRepository.getMaybeCurrencies();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
