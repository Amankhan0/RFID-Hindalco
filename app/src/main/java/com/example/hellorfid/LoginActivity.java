package com.example.hellorfid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements ApiCallBack.ApiCallback {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        EditText usernameInput = findViewById(R.id.username);
        EditText passwordInput = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create JSON object with login credentials

                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (username.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    JSONObject loginJson = new JSONObject();
                    try {
                        loginJson.put("username", username);
                        loginJson.put("password", password);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error creating JSON", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Log the JSON object being sent
                    Log.d(TAG, "Sending login request with JSON: " + loginJson.toString());


                    // Make the API call
                    ApiCallBack apiCallBack = new ApiCallBack();
                    String url = "http://137.184.74.218/auth/login"; // Replace with your actual URL
                    apiCallBack.login(url, loginJson, LoginActivity.this);
                }

            }
        });
    }

    @Override
    public void onSuccess(JSONObject responseJson) {
        String result = responseJson.toString();

        // Log the successful response
        Log.d(TAG, "Login successful. Response: " + result);

        // Handle successful response
        runOnUiThread(() -> {
            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            // You can start a new activity or handle the response as needed
        });
    }

    @Override
    public void onFailure(Exception e) {
        // Log the error
        Log.e(TAG, "Login failed. Error: " + e.getMessage(), e);

        // Handle failure response
        runOnUiThread(() -> {
            Toast.makeText(LoginActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
