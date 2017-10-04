package net.ahammad.roomsample;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by alahammad on 10/4/17.
 */


public class LocalCacheManager {
    private static final String DB_NAME = "database-name";
    private Context context;
    static LocalCacheManager _instance;

    public static LocalCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new LocalCacheManager(context);
        }
        return _instance;
    }

    public LocalCacheManager(Context context) {
        this.context = context;
    }

    public void getUsers(final DatabaseCallback databaseCallback) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
        db.userDao().getAll().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<User>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<User> users) throws Exception {
                if (users != null) {
                    databaseCallback.onUsersLoaded(users);
                }
            }
        });
    }

    public void addUser(final DatabaseCallback databaseCallback, final String firstName, final String lastName) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                User user = new User(firstName, lastName);
                AppDatabase db = Room.databaseBuilder(context,
                        AppDatabase.class, DB_NAME).build();
                db.userDao().insertAll(user);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                databaseCallback.onUserAdded();
            }

            @Override
            public void onError(Throwable e) {
                databaseCallback.onDataNotAvailable();
            }
        });
    }

    public void deleteUser(final DatabaseCallback databaseCallback, final User user) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
                db.userDao().delete(user);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                databaseCallback.onUserDeleted();
            }

            @Override
            public void onError(Throwable e) {
                databaseCallback.onDataNotAvailable();
            }
        });
    }


    public void updateUser(final DatabaseCallback databaseCallback, final User user){
        user.setFirstName("first name first name");
        user.setLastName("last name last name");
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
                db.userDao().updateUsers(user);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                databaseCallback.onUserUpdated();
            }

            @Override
            public void onError(Throwable e) {
                databaseCallback.onDataNotAvailable();
            }
        });
    }
}
