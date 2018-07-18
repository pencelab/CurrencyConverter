package com.pencelab.currencyconverter.model.db.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


/*
Table CurrencyConversion

INTEGER _id -> Auto-generated Primary Key
TEXT base_symbol -> Foreign Key from Currency
TEXT target_symbol -> Foreign Key from Currency
REAL value
INTEGER timestamp

*/

@Entity(tableName = "CurrencyConversion",
            foreignKeys = {
                @ForeignKey(
                        entity = Currency.class,
                        parentColumns = {"symbol", "symbol"},
                        childColumns = {"base_symbol", "target_symbol"},
                        onDelete = ForeignKey.CASCADE
                )
            }
        )
public class CurrencyConversion {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private Integer id;

    @NonNull
    @ColumnInfo(name = "base_symbol")
    private final String baseSymbol;

    @NonNull
    @ColumnInfo(name = "target_symbol")
    private final String targetSymbol;

    @NonNull
    @ColumnInfo(name = "value")
    private final BigDecimal value;

    @NonNull
    @ColumnInfo(name = "date")
    private final Date date;

    public CurrencyConversion(@NonNull String baseSymbol, @NonNull String targetSymbol, @NonNull BigDecimal value, @NonNull Date date) {
        this.baseSymbol = baseSymbol;
        this.targetSymbol = targetSymbol;
        this.value = value;
        this.date = date;
    }

    @Ignore
    public CurrencyConversion(@NonNull Integer id, @NonNull String baseSymbol, @NonNull String targetSymbol, @NonNull BigDecimal value, @NonNull Date date) {
        this.id = id;
        this.baseSymbol = baseSymbol;
        this.targetSymbol = targetSymbol;
        this.value = value;
        this.date = date;
    }

    public Integer getId() {
        return this.id;
    }

    @NonNull
    public String getBaseSymbol() {
        return this.baseSymbol;
    }

    @NonNull
    public String getTargetSymbol() {
        return this.targetSymbol;
    }

    @NonNull
    public BigDecimal getValue() {
        return this.value;
    }

    @NonNull
    public Date getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        if(this.id == null)
            return "Base Symbol: " + this.baseSymbol + " | Target Symbol: " + this.targetSymbol + " | Value: " + this.value + " | Date: " + this.date;
        else
            return "ID: " + this.id + " | Base Symbol: " + this.baseSymbol + " | Target Symbol: " + this.targetSymbol + " | Value: " + this.value + " | Date: " + this.date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyConversion that = (CurrencyConversion) o;

        if (!this.baseSymbol.equals(that.baseSymbol)) return false;
        else if(!this.targetSymbol.equals(that.targetSymbol)) return false;
        return this.value == that.value;
    }

    //Classic way
    /*@Override
    public int hashCode() {
        int result = this.baseSymbol.hashCode();
        result = 31 * result + this.targetSymbol.hashCode();
        result = 31 * result + ("" + this.value).hashCode();
        return result;
    }*/

    //JDK 7 way
    @Override
    public int hashCode() {
        return Objects.hash(this.baseSymbol, this.targetSymbol, this.value);
    }
}
