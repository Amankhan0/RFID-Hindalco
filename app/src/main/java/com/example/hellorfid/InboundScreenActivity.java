package com.example.hellorfid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class InboundScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inbound_screen);

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

    }
}