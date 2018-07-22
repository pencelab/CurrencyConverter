package com.pencelab.currencyconverter.common.file;

import android.content.Context;

import com.pencelab.currencyconverter.common.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

public class TextFileAssetReaderObservableTransformer<R> implements ObservableTransformer<R, String> {

    private final Context context;
    private final String filename;

    private TextFileAssetReaderObservableTransformer(String filename, Context context) {
        this.context = context;
        this.filename = filename;
    }

    @Override
    public ObservableSource<String> apply(Observable<R> upstream) {
        final boolean[] isEmpty = new boolean[1];
        upstream.isEmpty().subscribe(result -> isEmpty[0] = result);

        if(!isEmpty[0])
            return Observable.<String>error(new RuntimeException("Upstream is not empty!"))
                .doOnError(error -> Utils.log(error));
                //.onExceptionResumeNext(e -> {});
        else
            return this.readFromFile()
                .doOnError(error -> Utils.log(error));
                //.onExceptionResumeNext(e -> Utils.log("Error Found!"));
    }

    public Observable<String> readFromFile(){
        return Observable.<String>create(emitter -> {

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(this.context.getAssets().open(this.filename), "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    emitter.onNext(line);
                }
            } catch (UnsupportedEncodingException e) {
                emitter.onError(e);
            } catch (FileNotFoundException e){
                emitter.onError(e);
            } catch (IOException e) {
                emitter.onError(e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                emitter.onComplete();
            }

        }).subscribeOn(Schedulers.io());
    }

    public static TextFileAssetReaderObservableTransformer readFromAssetTextFile(String filename, Context context){
        return new TextFileAssetReaderObservableTransformer(filename, context);
    }
}
