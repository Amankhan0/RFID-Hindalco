package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        TextView username = findViewById(R.id.username);
        TextView userID = findViewById(R.id.userID);
        ImageView AllScreenBackBtn = findViewById(R.id.all_screen_back_btn);
        Button logout = findViewById(R.id.logout);
        sessionManager = new SessionManager(this);


        try {
            JSONObject claims = new JSONObject(sessionManager.getPayload());
            System.out.println("sessionManager.getPayload()" + sessionManager.getPayload());
            String UserName = claims.getString("sub");
            String userId = "User-ID: " + claims.getString("userId");
            username.setText(UserName);
            userID.setText(userId);

            System.out.println("sub---->>>"+UserName);
            System.out.println("roleId---->>>"+userId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

//        username.setText(sessionManager.getUsername());
//        userID.setText(sessionManager.getUserID());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the ProfileActivity context instead of "this"
                SessionManager sessionManager = new SessionManager(ProfileActivity.this);
                sessionManager.logout();

                // Redirect to the login activity or another appropriate activity
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });




        AllScreenBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                // Clear the back stack and start the new activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Finish the current activity so it's removed from the back stack
                finish();
            }
        });

    }
}