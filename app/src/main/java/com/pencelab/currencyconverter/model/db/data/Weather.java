package com.pencelab.currencyconverter.model.db.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity(tableName = "Weather")
public class Weather {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private Integer id;

    @NonNull
    @ColumnInfo(name = "state")
    private final String state;

    @NonNull
    @ColumnInfo(name = "location")
    private final String location;

    @NonNull
    @ColumnInfo(name = "degrees")
    private final double degrees;

    @NonNull
    @ColumnInfo(name = "datetime")
    private final Date datetime;

    public Weather(@NonNull String state, @NonNull String location, @NonNull double degrees, @NonNull Date datetime) {
        this.state = state;
        this.location = location;
        this.degrees = degrees;
        this.datetime = datetime;
    }

    @Ignore
    public Weather(@NonNull Integer id, @NonNull String state, @NonNull String location, @NonNull double degrees, @NonNull Date datetime) {
        this.id = id;
        this.state = state;
        this.location = location;
        this.degrees = degrees;
        this.datetime = datetime;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public String getState() {
        return state;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    @NonNull
    public double getDegrees() {
        return degrees;
    }

    @NonNull
    public Date getDatetime() {
        return datetime;
    }

    @Override
    public String toString() {
        if(id != null)
            return "ID: " + this.id + " | State: " + this.state + " | Location: " + this.location + " | Degrees: " + this.degrees + " | Datetime: " + this.datetime;
        else
            return "State: " + this.state + " | Location: " + this.location + " | Degrees: " + this.degrees + " | Datetime: " + this.datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather that = (Weather) o;

        if (!this.state.equals(that.state)) return false;
        else if(!this.location.equals(that.location)) return false;
        return this.degrees == that.degrees;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.state, this.location, this.degrees);
    }
}
