package com.example.wpiclubs.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.lang.Object;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wpiclubs.MainActivity;
import com.example.wpiclubs.R;

import com.example.wpiclubs.ui.login.LoginViewModel;
import com.example.wpiclubs.ui.login.LoginViewModelFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        ImageView wpi_logo=(ImageView)findViewById(R.id.wpi_logo);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);






        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());



            }
        });

        wpi_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wpi="http://www.wpi.edu";
                Uri address=Uri.parse(wpi);

                Intent goToWPI=new Intent(Intent.ACTION_VIEW, address);

                if (goToWPI.resolveActivity(getPackageManager())!=null){
                    startActivity(goToWPI);
                }
            }
        });


    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//        login();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

//    public void logOut(View view){
//        FirebaseAuth.getInstance().signOut();
//    }
//    public void login(){
//        OAuthProvider.Builder provider = OAuthProvider.newBuilder("microsoft.com");
//
//        // Force re-consent.
//        provider.addCustomParameter("prompt", "consent");
//
//// Target specific email with login hint.
//        provider.addCustomParameter("login_hint", "user@wpi.edu");
////
//////        Task<AuthResult> pendingResultTask = FirebaseAuth.getPendingAuthResult();
////        if (pendingResultTask != null) {
////            // There's something already here! Finish the sign-in for your user.
////            pendingResultTask
////                    .addOnSuccessListener(
////                            new OnSuccessListener<AuthResult>() {
////                                @Override
////                                public void onSuccess(AuthResult authResult) {
////                                    // User is signed in.
////                                    // IdP data available in
////                                    // authResult.getAdditionalUserInfo().getProfile().
////                                    // The OAuth access token can also be retrieved:
////                                    // authResult.getCredential().getAccessToken().
////                                }
////                            })
////                    .addOnFailureListener(
////                            new OnFailureListener() {
////                                @Override
////                                public void onFailure(@NonNull Exception e) {
////                                    // Handle failure.
////                                }
////                            });
////        } else {
////            FirebaseAuth
////                    .startActivityForSignInWithProvider(/* activity= */ this, provider.build())
////                    .addOnSuccessListener(
////                            new OnSuccessListener<AuthResult>() {
////                                @Override
////                                public void onSuccess(AuthResult authResult) {
////                                    // User is signed in.
////                                    // IdP data available in
////                                    // authResult.getAdditionalUserInfo().getProfile().
////                                    // The OAuth access token can also be retrieved:
////                                    // authResult.getCredential().getAccessToken().
////                                }
////                            })
////                    .addOnFailureListener(
////                            new OnFailureListener() {
////                                @Override
////                                public void onFailure(@NonNull Exception e) {
////                                    // Handle failure.
////                                }
////                            });
////        }
//    }
}
