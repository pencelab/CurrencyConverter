package com.pencelab.currencyconverter.model.db.data;

/*

Table Currency

TEXT code -> Primary Key
TEXT name
TEXT location

*/

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

@Entity(tableName = "Currency")
public class Currency {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "code")
    private final String code;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @NonNull
    @ColumnInfo(name = "symbol")
    private final String symbol;

    public Currency(@NonNull String code, @NonNull String name, @NonNull String symbol) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
    }

    @NonNull
    public String getCode() {
        return this.code;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public String toString() {
        return "Code: " + this.code + " | Name: " + this.name + " | Symbol: " + this.symbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Currency that = (Currency) obj;

        if (!this.code.equals(that.code)) return false;
        else if(!this.name.equals(that.name)) return false;
        return this.symbol.equals(that.symbol);
    }

    //JDK 7 way
    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.name, this.symbol);
    }
}
