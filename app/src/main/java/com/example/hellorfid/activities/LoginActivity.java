package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.dump.ApiCallBack;
import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity implements ApiCallBack.ApiCallback {

    private static final String TAG = "LoginActivity";
    private SessionManager sessionManager;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sessionManager = new SessionManager(this);
        mainHandler = new Handler(Looper.getMainLooper());

        EditText usernameInput = findViewById(R.id.username);
        EditText passwordInput = findViewById(R.id.password);
        ImageView settingIcon = findViewById(R.id.setting_icon);

        if (sessionManager.getToken() != null) {
            System.out.println("call --- login --- intent");
            startHomeActivity();
            return;
        }

        Button loginButton = findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (username.isEmpty()) {
                    showToast("Please enter username");
                    return;
                } else if (password.isEmpty()) {
                    showToast("Please enter password");
                    return;
                } else {
                    JSONObject loginJson = new JSONObject();
                    try {
                        loginJson.put("username", username);
                        loginJson.put("password", password);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("Error creating JSON");
                        return;
                    }
                    Log.d(TAG, "Sending login request with JSON: " + loginJson.toString());
                    ApiCallBack apiCallBack = new ApiCallBack(LoginActivity.this);
                    String login = Constants.login;
                    apiCallBack.login(login, loginJson, LoginActivity.this);
                }
            }
        });

        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSuccess(JSONObject responseJson) {
        String result = responseJson.toString();
        Log.d(TAG, "Login successful. Response: " + result);

        String token = responseJson.optString("jwtToken");
        sessionManager.setToken(token);
        System.out.println("token from login: " + token);

        try {
            decodeJWTWithoutVerification(token);
        } catch (Exception e) {
            Log.e(TAG, "Error decoding JWT", e);
            showToast("Error decoding user info");
        }

        mainHandler.post(() -> {
            showToast("Login Successful");
            startHomeActivity();
        });
    }

    @Override
    public void onFailure(Exception e) {
        Log.e(TAG, "Login failed. Error: " + e.getMessage(), e);
        showToast("Login Failed: " + e.getMessage());
    }

    private void decodeJWTWithoutVerification(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length != 3) {
                Log.e(TAG, "Invalid JWT format");
                return;
            }

            String payload = getJson(parts[1]);

            Log.d(TAG, "JWT Payload: " + payload);

            JSONObject claims = new JSONObject(payload);
            String sub = claims.optString("sub", "N/A");
            String roleId = claims.optJSONObject("roleId").optString("roleName", "N/A");

            Log.d(TAG, "Subject: " + sub);
            Log.d(TAG, "Role: " + roleId);

            // Store payload and role in session
            sessionManager.setPayload(payload);
            sessionManager.setRole(roleId);

            final String welcomeMessage = "Welcome, " + sub;
            showToast(welcomeMessage);

        } catch (Exception e) {
            Log.e(TAG, "Error decoding JWT", e);
            throw new RuntimeException("Error decoding JWT", e);
        }
    }

    private String getJson(String strEncoded) throws Exception {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    private void showToast(final String message) {
        mainHandler.post(() -> Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}