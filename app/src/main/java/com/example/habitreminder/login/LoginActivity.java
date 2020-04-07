package com.example.habitreminder.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.habitreminder.OnboardingPackage.OnboardPreferenceManager;
import com.example.habitreminder.R;
import com.example.habitreminder.userhome.UserDashboardActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.type.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class LoginActivity extends AppCompatActivity {

    private boolean emailCheck, passwordCheck;
    private FirebaseAuth mAuth;
    private String userEmail, userPassword;
    private OnboardPreferenceManager prefManager;
    private Context _context;
    private Map<String, Object> userData = new HashMap<>();
    private FirebaseFirestore db;
    private CallbackManager callbackManager;
    private LatLng myLatLng;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _context = this;
        prefManager = new OnboardPreferenceManager(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.fullyInitialize();
        callbackManager = CallbackManager.Factory.create();

        TextView goBack = findViewById(R.id.header_login_back);
        ImageButton google_login = findViewById(R.id.google_sign_in);
        ImageButton facebook_login = findViewById(R.id.facebook_sign_in);
        final EditText email = findViewById(R.id.email_input);
        final EditText password = findViewById(R.id.password_input);
        Button next = findViewById(R.id.next);
        Log.d("MAUTH", "onCreate: " + mAuth);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailCheck && passwordCheck) {
//                    Log.d("MAUTH", "onCreate: " + userEmail + " - " + userPassword);
                    mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (mAuth.getUid() != null) {
                                            db.collection("users").document(mAuth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                    prefManager.setUserData(documentSnapshot.get("name").toString(), documentSnapshot.get("email").toString(), documentSnapshot.get("account_type").toString());
                                                    redirectToHome();
                                                    finish();
                                                }
                                            });
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Authentication failed!.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()) {
                    email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                    emailCheck = true;
                    userEmail = charSequence.toString();
                } else {
                    emailCheck = false;
                    email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass = charSequence.toString().trim();
                if (pass.length() >= 8 && pass.matches(".*\\d.*")) {
                    password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                    passwordCheck = true;
                    userPassword = charSequence.toString();
                } else {
                    passwordCheck = false;
                    password.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(_context, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_CODE);
            }
        });

        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(_context, "ok", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();
                            String userID_for_facebook = profile.getId();
                            Log.i("userID_for_facebook", userID_for_facebook);
                            saveUserData(id, email, firstName + lastName, "FACEBOOK");
                            prefManager.setFacebookSignIn(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Some thing went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("userID_for_google ", "userID_for_google");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String userID_for_google = account.getId();
                Log.i("userID_for_google ", userID_for_google);
                saveUserData(account.getId(), account.getEmail(), account.getDisplayName(), "GOOGLE");
                prefManager.setGoogleSignIn(true);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Some thing went wrong  " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("userID_for_google", String.valueOf(e));
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void saveUserData(final String id, final String email, final String name, final String type) {
        DocumentReference userRef = db.collection("users").document(id);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        redirectToHome();
                    } else {
                        saveGoogleFacebookSigninUser(id, email, name, type);
                    }
                } else {
                    saveGoogleFacebookSigninUser(id, email, name, type);
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveGoogleFacebookSigninUser(final String id, final String email, final String name, final String type) {
        userData.put("email", email);
        userData.put("name", name);
        userData.put("user_id", id);
        userData.put("created_at", new Date());
        userData.put("account_type", type);
        db.collection("users").document(id).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        redirectToHome();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


    private void redirectToHome() {
        prefManager.setUserLogin(true);
        startActivity(new Intent(_context, UserDashboardActivity.class));
        finish();
    }
}
