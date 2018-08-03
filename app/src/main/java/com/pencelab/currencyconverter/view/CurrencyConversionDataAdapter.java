package com.pencelab.currencyconverter.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pencelab.currencyconverter.R;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversionPlus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CurrencyConversionDataAdapter extends RecyclerView.Adapter<CurrencyConversionViewHolder> {

    public interface ClickListener{
        void onItemClicked(CurrencyConversionPlus ccp);
    }

    private ClickListener clickListener;

    private final List<CurrencyConversionPlus> data = new ArrayList<>();

    public CurrencyConversionDataAdapter(){
        super();
    }

    public CurrencyConversionDataAdapter(ClickListener clickListener){
        super();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CurrencyConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_conversion_item, parent, false);
        CurrencyConversionViewHolder viewHolder = new CurrencyConversionViewHolder(view);
        if(this.clickListener != null) {
            view.setOnClickListener(v -> clickListener.onItemClicked(data.get(viewHolder.getAdapterPosition())));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyConversionViewHolder holder, int position) {
        CurrencyConversionPlus currencyConversionPlus = data.get(position);
        holder.setSymbolLeft(currencyConversionPlus.getBaseCode());
        holder.setNameLeft(currencyConversionPlus.getBaseCurrencyName());
        holder.setAmountLeft(new BigDecimal(1));
        holder.setSymbolRight(currencyConversionPlus.getTargetCode());
        holder.setNameRight(currencyConversionPlus.getTargetCurrencyName());
        holder.setAmountRight(currencyConversionPlus.getValue());
        holder.setDate(currencyConversionPlus.getDate());
        holder.setSource(currencyConversionPlus.getSource());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public boolean contains(CurrencyConversionPlus currencyConversionPlus){
        return this.data.contains(currencyConversionPlus);
    }

    public int getPosition(CurrencyConversionPlus currencyConversionPlus){
        return this.data.indexOf(currencyConversionPlus);
    }

    public boolean add(CurrencyConversionPlus currencyConversionPlus){
        int index = this.getPosition(currencyConversionPlus);

        if(index < 0){
            this.data.add(0, currencyConversionPlus);
            this.notifyItemInserted(0);
            return true;
        }

        this.data.set(index, currencyConversionPlus);
        this.notifyItemChanged(index);

        return false;
    }
}
