package com.example.hellorfid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HandheldTerminalActivity extends AppCompatActivity {

    private Button buttonReceiving, buttonPicking, buttonReplace, buttonHold, buttonConsume, buttonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handheldterminal);

        // Initialize buttons
        buttonReceiving = findViewById(R.id.buttonReceiving);
        buttonPicking = findViewById(R.id.buttonPicking);
        buttonReplace = findViewById(R.id.buttonReplace);
        buttonHold = findViewById(R.id.buttonHold);
        buttonConsume = findViewById(R.id.buttonConsume);

//         Set onClick listeners
        buttonReceiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                startActivity(new Intent(HandheldTerminalActivity.this, InboundScreenActivity.class));
            }
        });

        ImageView AllScreenBackBtn = findViewById(R.id.all_screen_back_btn);
        AllScreenBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HandheldTerminalActivity.this, LoginActivity.class);
                // Clear the back stack and start the new activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Finish the current activity so it's removed from the back stack
                finish();
            }
        });

        ImageView ProfileButton = findViewById(R.id.profile);
        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HandheldTerminalActivity.this, ProfileActivity.class);
                // Clear the back stack and start the new activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Finish the current activity so it's removed from the back stack
                finish();
            }
        });

//
//        buttonPicking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle Picking button click
//                startActivity(new Intent(HandheldTerminalActivity.this, PickingActivity.class));
//            }
//        });
//
//        buttonReplace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle Replace button click
//                startActivity(new Intent(HandheldTerminalActivity.this, ReplaceActivity.class));
//            }
//        });
//
//        buttonHold.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle Hold button click
//                startActivity(new Intent(HandheldTerminalActivity.this, HoldActivity.class));
//            }
//        });
//
//        buttonConsume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle Consume button click
//                startActivity(new Intent(HandheldTerminalActivity.this, ConsumeActivity.class));
//            }
//        });
//
//        buttonHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle Home button click
//                // This could return to a home screen or perform another action
//                finish();
//            }
//        });
    }
}
