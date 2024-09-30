package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;

public class PendingOpsActivity extends AppCompatActivity {

    Button cancel;
    Button complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pending_ops);
        SessionManager sessionManager = new SessionManager(this);
        cancel = findViewById(R.id.cancel);
        complete = findViewById(R.id.complete);


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PendingOpsActivity.this,ActionActivity.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.clearPendingOps();
                startActivity(new Intent(PendingOpsActivity.this,HandheldTerminalActivity.class));

            }
        });

    }
}