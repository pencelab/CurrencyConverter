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

}
