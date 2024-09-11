package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;

public class SettingActivity extends AppCompatActivity {

    private View contentView;
    private Button toggleButton;
    private EditText ipAddressEditText;
    private SessionManager sessionManager;
    private static final String IP_ADDRESS_KEY = "ip_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        sessionManager = new SessionManager(this);

        setupAccordion();
        setupIpAddressEditText();
        setupBackButton();
    }

    private void setupAccordion() {
        toggleButton = findViewById(R.id.toggleButton1);
        contentView = findViewById(R.id.content1);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent();
            }
        });
    }

    private void setupIpAddressEditText() {
        ipAddressEditText = findViewById(R.id.ipAddressEditText);

        // Set default value from SessionManager
        String savedIpAddress = sessionManager.getIpAddress();
        if (savedIpAddress != null) {
            ipAddressEditText.setText(savedIpAddress);
        }

        ipAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Save the text to SessionManager
                sessionManager.setIpAddress(s.toString());
            }
        });
    }

    private void setupBackButton() {
        ImageView allScreenBackBtn = findViewById(R.id.allScreenBackBtn);
        allScreenBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, HandheldTerminalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void toggleContent() {
        if (contentView.getVisibility() == View.VISIBLE) {
            closeContent();
        } else {
            openContent();
        }
    }

    private void openContent() {
        contentView.setVisibility(View.VISIBLE);
        toggleButton.setText("Close");
    }

    private void closeContent() {
        contentView.setVisibility(View.GONE);
        toggleButton.setText("Open");
    }


}