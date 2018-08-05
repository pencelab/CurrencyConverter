package com.pencelab.currencyconverter.view;

import android.app.ActivityManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pencelab.currencyconverter.R;
import com.pencelab.currencyconverter.common.Utils;
import com.pencelab.currencyconverter.dependencyinjection.App;
import com.pencelab.currencyconverter.model.db.data.Currency;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionPlus;
import com.pencelab.currencyconverter.services.CurrencyLayerRequesterService;
import com.pencelab.currencyconverter.viewmodel.CurrencyConversionViewModel;
import com.pencelab.currencyconverter.viewmodel.CurrencyConversionViewModelFactory;
import com.pencelab.currencyconverter.viewmodel.CurrencyViewModel;
import com.pencelab.currencyconverter.viewmodel.CurrencyViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CurrencyMonitorActivity extends AppCompatActivity {

    @BindView(R.id.currency_conversion_recycler_view)
    RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private CurrencyConversionDataAdapter currencyConversionDataAdapter;

    private String accessKey = "8602e06a75aa63372e10373e989426b5";

    private CompositeDisposable disposables;

    @Inject
    CurrencyConversionViewModelFactory currencyConversionViewModelFactory;

    @Inject
    CurrencyViewModelFactory currencyViewModelFactory;

    private CurrencyConversionViewModel currencyConversionViewModel;
    private CurrencyViewModel currencyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_monitor);

        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);

        this.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(){
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        this.currencyConversionDataAdapter = new CurrencyConversionDataAdapter();
        this.recyclerView.setAdapter(this.currencyConversionDataAdapter);

        this.currencyConversionViewModel = ViewModelProviders.of(this, currencyConversionViewModelFactory).get(CurrencyConversionViewModel.class);
        this.currencyViewModel = ViewModelProviders.of(this, currencyViewModelFactory).get(CurrencyViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.disposables = new CompositeDisposable();

        //this.observeCurrencyLayerData();
        this.observeCurrencyConversionsFromDataBase();
    }

    /*public void observeCurrencyLayerData(){

        this.disposables.add(
            Observable.interval(0, 1, TimeUnit.HOURS)
                    .subscribeOn(Schedulers.io())
                    .flatMapSingle(i -> this.currencyLayerService.getCurrencyLayerDataSingle(this.accessKey))
                    .doOnError(error -> Utils.log(error))
                    .onExceptionResumeNext(e -> Utils.log("OOPS!!! Exception: " + e))
                    .subscribe(currencyLayerData -> {
                        Utils.log(currencyLayerData.toString());
                    })
        );
    }*/

    private void observeCurrencyConversionsFromDataBase(){
        this.disposables.add(
                this.currencyConversionViewModel.getLatestConversions()
                    .subscribeOn(Schedulers.single())
                    .subscribe(list -> {
                        for (CurrencyConversionPlus ccp : list){
                            if (this.currencyConversionDataAdapter.add(ccp))
                                this.recyclerView.smoothScrollToPosition(0);
                        }
                    })
        );
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(this.disposables != null) {
            this.disposables.dispose();
            this.disposables = null;
        }
    }
}
