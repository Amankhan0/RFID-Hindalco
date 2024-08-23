package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.dump.ChooseBatch;
import com.example.hellorfid.session.SessionManager;

public class HandheldTerminalActivity extends AppCompatActivity {

    private Button buttonReceiving, buttonPicking, buttonReplace, buttonHold, buttonConsume, buttonHome, addBag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handheldterminal);

        SessionManager sessionManager = new SessionManager(this);

        System.out.println("sessionManager.getToken()"+sessionManager.getToken());
        System.out.println("sessionManager.getPayload()"+sessionManager.getPayload());


        // Initialize buttons
        buttonReceiving = findViewById(R.id.buttonReceiving);
        buttonPicking = findViewById(R.id.buttonPicking);
        buttonReplace = findViewById(R.id.buttonReplace);
        buttonHold = findViewById(R.id.buttonHold);
        buttonConsume = findViewById(R.id.buttonConsume);
        addBag = findViewById(R.id.buttonAddBag);

//         Set onClick listeners
        buttonReceiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                startActivity(new Intent(HandheldTerminalActivity.this, InboundScreenActivity.class));
            }
        });

        addBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                startActivity(new Intent(HandheldTerminalActivity.this, ChooseBatch.class));
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
