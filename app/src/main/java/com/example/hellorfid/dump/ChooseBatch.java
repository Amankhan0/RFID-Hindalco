package com.example.hellorfid.dump;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hellorfid.R;
import com.example.hellorfid.activities.AddBatchActivity;

public class ChooseBatch extends AppCompatActivity {

    private TextView automatically;
    private TextView manually;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_batch);
        EdgeToEdge.enable(this);

        automatically = findViewById(R.id.Automatically);
        manually = findViewById(R.id.manually);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set onClick listeners
        automatically.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the automatically button click
                Intent intent = new Intent(ChooseBatch.this, AddBatchActivity.class);
                startActivity(intent);
            }
        });

        manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the manually button click
                Intent intent = new Intent(ChooseBatch.this, AddBatchActivity.class);
                startActivity(intent);
            }
        });
    }
}
