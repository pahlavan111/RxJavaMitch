package com.bp.rxjavamitch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "bpf";

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);

//        playPart1();
//        playPart2();
        playPart3();

    }

    private void playPart3() {

        Observable<Integer> integerObservable = Observable
                .range(0, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        integerObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onN S");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext: " + integer);
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "on E ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "on C ");
            }
        });
    }

    // Flowables
    private void playPart2() {
        Flowable<Integer> integerFlowable = Flowable.range(1, 10)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());

        integerFlowable.subscribe(new FlowableSubscriber<Integer>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {
                Log.d(TAG, "onSubscribe: " + s.toString());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer.toString());
                Toast.makeText(MainActivity.this, integer + "", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called ");
            }
        });


    }

    //observer and observable
    void playPart1() {
        // create an observable object
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTaskList())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(@NonNull Task task) throws Exception {
                        Log.d(TAG, "test: " + Thread.currentThread().getName());
                        Thread.sleep(2000);

                        return task.isComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

                Log.d(TAG, "onSubscribe: called");
            }

            @Override
            public void onNext(@NonNull Task task) {

                Log.d(TAG, "onNext: " + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + task.getDescription());

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onComplete() {

                Log.d(TAG, "onComplete: called");
            }
        });

    }
}