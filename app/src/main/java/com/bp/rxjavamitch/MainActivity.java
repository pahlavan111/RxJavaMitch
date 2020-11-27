package com.bp.rxjavamitch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "bp";

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        
        playPart1();
        playPart2();
        

    }

    // Flowables
    private void playPart2() {

    }

    //observer and observable
    void playPart1(){
        // create an observable object
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTaskList())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(@NonNull Task task) throws Exception {
                        Log.d(TAG,"test: "+Thread.currentThread().getName());
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
                Log.d(TAG, "onError: "+e);
            }

            @Override
            public void onComplete() {

                Log.d(TAG, "onComplete: called");
            }
        });

    }
}