package com.bytebuilding.affairmanager.utils;

import com.bytebuilding.affairmanager.model.realm.User;
import com.bytebuilding.affairmanager.model.realm.UserAffair;

public interface FirebaseHelper {

    void saveUserToFirebase(User user);

    void saveAffairToFireBase(UserAffair userAffair);

}
