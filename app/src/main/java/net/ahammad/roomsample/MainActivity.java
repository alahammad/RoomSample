package net.ahammad.roomsample;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements DatabaseCallback {
    private static final String DB_NAME = "database-name";

    EditText firstName;
    EditText lastName;
    RecyclerView recyclerView;
    UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstName = (EditText) findViewById(R.id.et_firstname);
        lastName = (EditText) findViewById(R.id.et_lastname);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

    }

    public void add(View view) {
        if (!TextUtils.isEmpty(firstName.getText().toString()) && !TextUtils.isEmpty(lastName.getText().toString())) {
            LocalCacheManager.getInstance(this).addUser(this, firstName.getText().toString(), lastName.getText().toString());
        }
    }

    public void fetch(View view) {
        LocalCacheManager.getInstance(this).getUsers(this);
    }


    public void update(View view) {
        getUser(1, false);
    }

    private void getUser(int id, final boolean delete) {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DB_NAME).build();
        db.userDao().findById(1).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<User>() {

            @Override
            public void accept(@io.reactivex.annotations.NonNull User user) throws Exception {
                if (user != null)
                    if (!delete)
                        LocalCacheManager.getInstance(MainActivity.this).updateUser(MainActivity.this, user);
                    else
                        LocalCacheManager.getInstance(MainActivity.this).deleteUser(MainActivity.this, user);
            }
        });
    }


    public void Delete(View view) {
        getUser(1, true);
    }

    @Override
    public void onUsersLoaded(List<User> users) {
        adapter = new UsersAdapter(users, MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    public void onUserDeleted() {
        Log.d("room", "onUserDeleted");
    }

    @Override
    public void onUserAdded() {
        Log.d("room", "onUserAdded");
    }

    @Override
    public void onDataNotAvailable() {
        Log.d("room", "onDataNotAvailable");
    }

    @Override
    public void onUserUpdated() {
        Log.d("room", "onUserUpdated");
    }

}
