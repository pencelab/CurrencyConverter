package com.pencelab.currencyconverter.common.file;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;

public class TextFileAssetReaderObservableSource implements ObservableSource<String> {

    private final Context context;
    private final String filename;

    private TextFileAssetReaderObservableSource(String filename, Context context) {
        this.context = context;
        this.filename = filename;
    }

    @Override
    public void subscribe(Observer<? super String> observer) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(this.context.getAssets().open(this.filename), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                observer.onNext(line);
            }
        } catch (UnsupportedEncodingException e) {
            observer.onError(e);
        } catch (FileNotFoundException e){
            observer.onError(e);
        } catch (IOException e) {
            observer.onError(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            observer.onComplete();
        }
    }

    public static TextFileAssetReaderObservableSource readFromAssetTextFile(String filename, Context context){
        return new TextFileAssetReaderObservableSource(filename, context);
    }
}
