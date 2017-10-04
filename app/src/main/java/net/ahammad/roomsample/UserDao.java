package net.ahammad.roomsample;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by alahammad on 10/2/17.
 */

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    Maybe<List<User>> getAll();

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    Flowable<List<User>> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Query("SELECT * FROM users where uid = :id")
    Maybe<User> findById(int id);


    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Update
    public void updateUsers(User... users);
}
