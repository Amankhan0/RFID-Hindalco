package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;

public class HandheldTerminalActivity extends AppCompatActivity {

    private Button orderButton,inboundorderButton, buttonReplace, buttonHold,buttonUnHold, buttonConsume, buttonMapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handheldterminal);

        SessionManager sessionManager = new SessionManager(this);



        System.out.println("sessionManager.getToken()"+sessionManager.getToken());
        System.out.println("sessionManager.getPayload()"+sessionManager.getPayload());


        // Initialize buttons
        buttonReplace = findViewById(R.id.buttonReplace);
        buttonHold = findViewById(R.id.buttonHold);
        buttonConsume = findViewById(R.id.buttonConsume);
        orderButton = findViewById(R.id.ordersButton);
        inboundorderButton = findViewById(R.id.inBoundOrder);
        buttonHold = findViewById(R.id.buttonHold);
        buttonUnHold = findViewById(R.id.unHold);
        buttonMapping = findViewById(R.id.buttonMapping);




        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                startActivity(new Intent(HandheldTerminalActivity.this, OrderActivity.class));
            }
        });

        buttonMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                startActivity(new Intent(HandheldTerminalActivity.this, Mapping.class));
            }
        });

        inboundorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                startActivity(new Intent(HandheldTerminalActivity.this, InboundOrderActivity.class));
            }
        });

        buttonHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                startActivity(new Intent(HandheldTerminalActivity.this, HoldActivity.class));
            }
        });


        buttonUnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                startActivity(new Intent(HandheldTerminalActivity.this, UnHoldActivity.class));
            }
        });

        buttonReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                sessionManager.clearReplaceScan();
                sessionManager.clearSecondReplaceScan();
                startActivity(new Intent(HandheldTerminalActivity.this, ReplaceMainActivity.class));
            }
        });

        buttonConsume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HandheldTerminalActivity.this, ConsumeActivity.class));
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
    }
}
