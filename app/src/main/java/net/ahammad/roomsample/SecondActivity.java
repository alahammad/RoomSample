package net.ahammad.roomsample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by alahammad on 10/4/17.
 */

public class SecondActivity extends AppCompatActivity {

    Button mAsyncTask;
    Button mRxJava;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mAsyncTask = (Button)findViewById(R.id.button);
        mRxJava = (Button)findViewById(R.id.button2);

        mAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runAsyncTask();
            }
        });
        mRxJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runRxJava();
            }
        });
    }

    private void runRxJava() {
        Observable.fromCallable(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                Log.d("activity","onBackGround!!");
                return "";
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("activity","onSubscribe");
            }

            @Override
            public void onNext(Object aVoid) {
                Log.d("activity","onNext");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("activity","onError :: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("activity","onComplete");
            }
        });
    }

    private void runAsyncTask() {
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                Log.d("activity","onBackGround!!");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d("activity","onPostExecute !!");
            }
        }.execute();
    }
}
