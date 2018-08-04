package com.pencelab.currencyconverter.model.db.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.pencelab.currencyconverter.common.Utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


/*
Table CurrencyConversion

INTEGER _id -> Auto-generated Primary Key
TEXT base_code -> Foreign Key from Currency
TEXT target_code -> Foreign Key from Currency
REAL value
INTEGER timestamp
TEXT source

*/

@Entity(tableName = "CurrencyConversion",
            indices = {
                @Index(value = "base_code", unique = false),
                @Index(value = "target_code", unique = false)
            },
            foreignKeys = {
                @ForeignKey(
                        entity = Currency.class,
                        parentColumns = {"code"},
                        childColumns = {"base_code"},
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Currency.class,
                        parentColumns = {"code"},
                        childColumns = {"target_code"},
                        onDelete = ForeignKey.CASCADE
                )
            }
        )
public class CurrencyConversion {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    protected Integer id;

    @NonNull
    @ColumnInfo(name = "base_code")
    protected final String baseCode;

    @NonNull
    @ColumnInfo(name = "target_code")
    protected final String targetCode;

    @NonNull
    @ColumnInfo(name = "value")
    protected final BigDecimal value;

    @NonNull
    @ColumnInfo(name = "date")
    protected final Date date;

    @NonNull
    @ColumnInfo(name = "source")
    protected final String source;

    public CurrencyConversion(@NonNull String baseCode, @NonNull String targetCode, @NonNull BigDecimal value, @NonNull Date date, @NonNull String source) {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.value = value;
        this.date = date;
        this.source = source;
    }

    @Ignore
    public CurrencyConversion(@NonNull Integer id, @NonNull String baseCode, @NonNull String targetCode, @NonNull BigDecimal value, @NonNull Date date, @NonNull String source) {
        this.id = id;
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.value = value;
        this.date = date;
        this.source = source;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public String getBaseCode() {
        return this.baseCode;
    }

    @NonNull
    public String getTargetCode() {
        return this.targetCode;
    }

    @NonNull
    public BigDecimal getValue() {
        return this.value;
    }

    @NonNull
    public Date getDate() {
        return this.date;
    }

    @NonNull
    public String getSource() {
        return this.source;
    }

    @Override
    public String toString() {
        if(this.id == null)
            return "Base Code: " + this.baseCode + " | Target Code: " + this.targetCode + " | Value: " + this.value + " | Date: " + this.date + " | Source: " + this.source;
        else
            return "ID: " + this.id + " | Base Code: " + this.baseCode + " | Target Code: " + this.targetCode + " | Value: " + this.value + " | Date: " + this.date + " | Source: " + this.source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        //Check if 'this' and 'o' are not from the same class. If they're not, then check that 'this' is parent of 'o'. If not then returns false
        if(getClass() != o.getClass() && !getClass().isAssignableFrom(o.getClass())) return false;

        CurrencyConversion that = (CurrencyConversion) o;

        /*if (!this.baseCode.equals(that.baseCode)) return false;
        else if(!this.targetCode.equals(that.targetCode)) return false;
        return this.value.equals(that.value);*/

        if (!this.baseCode.equals(that.baseCode)) return false;
        return this.targetCode.equals(that.targetCode);
    }

    //Classic way
    /*@Override
    public int hashCode() {
        int result = this.baseCode.hashCode();
        result = 31 * result + this.targetCode.hashCode();
        result = 31 * result + ("" + this.value).hashCode();
        return result;
    }*/

    //JDK 7 way
    @Override
    public int hashCode() {
        return Objects.hash(this.baseCode, this.targetCode, this.value);
    }
}
