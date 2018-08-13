package com.pencelab.currencyconverter.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.pencelab.currencyconverter.model.db.data.Currency;
import com.pencelab.currencyconverter.model.db.repository.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class CurrencyViewModel extends ViewModel {

    private CurrencyRepository currencyRepository;

    private BehaviorSubject<String[]> currencySubject = BehaviorSubject.create();

    private List<String> codesList = new ArrayList<>();

    public CurrencyViewModel(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /*public Maybe<List<Currency>> getCurrencies(){
        return this.currencyRepository.getMaybeCurrencies();
    }*/

    public BehaviorSubject<String[]> getCurrenciesText() {
        this.currencyRepository.getMaybeCurrencies()
                .subscribeOn(Schedulers.io())
                .map(list -> {
                    this.codesList.clear();
                    String[] currenciesArray = new String[list.size()];
                    for(int i = 0; i < list.size(); i++) {
                        codesList.add(list.get(i).getCode());
                        currenciesArray[i] = list.get(i).getCode() + " - " + list.get(i).getName();
                    }

                    return currenciesArray;
                })
                .subscribe(currenciesArray -> this.currencySubject.onNext(currenciesArray));

        return this.currencySubject;
    }

    public boolean isValidCode(String code){
        return this.codesList.contains(code);
    }

    public boolean isValidData(String baseCode, String targetCode){
        return this.isValidCode(baseCode) && this.isValidCode(targetCode);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(this.currencySubject != null)
            this.currencySubject.onComplete();
    }
}
