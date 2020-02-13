package com.example.linesample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.LoginDelegate;
import com.linecorp.linesdk.LoginListener;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;
import com.linecorp.linesdk.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private Button btn_login;

//    private LoginDelegate loginDelegate = LoginDelegate.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.button);
        btn_login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try{
                    // App-to-app login
                    Intent loginIntent = LineLoginApi.getLoginIntent(
                            view.getContext(),
                            Constants.CHANNEL_ID,
                            new LineAuthenticationParams.Builder()
                                    .scopes(Arrays.asList(Scope.PROFILE))
                                    // .nonce("<a randomly-generated string>") // nonce can be used to improve security
                                    .build());
                    startActivityForResult(loginIntent, REQUEST_CODE);

                }
                catch(Exception e) {
                    Log.e("ERROR", e.toString());
                }
            }
        });


//        LoginButton loginButton = findViewById(R.id.line_login_btn);
//        loginButton.setChannelId(Constants.CHANNEL_ID);
//        loginButton.enableLineAppAuthentication(true);
//        // set up required scopes and nonce.
//        loginButton.setAuthenticationParams(new LineAuthenticationParams.Builder()
//                .scopes(Arrays.asList(Scope.PROFILE))
//                // .nonce("<a randomly-generated string>") // nonce can be used to improve security
//                .build()
//        );
//        loginButton.setLoginDelegate(loginDelegate);
//        loginButton.addLoginListener(new LoginListener() {
//            @Override
//            public void onLoginSuccess(@NonNull LineLoginResult result) {
//                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onLoginFailure(@Nullable LineLoginResult result) {
//                Toast.makeText(getApplicationContext(), "Login failure", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE) {
            Log.e("ERROR", "Unsupported Request");
            return;
        }

        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);

        switch (result.getResponseCode()) {

            case SUCCESS:
                // Login successful
                String accessToken = result.getLineCredential().getAccessToken().getTokenString();

                Intent transitionIntent = new Intent(this, LandingPage.class);
                transitionIntent.putExtra("line_profile", result.getLineProfile());
                transitionIntent.putExtra("line_credential", result.getLineCredential());
                transitionIntent.putExtra("display_name", result.getLineProfile().getDisplayName());
                transitionIntent.putExtra("user_id", result.getLineProfile().getUserId());
                transitionIntent.putExtra("token", accessToken);
                startActivity(transitionIntent);
                break;

            case CANCEL:
                // Login canceled by user
                Log.e("ERROR", "LINE Login Canceled by user.");
                break;

            default:
                // Login canceled due to other error
                Log.e("ERROR", "Login FAILED!");
                Log.e("ERROR", result.getErrorData().toString());
        }
    }

}
