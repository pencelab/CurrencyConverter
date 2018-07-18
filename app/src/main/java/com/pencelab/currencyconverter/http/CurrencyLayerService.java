package com.pencelab.currencyconverter.http;

import com.pencelab.currencyconverter.http.currencylayer.model.CurrencyLayerData;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CurrencyLayerService {

    /*@GET("live?access_key={access_key}")
    Single<CurrencyLayerData> getCurrencyLayerDataSingle(@Path("access_key") String accessKey);*/

    @GET("live")
    Single<CurrencyLayerData> getCurrencyLayerDataSingle(@Query("access_key") String accessKey);

}
