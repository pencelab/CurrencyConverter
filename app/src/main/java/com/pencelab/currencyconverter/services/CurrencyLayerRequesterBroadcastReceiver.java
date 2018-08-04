package com.pencelab.currencyconverter.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CurrencyLayerRequesterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, CurrencyLayerRequesterService.class));
    }
}
