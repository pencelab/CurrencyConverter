package com.pencelab.currencyconverter.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pencelab.currencyconverter.R;
import com.pencelab.currencyconverter.model.db.data.CurrencyConversion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CurrencyConversionDataAdapter extends RecyclerView.Adapter<CurrencyConversionViewHolder> {

    public interface ClickListener{
        void onItemClicked(CurrencyConversion cc);
    }

    private ClickListener clickListener;

    private final List<CurrencyConversion> data = new ArrayList<>();

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
        CurrencyConversion currencyConversion = data.get(position);
        holder.setSymbolLeft(currencyConversion.getBaseCode());
        holder.setSymbolRight(currencyConversion.getTargetCode());

        holder.setLocationLeft(currencyConversion.getBaseCode());
        holder.setLocationRight(currencyConversion.getBaseCode());

        holder.setAmountLeft(new BigDecimal(1));
        holder.setAmountRight(currencyConversion.getValue());
        holder.setDate(currencyConversion.getDate());
        holder.setSource(currencyConversion.getSource());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public boolean contains(CurrencyConversion currencyConversion){
        return this.data.contains(currencyConversion);
    }

    public int getPosition(CurrencyConversion currencyConversion){
        return this.data.indexOf(currencyConversion);
    }

    public boolean add(CurrencyConversion currencyConversion){
        int index = this.getPosition(currencyConversion);

        if(index < 0){
            this.data.add(0, currencyConversion);
            this.notifyItemInserted(0);
            return true;
        }

        this.data.set(index, currencyConversion);
        this.notifyItemChanged(index);

        return false;
    }
}
