package com.pencelab.currencyconverter.model.db.data;

/*

Table Currency

TEXT symbol -> Primary Key
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
    @ColumnInfo(name = "symbol")
    private final String symbol;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @NonNull
    @ColumnInfo(name = "location")
    private final String location;

    public Currency(@NonNull String symbol, @NonNull String name, @NonNull String location) {
        this.symbol = symbol;
        this.name = name;
        this.location = location;
    }

    @NonNull
    public String getSymbol() {
        return this.symbol;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getLocation() {
        return this.location;
    }

    @Override
    public String toString() {
        return "Symbol: " + this.symbol + " | Name: " + this.name + " | Location: " + this.location;
    }

}
