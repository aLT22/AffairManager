package com.bytebuilding.affairmanager.activities;

import com.bytebuilding.affairmanager.model.realm.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.bytebuilding.affairmanager.activities.SignUpActivity.FIREBASE_DATABASE_URL;

/**
 * Created by atlas on 24.03.17.
 */

public interface FirebaseHelper {

    /*DatabaseReference rootReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl(FIREBASE_DATABASE_URL);
    DatabaseReference userReference = rootReference.child("users");*/

    void saveUserToFirebase(User user);

}
