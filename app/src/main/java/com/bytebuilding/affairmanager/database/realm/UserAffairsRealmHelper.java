package com.bytebuilding.affairmanager.database.realm;

import com.bytebuilding.affairmanager.model.realm.UserAffair;

import java.util.Collection;

import io.realm.Realm;

/**
 * Created by Turkin A. on 03.05.17.
 */

public class UserAffairsRealmHelper {

    public static void addUserAffairAsync(Realm realm, final UserAffair userAffair) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(userAffair);
            }
        });
    }

    public static void deleteUserAffairAsync(Realm realm, final long timestamp) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserAffair.delete(realm, timestamp);
            }
        });
    }

    public static void deleteAllAffairsAsync(Realm realm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    public static void deleteUserAffairsAsync(Realm realm, Collection<Long> ids) {
        final Long[] idsToDelete = new Long[ids.size()];

        ids.toArray(idsToDelete);

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Long id:
                     idsToDelete){
                    UserAffair.delete(realm, id);
                }
            }
        });
    }

}
