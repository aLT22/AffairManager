package com.bytebuilding.affairmanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.model.realm.User;
import com.bytebuilding.affairmanager.utils.AffairManagerApplication;
import com.bytebuilding.affairmanager.utils.CryptoUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmObject;

import static com.bytebuilding.affairmanager.activities.SignUpActivity.FIREBASE_DATABASE_URL;

public class LoginActivity extends AppCompatActivity implements FirebaseHelper, GoogleApiClient.OnConnectionFailedListener {

    DatabaseReference rootReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl(FIREBASE_DATABASE_URL);
    DatabaseReference userReference = rootReference.child("users");

    public static final String DEFAULT_VK_PASS = "vkpass";
    public static final int RC_SIGN_IN = 007;

    @BindView(R.id.btn_sign_up) Button btnSignUp;
    @BindView(R.id.btn_sign_in) Button btnSignIn;
    @BindView(R.id.btn_sign_in_vk) Button btnSignInVk;
    @BindView(R.id.btn_sign_in_google) Button btnSignInGoogle;
    @BindView(R.id.btn_sign_in_facebook) Button btnSignInFacebook;

    private LoginButton loginButton;

    private Unbinder unbinder;

    private Realm realm;

    private SharedPreferences preferences = null;

    private String[] scopes = new String[]{
            VKScope.EMAIL, VKScope.WALL
    };

    private boolean isCancelled = false;
    private boolean isAccepted = false;

    private CallbackManager callbackManager;

    private GoogleSignInOptions gso;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        preferences = getSharedPreferences("AffairManagerPreferences", MODE_PRIVATE);

        callbackManager = CallbackManager.Factory.create();

        registrationCallback();

        buildGoogleLogin();
    }

    private void buildGoogleLogin() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @OnClick(R.id.btn_sign_in_vk)
    public void onSignInVkClick() {
        VKSdk.login(this, scopes);

        preferences.edit().putString("type", getResources().getStringArray(R.array
                .registration_type_in_preferences)[2]).apply();
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInClick() {
        btnSignIn.setEnabled(false);

        goToEnterActivity();

        btnSignIn.setEnabled(true);
    }

    private void goToEnterActivity() {
        Intent enterActivityIntent = new Intent(getApplicationContext(), EnterActivity.class);
        startActivity(enterActivityIntent);
        finish();
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUpClick() {
        btnSignUp.setEnabled(false);

        goToSignUpActivity();

        btnSignUp.setEnabled(true);
    }

    private void goToSignUpActivity() {
        Intent signUpActivityIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpActivityIntent);
        finish();
    }

    @OnClick(R.id.btn_sign_in_facebook)
    public void onSignInFacebookClick() {
        preferences.edit().putString("type", getResources().getStringArray(R.array
                .registration_type_in_preferences)[0]).apply();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile",
                "email"));
    }

    @OnClick(R.id.btn_sign_in_google)
    public void onSignInGoogleClick() {
        preferences.edit().putString("type", getResources().getStringArray(R.array
                .registration_type_in_preferences)[1]).apply();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void registrationCallback() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    final long id = System.currentTimeMillis();
                                    final String login = CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils
                                            .VECTOR, object.getString("email"));
                                    final String password = CryptoUtils.encrypt(CryptoUtils.KEY,
                                            CryptoUtils.VECTOR, object.getString("id"));

                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            User user = realm.createObject(User.class, id);
                                            user.setUserLogin(login);
                                            user.setUserPassword(password);
                                            user.setUserOrganization("");
                                        }
                                    });

                                    setUserPreferences(id, login, password);

                                    saveUserToFirebase(realm.where(User.class).findAll().last());

                                    isAccepted = true;

                                    Intent intent = new Intent(getApplicationContext(),
                                            MainOnlineActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), getResources()
                                                    .getString(R.string.error_getting_data_from_firebase),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                isCancelled = true;
                preferences.edit().putString("type", "").apply();

                Toast.makeText(getApplicationContext(), getResources().getString(R.string
                        .toast_vk_autorization_decline), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                isCancelled = true;

                preferences.edit().putString("type", "").apply();

                Toast.makeText(getApplicationContext(), getResources().getString(R.string
                        .toast_vk_autorization_decline), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUserPreferences(long id, String login, String password) {
        preferences.edit().putLong("id", id).apply();
        preferences.edit().putString("login", login).apply();
        preferences.edit().putString("password", password).apply();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        realm.close();

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (preferences.getString("type", "").equals(getResources().getStringArray(R.array
                .registration_type_in_preferences)[0])) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (preferences.getString("type", "").equals(getResources().getStringArray(R.array
                .registration_type_in_preferences)[1])) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Signed in successfully, show authenticated UI.
                    GoogleSignInAccount acct = result.getSignInAccount();

                    final long id = System.currentTimeMillis();
                    final String login = CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, acct
                            .getEmail());
                    final String password = CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, acct
                            .getId());

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            User user = realm.createObject(User.class, id);
                            user.setUserLogin(login);
                            user.setUserPassword(password);
                            user.setUserOrganization("");
                        }
                    });

                    setUserPreferences(id, login, password);

                    saveUserToFirebase(realm.where(User.class).findAll().last());

                    isAccepted = true;

                    Intent intent = new Intent(getApplicationContext(), MainOnlineActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        } else if (preferences.getString("type", "").equals(getResources().getStringArray(R.array
                .registration_type_in_preferences)[2])) {
            if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                @Override
                public void onResult(VKAccessToken res) {
                    // Пользователь успешно авторизовался
                    final long id = System.currentTimeMillis();
                    final String login = CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR, res
                            .email);
                    final String password = CryptoUtils.encrypt(CryptoUtils.KEY, CryptoUtils.VECTOR,
                            res.userId);

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            User user = realm.createObject(User.class, id);
                            user.setUserLogin(login);
                            user.setUserPassword(password);
                            user.setUserOrganization("");
                        }
                    });

                    setUserPreferences(id, login, password);

                    saveUserToFirebase(realm.where(User.class).findAll().last());

                    isAccepted = true;

                    Intent intent = new Intent(getApplicationContext(), MainOnlineActivity.class);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onError(VKError error) {
                    // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                    isCancelled = true;
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string
                            .toast_vk_autorization_decline), Toast.LENGTH_SHORT).show();

                    preferences.edit().putString("type", "").apply();
                }
            })) {}

            if (!isCancelled) {
                this.finish();
            }
        }
    }

    @Override
    public void saveUserToFirebase(User user) {
        userReference.child(String.valueOf(user.getUserId())).setValue(user);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
