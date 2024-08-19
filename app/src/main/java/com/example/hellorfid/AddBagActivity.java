package com.example.hellorfid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBagActivity extends AppCompatActivity {

    private Spinner buildingSpinner;
    private Spinner productSpinner;
    private EditText lotNumberEditText;
    private EditText batchNumberEditText;
    private EditText bagQuantityEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bag);

        // Initialize views
        buildingSpinner = findViewById(R.id.buildingspinner);
        productSpinner = findViewById(R.id.productspinner);
        lotNumberEditText = findViewById(R.id.lotnum);
        batchNumberEditText = findViewById(R.id.batchnum);
        bagQuantityEditText = findViewById(R.id.numofbag);
        submitButton = findViewById(R.id.addbad_btn);

        ImageView AllScreenBackBtn = findViewById(R.id.all_screen_back_btn);
        AllScreenBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBagActivity.this, HandheldTerminalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Set up building spinner
        ArrayAdapter<CharSequence> buildingAdapter = ArrayAdapter.createFromResource(this,
                R.array.addbagbuilding, android.R.layout.simple_spinner_item);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(buildingAdapter);

        // Set up product spinner
        ArrayAdapter<CharSequence> productAdapter = ArrayAdapter.createFromResource(this,
                R.array.addbagproduct, android.R.layout.simple_spinner_item);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(productAdapter);

        // Set up submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void submitForm() {
        String selectedBuilding = buildingSpinner.getSelectedItem().toString();
        String selectedProduct = productSpinner.getSelectedItem().toString();
        String lotNumber = lotNumberEditText.getText().toString();
        String batchNumber = batchNumberEditText.getText().toString();
        String bagQuantity = bagQuantityEditText.getText().toString();

        // Validate inputs
        if (selectedBuilding.isEmpty() || selectedProduct.isEmpty() || lotNumber.isEmpty() ||
                batchNumber.isEmpty() || bagQuantity.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Print values (for debugging, remove in production)
        System.out.println("Selected Building: " + selectedBuilding);
        System.out.println("Selected Product: " + selectedProduct);
        System.out.println("Lot Number: " + lotNumber);
        System.out.println("Batch Number: " + batchNumber);
        System.out.println("Bag Quantity: " + bagQuantity);

        // TODO: Process the form data (e.g., save to database, send to server, etc.)

        Toast.makeText(this, "Form submitted successfully", Toast.LENGTH_SHORT).show();
    }
}