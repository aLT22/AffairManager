package com.bytebuilding.affairmanager.database.realm;

import com.bytebuilding.affairmanager.model.realm.UserAffair;
import com.bytebuilding.affairmanager.model.realm.UserGroup;

import java.util.Collection;

import io.realm.Realm;

/**
 * Created by Turkin A. on 06.05.17.
 */

public class UserGroupsRealmHelper {

    public static void addUserGroupAsync(Realm realm, final UserGroup userGroup) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(userGroup);
            }
        });
    }

    public static void deleteUserGroupAsync(Realm realm, final long timestamp) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserGroup.delete(realm, timestamp);
            }
        });
    }

    public static void deleteAllGroupsAsync(Realm realm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    public static void deleteUserGroupsAsync(Realm realm, Collection<Long> ids) {
        final Long[] idsToDelete = new Long[ids.size()];

        ids.toArray(idsToDelete);

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Long id:
                        idsToDelete){
                    UserGroup.delete(realm, id);
                }
            }
        });
    }

}
