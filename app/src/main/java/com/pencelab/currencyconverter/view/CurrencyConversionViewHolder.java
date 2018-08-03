package com.pencelab.currencyconverter.view;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.pencelab.currencyconverter.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyConversionViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.currency_conversion_item_info_symbol_left)
    TextView symbolLeft;

    @BindView(R.id.currency_conversion_item_info_symbol_right)
    TextView symbolRight;

    @BindView(R.id.currency_conversion_item_info_name_left)
    TextView nameLeft;

    @BindView(R.id.currency_conversion_item_info_name_right)
    TextView nameRight;

    @BindView(R.id.currency_conversion_item_info_amount_left)
    TextView amountLeft;

    @BindView(R.id.currency_conversion_item_info_amount_right)
    TextView amountRight;

    @BindView(R.id.currency_conversion_item_date)
    TextView date;

    @BindView(R.id.currency_conversion_item_source)
    TextView source;

    private static final NumberFormat BASE_CURRENCY_FORMAT = new DecimalFormat("#0");
    private static final NumberFormat TARGET_CURRENCY_FORMAT = new DecimalFormat("#0.00");

    private String dateFormat = "";

    public CurrencyConversionViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
        this.dateFormat = v.getResources().getString(R.string.format_date);
    }

    public void setSymbolLeft(String symbolLeft) {
        this.symbolLeft.setText(symbolLeft);
    }

    public void setSymbolRight(String symbolRight) {
        this.symbolRight.setText(symbolRight);
    }

    public void setNameLeft(String nameLeft) {
        this.nameLeft.setText(nameLeft);
    }

    public void setNameRight(String nameRight) {
        this.nameRight.setText(nameRight);
    }

    public void setAmountLeft(BigDecimal amountLeft) {
        this.amountLeft.setText(BASE_CURRENCY_FORMAT.format(amountLeft.floatValue()));
    }

    public void setAmountRight(BigDecimal amountRight) {
        this.amountRight.setText(TARGET_CURRENCY_FORMAT.format(amountRight.floatValue()));
    }

    public void setDate(Date date) {
        this.date.setText(DateFormat.format(this.dateFormat, date));
    }

    public void setSource(String source) {
        this.source.setText(source);
    }
}
