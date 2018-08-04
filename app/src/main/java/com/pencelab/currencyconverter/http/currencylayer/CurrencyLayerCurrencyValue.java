package com.pencelab.currencyconverter.http.currencylayer;

import android.support.annotation.NonNull;

public final class CurrencyLayerCurrencyValue{

    @NonNull
    private String currency;

    @NonNull
    private Double value;

    public CurrencyLayerCurrencyValue(@NonNull String currency, @NonNull Double value){
        this.currency = currency;
        this.value = value;
    }

    @NonNull
    public String getCurrency() {
        return currency;
    }

    @NonNull
    public double getValue() {
        return value;
    }
}
