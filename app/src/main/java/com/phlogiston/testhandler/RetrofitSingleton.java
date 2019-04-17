package com.phlogiston.testhandler;

import android.graphics.ColorSpace;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class RetrofitSingleton {
    private static final String TAG = RetrofitSingleton.class.getSimpleName();

    private static Observable<ArrayList<ColorSpace.Model>> observableRetrofit;
    private static BehaviorSubject<ArrayList<ColorSpace.Model>> observableModelsList;
    private static Subscription subscription;

    private RetrofitSingleton() {
    }

    public static void init() {
        Log.d(TAG, "init");

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();

        GetModels apiService = retrofit.create(GetModels.class);

        observableRetrofit = apiService.getModelsList();
    }

    public static void resetModelsObservable() {
        observableModelsList = BehaviorSubject.create();

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = observableRetrofit.subscribe(new Subscriber<ArrayList<ColorSpace.Model>>() {
            @Override
            public void onCompleted() {
                //do nothing
            }

            @Override
            public void onError(Throwable e) {
                observableModelsList.onError(e);
            }

            @Override
            public void onNext(ArrayList<ColorSpace.Model> models) {
                observableModelsList.onNext(models);
            }
        });
    }

    public static Observable<ArrayList<ColorSpace.Model>> getModelsObservable() {
        if (observableModelsList == null) {
            resetModelsObservable();
        }
        return observableModelsList;
    }
}
