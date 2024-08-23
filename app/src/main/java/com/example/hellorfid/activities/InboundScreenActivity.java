package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.hellorfid.R;
import com.example.hellorfid.fragment.CompleteFragmentActivity;
import com.example.hellorfid.fragment.InprogressFragmentActivity;

public class InboundScreenActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inbound_screen);

        // Find and display the InprogressFragmentActivity by default
        InprogressFragmentActivity inprogressFragmentActivity = new InprogressFragmentActivity();

        // Replace the contents of the container with the new fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.inbound_fragments, inprogressFragmentActivity);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        // Make the fragment container visible
        findViewById(R.id.inbound_fragments).setVisibility(View.VISIBLE);

        Button InprogressBtn = findViewById(R.id.inbound_inprogress_btn);
        InprogressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InprogressFragmentActivity InprogressFragmentActivity = new InprogressFragmentActivity();

                // Replace the contents of the container with the new fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.inbound_fragments, InprogressFragmentActivity);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

                // Make the fragment container visible
                findViewById(R.id.inbound_fragments).setVisibility(View.VISIBLE);
            }
        });

        ImageView AllScreenBackBtn = findViewById(R.id.all_screen_back_btn);
        AllScreenBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InboundScreenActivity.this, HandheldTerminalActivity.class);
                // Clear the back stack and start the new activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Finish the current activity so it's removed from the back stack
                finish();
            }
        });




        Button CompleteBtn = findViewById(R.id.inbound_complete_btn);
        CompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompleteFragmentActivity CompleteFragmentActivity = new CompleteFragmentActivity();

                // Replace the contents of the container with the new fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.inbound_fragments, CompleteFragmentActivity);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

                // Make the fragment container visible
                findViewById(R.id.inbound_fragments).setVisibility(View.VISIBLE);
            }
        });




//
//        ImageView nexticon = findViewById(R.id.column3_next);
//        nexticon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(InboundScreenActivity.this, ScanReplace.class);
//                // Clear the back stack and start the new activity
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                // Finish the current activity so it's removed from the back stack
//                finish();
//            }
//        });
//
//        System.out.println("0------");

    }
}