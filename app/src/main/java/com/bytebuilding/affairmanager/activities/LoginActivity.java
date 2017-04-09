package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.model.realm.User;
import com.bytebuilding.affairmanager.utils.AffairManagerApplication;
import com.bytebuilding.affairmanager.utils.CryptoUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmObject;

import static com.bytebuilding.affairmanager.activities.SignUpActivity.FIREBASE_DATABASE_URL;

public class LoginActivity extends AppCompatActivity implements FirebaseHelper{

    DatabaseReference rootReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl(FIREBASE_DATABASE_URL);
    DatabaseReference userReference = rootReference.child("users");

    public static final String DEFAULT_VK_PASS = "vkpass";

    @BindView(R.id.btn_sign_up) Button btnSignUp;
    @BindView(R.id.btn_sign_in) Button btnSignIn;
    @BindView(R.id.btn_sign_in_vk) Button btnSignInVk;
    @BindView(R.id.btn_sign_in_google) Button btnSignInGoogle;
    @BindView(R.id.btn_sign_in_facebook) Button btnSignInFacebook;

    private Unbinder unbinder;

    private Realm realm;

    private SharedPreferences preferences = null;

    private String[] scopes = new String[]{
            VKScope.EMAIL, VKScope.WALL
    };
    private boolean isCancelled = false;
    private boolean isAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        preferences = getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE);
    }

    @OnClick(R.id.btn_sign_in_vk)
    public void onSignInVkClick() {
        VKSdk.login(this, scopes);
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInClick() {
        Intent enterActivityIntent = new Intent(getApplicationContext(), EnterActivity.class);
        startActivity(enterActivityIntent);
        finish();
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUpClick() {
        Intent signUpActivityIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpActivityIntent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        realm.close();

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался
                final String login = CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, res
                        .email);
                final String password = CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR,
                        DEFAULT_VK_PASS);

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number num = realm.where(User.class).max("userId");
                        long nextID;
                        if(num == null) {
                            nextID = 0;
                        } else {
                            nextID = num.intValue() + 1;
                        }
                        User user = realm.createObject(User.class, nextID);
                        user.setUserLogin(login);
                        user.setUserPassword(password);
                    }
                });

                preferences.edit().putString("login", login).apply();
                preferences.edit().putString("password", password).apply();

                saveUserToFirebase(realm.where(User.class).findAll().last());

                isAccepted = true;

                Intent intent = new Intent(getApplicationContext(), MainOnlineActivity.class);
                startActivity(intent);
            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                isCancelled = true;
                Toast.makeText(getApplicationContext(), getResources().getText(R.string
                        .toast_vk_autorization_decline), Toast.LENGTH_SHORT).show();
            }
        })) {}

        if (!isCancelled) {
            this.finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void saveUserToFirebase(User user) {
        userReference.child(String.valueOf(System.currentTimeMillis())).setValue(user);
    }
}
