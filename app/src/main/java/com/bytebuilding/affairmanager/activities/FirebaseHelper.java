package com.bytebuilding.affairmanager.activities;

import com.bytebuilding.affairmanager.model.realm.User;

/**
 * Created by atlas on 24.03.17.
 */

public interface FirebaseHelper {

    // TODO: 10.04.17 add method for checking already registrated user

    void saveUserToFirebase(User user);

}
