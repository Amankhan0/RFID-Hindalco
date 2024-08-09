package com.example.hellorfid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
public class ScanReplace extends AppCompatActivity {

    private LinearLayout linearLayout;
    private TextView scanTextView;
    private TextView bagTextView;
    private EditText editTextText;
    private Button submitbn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanpicking);

        // Initialize the views
        linearLayout = findViewById(R.id.linearLayout);
        editTextText = findViewById(R.id.editTextText);
        submitbn = findViewById(R.id.submitButton1);


        // Example of setting a click listener on the EditText
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanReplace.this, MainActivity.class);
                // Clear the back stack and start the new activity
                startActivity(intent);
                // Finish the current activity so it's removed from the back stack
            }
        });

        // Example of setting a click listener on the EditText
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanReplace.this, MainActivity.class);
                // Clear the back stack and start the new activity
                startActivity(intent);
                // Finish the current activity so it's removed from the back stack
            }
        });


        // Example of setting a click listener on the EditText
        submitbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanReplace.this, MainActivity.class);
                // Clear the back stack and start the new activity
                startActivity(intent);
                // Finish the current activity so it's removed from the back stack
            }
        });

    }
}