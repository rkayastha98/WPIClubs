package com.example.wpiclubs;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth= FirebaseAuth.getInstance();

        Button fab = findViewById(R.id.login);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login(){

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("microsoft.com");
        System.out.println("==============================Signing with microsoft========================");

        byte[] sha1 = {(byte)0xC0,(byte)0xEB,0x1D,(byte)0xDF,(byte)0xCE,0x08,(byte)0x94,(byte)0xE2,(byte)0xCE,(byte)0x88,0x4C,(byte)0xB1,(byte)0xF7,(byte)0xBE,(byte)0xE9,(byte)0x8C,(byte)0xF3,0x69,(byte)0xA2,(byte)0xDD};
        Log.e("keyhash", Base64.encodeToString(sha1, Base64.NO_WRAP));

        // Force re-consent.
        provider.addCustomParameter("prompt", "consent");

        // Optional "tenant" parameter in case you are using an Azure AD tenant.
// eg. '8eaef023-2b34-4da1-9baa-8bc8c9d6a490' or 'contoso.onmicrosoft.com'
// or "common" for tenant-independent tokens.
// The default value is "common".
        provider.addCustomParameter("tenant", "589c76f5-ca15-41f9-884b-55ec15a0672a");

        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("calendars.read");
                    }
                };
        provider.setScopes(scopes);

        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    System.out.println("=============================Pending Success===========================");
                                    System.out.println("===================================="+authResult.getAdditionalUserInfo().getProfile()+"============================");

                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("Pending Failed: "+e.getMessage());
                                    // Handle failure.
                                }
                            });
        } else {
            System.out.println("===================================No Pending===========================");
            firebaseAuth
                    .startActivityForSignInWithProvider(this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    System.out.println("================================Auth Success==========================");

                                    System.out.println("===================================="+authResult.getAdditionalUserInfo().getProfile()+"============================");
                                    // User is signed in.
                                    // IdP data available in
                                    // .
                                    // The OAuth access token can also be retrieved:
                                    // .
//                                System.out.println( authResult.getCredential().getAccessToken());
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("====================================================================Auth Failed: "+e.getMessage() + "\nTrace: "+ e.getClass().getCanonicalName());
                                    // Handle failure.
                                }
                            });

        }



    }
}
