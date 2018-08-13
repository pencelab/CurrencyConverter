package com.pencelab.currencyconverter.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.pencelab.currencyconverter.R;
import com.pencelab.currencyconverter.common.Utils;
import com.pencelab.currencyconverter.dependencyinjection.App;
import com.pencelab.currencyconverter.model.db.data.Currency;
import com.pencelab.currencyconverter.model.db.repository.CurrencyRepository;
import com.pencelab.currencyconverter.viewmodel.CurrencyViewModel;
import com.pencelab.currencyconverter.viewmodel.CurrencyViewModelFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddConversionActivity extends AppCompatActivity {

    @BindView(R.id.conversion_code_base)
    AutoCompleteTextView conversionBaseCode;

    @BindView(R.id.conversion_name_base)
    TextView conversionBaseName;

    @BindView(R.id.conversion_code_target)
    AutoCompleteTextView conversionTargetCode;

    @BindView(R.id.conversion_name_target)
    TextView conversionTargetName;

    @BindView(R.id.button_add_conversion)
    Button addCurrencyButton;

    @Inject
    CurrencyViewModelFactory currencyViewModelFactory;

    private CurrencyViewModel currencyViewModel;

    private CompositeDisposable disposables;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conversion);

        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);

        this.init();
    }

    private void init(){
        this.currencyViewModel = ViewModelProviders.of(this, this.currencyViewModelFactory).get(CurrencyViewModel.class);

        this.conversionBaseCode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                this.validateBaseCurrency();
                this.validateData();
            }
            return false;
        });

        this.conversionBaseCode.setOnItemClickListener((parent, view, position, id) -> {
            String[] data = this.adapter.getItem(position).split("-");
            this.conversionBaseCode.setText(data[0].trim());
            this.conversionBaseName.setText(data[1].trim());
            this.validateBaseCurrency();
            this.validateData();
        });

        this.conversionBaseCode.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                this.validateBaseCurrency();
                this.validateData();
            }
        });

        this.conversionBaseCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversionBaseName.setText("");
                addCurrencyButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.conversionTargetCode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                this.validateTargetCurrency();
                this.validateData();
            }
            return false;
        });

        this.conversionTargetCode.setOnItemClickListener((parent, view, position, id) -> {
            String[] data = this.adapter.getItem(position).split("-");
            this.conversionTargetCode.setText(data[0].trim());
            this.conversionTargetName.setText(data[1].trim());
            this.validateTargetCurrency();
            this.validateData();
        });

        this.conversionTargetCode.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                this.validateTargetCurrency();
                this.validateData();
            }
        });

        this.conversionTargetCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversionTargetName.setText("");
                addCurrencyButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void validateBaseCurrency(){
        if(this.currencyViewModel.isValidCode(this.conversionBaseCode.getText().toString())){
            this.conversionBaseCode.setTextColor(getResources().getColor(R.color.colorText));
        }else{
            this.conversionBaseCode.setTextColor(getResources().getColor(R.color.colorError));
        }
    }

    private void validateTargetCurrency(){
        if(this.currencyViewModel.isValidCode(this.conversionTargetCode.getText().toString())){
            this.conversionTargetCode.setTextColor(getResources().getColor(R.color.colorText));
        }else{
            this.conversionTargetCode.setTextColor(getResources().getColor(R.color.colorError));
        }
    }

    private void validateData(){
        if(this.currencyViewModel.isValidData(this.conversionBaseCode.getText().toString(), this.conversionTargetCode.getText().toString())){
            this.addCurrencyButton.setEnabled(true);
        }else{
            this.addCurrencyButton.setEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.disposables = new CompositeDisposable();
        this.observeCurrencies();
    }

    private void observeCurrencies(){
        this.disposables.add(
                this.currencyViewModel.getCurrenciesText()
                        /*.subscribeOn(Schedulers.io())
                        .map(list -> {
                            String[] currenciesArray = new String[list.size()];
                            for(int i = 0; i < list.size(); i++)
                                currenciesArray[i] = list.get(i).getCode() + " - " + list.get(i).getName();

                            return currenciesArray;
                        })*/
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(currenciesArray -> {
                            this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, currenciesArray);
                            this.conversionBaseCode.setAdapter(adapter);
                            this.conversionTargetCode.setAdapter(adapter);
                        })
        );
    }

    @OnClick(R.id.button_add_conversion)
    void addCurrencyOnClick(){
        Utils.log("SHOULD ADD CURRENCY NOW!!!");
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(this.disposables != null){
            this.disposables.dispose();
            this.disposables = null;
        }
    }
}
