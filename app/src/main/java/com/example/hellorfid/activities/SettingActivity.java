//package com.example.hellorfid.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.hellorfid.R;
//import com.example.hellorfid.session.SessionManager;
//
//public class SettingActivity extends AppCompatActivity {
//
//    private View contentView1, contentView2;
//    private Button toggleButton1, toggleButton2;
//    private EditText ipAddressEditText;
//    private SeekBar rfidRangeSeekBar;
//    private TextView rfidRangeValueText;
//    private SessionManager sessionManager;
//    private static final String TAG = "SettingActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
//
//        sessionManager = new SessionManager(this);
//
//        initializeViews();
//        setupAccordions();
//        setupIpAddressEditText();
//        setupRfidRangeSeekBar();
//        setupBackButton();
//    }
//
//    private void initializeViews() {
//        contentView1 = findViewById(R.id.content1);
//        contentView2 = findViewById(R.id.content2);
//        toggleButton1 = findViewById(R.id.toggleButton1);
//        toggleButton2 = findViewById(R.id.toggleButton2);
//        ipAddressEditText = findViewById(R.id.ipAddressEditText);
//        rfidRangeSeekBar = findViewById(R.id.rfidRangeSeekBar);
//        rfidRangeValueText = findViewById(R.id.rfidRangeValueText);
//    }
//
//    private void setupAccordions() {
//        toggleButton1.setOnClickListener(v -> toggleContent(contentView1, toggleButton1));
//        toggleButton2.setOnClickListener(v -> toggleContent(contentView2, toggleButton2));
//    }
//
//    private void setupIpAddressEditText() {
//        String savedIpAddress = sessionManager.getIpAddress();
//        if (savedIpAddress != null) {
//            ipAddressEditText.setText(savedIpAddress);
//        }
//
//        ipAddressEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                sessionManager.setIpAddress(s.toString());
//            }
//        });
//    }
//
//    private void setupRfidRangeSeekBar() {
//        rfidRangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                updateRfidRangeValue(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });
//    }
//
//    private void updateRfidRangeValue(int progress) {
//        rfidRangeValueText.setText("Current Range: " + progress + "%");
//        Log.d(TAG, "RFID Reader Range set to: " + progress + "%");
//    }
//
//    private void setupBackButton() {
//        ImageView allScreenBackBtn = findViewById(R.id.allScreenBackBtn);
//        allScreenBackBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(SettingActivity.this, HandheldTerminalActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        });
//    }
//
//    private void toggleContent(View contentView, Button toggleButton) {
//        if (contentView.getVisibility() == View.VISIBLE) {
//            contentView.setVisibility(View.GONE);
//            toggleButton.setText("Open");
//        } else {
//            contentView.setVisibility(View.VISIBLE);
//            toggleButton.setText("Close");
//        }
//    }
//}

package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingActivity extends AppCompatActivity {

    private View contentView1, contentView2;
    private Button toggleButton1, toggleButton2;
    private Spinner ipAddressSpinner;
    private EditText ipAddressEditText;
    private SeekBar rfidRangeSeekBar;
    private TextView rfidRangeValueText;
    private SessionManager sessionManager;
    private static final String TAG = "SettingActivity";

    private static final String DEFAULT_IP = "https://api.hindalco.headsupcorporation.com/";
    private static final String ALTERNATE_IP = "http://192.168";
    private static final String CUSTOM_IP = "Custom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sessionManager = new SessionManager(this);

        initializeViews();
        setupAccordions();
        setupIpAddressSpinner();
        setupIpAddressEditText();
        setupRfidRangeSeekBar();
        setupBackButton();
    }

    private void initializeViews() {
        contentView1 = findViewById(R.id.content1);
        contentView2 = findViewById(R.id.content2);
        toggleButton1 = findViewById(R.id.toggleButton1);
        toggleButton2 = findViewById(R.id.toggleButton2);
        ipAddressSpinner = findViewById(R.id.ipAddressSpinner);
        ipAddressEditText = findViewById(R.id.ipAddressEditText);
        rfidRangeSeekBar = findViewById(R.id.rfidRangeSeekBar);
        rfidRangeValueText = findViewById(R.id.rfidRangeValueText);
    }

    private void setupAccordions() {
        toggleButton1.setOnClickListener(v -> toggleContent(contentView1, toggleButton1));
        toggleButton2.setOnClickListener(v -> toggleContent(contentView2, toggleButton2));
    }

    private void setupIpAddressSpinner() {
        ArrayList<String> ipOptions = new ArrayList<>(Arrays.asList(DEFAULT_IP, ALTERNATE_IP, CUSTOM_IP));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ipOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ipAddressSpinner.setAdapter(adapter);

        String savedIpAddress = sessionManager.getIpAddress();
        if (savedIpAddress != null) {
            if (savedIpAddress.equals(DEFAULT_IP)) {
                ipAddressSpinner.setSelection(0);
                ipAddressEditText.setText(DEFAULT_IP);
            } else if (savedIpAddress.startsWith(ALTERNATE_IP)) {
                ipAddressSpinner.setSelection(1);
                ipAddressEditText.setText(savedIpAddress);
            } else {
                ipAddressSpinner.setSelection(2);
                ipAddressEditText.setText(savedIpAddress);
            }
        } else {
            ipAddressSpinner.setSelection(0);
            ipAddressEditText.setText(DEFAULT_IP);
        }

        ipAddressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedIp = parent.getItemAtPosition(position).toString();
                switch (selectedIp) {
                    case DEFAULT_IP:
                        ipAddressEditText.setText(DEFAULT_IP);
                        ipAddressEditText.setEnabled(false);
                        sessionManager.setIpAddress(DEFAULT_IP);
                        break;
                    case ALTERNATE_IP:
                        ipAddressEditText.setText(ALTERNATE_IP);
                        ipAddressEditText.setEnabled(true);
                        break;
                    case CUSTOM_IP:
                        ipAddressEditText.setText("");
                        ipAddressEditText.setEnabled(true);
                        ipAddressEditText.setHint("Enter custom IP address");
                        break;
                }
                Log.d(TAG, "Selected IP Address: " + selectedIp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupIpAddressEditText() {
        ipAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int selectedPosition = ipAddressSpinner.getSelectedItemPosition();
                if (selectedPosition == 1 || selectedPosition == 2) {
                    sessionManager.setIpAddress(s.toString());
                }
            }
        });
    }

    private void setupRfidRangeSeekBar() {
        rfidRangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateRfidRangeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateRfidRangeValue(int progress) {
        rfidRangeValueText.setText("Current Range: " + progress + "%");
        Log.d(TAG, "RFID Reader Range set to: " + progress + "%");
    }

    private void setupBackButton() {
        ImageView allScreenBackBtn = findViewById(R.id.allScreenBackBtn);
        allScreenBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, HandheldTerminalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void toggleContent(View contentView, Button toggleButton) {
        if (contentView.getVisibility() == View.VISIBLE) {
            contentView.setVisibility(View.GONE);
            toggleButton.setText("Open");
        } else {
            contentView.setVisibility(View.VISIBLE);
            toggleButton.setText("Close");
        }
    }
}