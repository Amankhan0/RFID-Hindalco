package com.example.hellorfid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddBagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bag);

        ImageView AllScreenBackBtn = findViewById(R.id.all_screen_back_btn);
        AllScreenBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBagActivity.this, HandheldTerminalActivity.class);
                // Clear the back stack and start the new activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Finish the current activity so it's removed from the back stack
                finish();
            }
        });

        Spinner buildingSpinner = findViewById(R.id.buildingspinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> buildingadapter = ArrayAdapter.createFromResource(this,
                R.array.addbagbuilding, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        buildingadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        buildingSpinner.setAdapter(buildingadapter);

        Spinner productSpinner = findViewById(R.id.productspinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> productadapter = ArrayAdapter.createFromResource(this,
                R.array.addbagproduct, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        productadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        productSpinner.setAdapter(productadapter);


    }
}