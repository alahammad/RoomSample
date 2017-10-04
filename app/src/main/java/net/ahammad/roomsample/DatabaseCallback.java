package net.ahammad.roomsample;

import java.util.List;

/**
 * Created by alahammad on 10/4/17.
 */

public interface DatabaseCallback {

    void onUsersLoaded(List<User> users);

    void onUserDeleted();

    void onUserAdded();

    void onDataNotAvailable();

    void onUserUpdated();
}
