package com.example.hellorfid.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.GridLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.reader.MainActivity;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MultiActionActivity extends AppCompatActivity {

    private static final String TAG = "MultiActionActivity";
    TextView actionName;
    SessionManager sessionManager;
    GridLayout gridLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_multi_action);
        sessionManager = new SessionManager(this);
        actionName = findViewById(R.id.actionName);
        gridLayout = findViewById(R.id.gridLayout);
        actionName.setText(sessionManager.getOptionSelected());

        createDynamicButtons();
    }

    private void createDynamicButtons() {
        String jsonStr = sessionManager.getStory();
        Log.d(TAG, "Final action triggered: " + jsonStr);

        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String buttonName = jsonObject.getString("name");
                JSONArray storyArray = jsonObject.getJSONArray("story");

                Button dynamicButton = new Button(this);
                dynamicButton.setText(buttonName);
                dynamicButton.setLayoutParams(new GridLayout.LayoutParams());
                dynamicButton.setTextColor(getResources().getColor(android.R.color.white));
                dynamicButton.setBackgroundResource(R.drawable.button_background); // Assuming you have this drawable

                gridLayout.addView(dynamicButton);

                dynamicButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sessionManager.setStory(jsonObject.toString());
                        sessionManager.setCaseExcutor(storyArray.toString());
                        handleButtonClick();
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleButtonClick() {
        try {
            JSONArray storyArray = new JSONArray(sessionManager.getCaseExcutor());
            for (int i = 0; i < storyArray.length(); i++) {
                JSONObject action = storyArray.getJSONObject(i);
                String actionType = action.getString("actionType");
                boolean isExcuted = action.getBoolean("isExcuted");
                if (!isExcuted) {
                    handleCase(actionType,action);
                    break;
                }
                Log.d(TAG, "Final action triggered: " + action);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void handleCase(String actionType,JSONObject item) throws JSONException {
        switch (actionType) {
            case "SCAN":
                Log.d(TAG, "Action Recevied: " + sessionManager.getCaseExcutor());
                sessionManager.setCheckTagOn(item.getString("actionName"));
                startActivity(new Intent(MultiActionActivity.this, MainActivity.class));
                break;
            case "FINAL":
//                handleFinalAction(actionName);
                break;
            default:
                Log.w(TAG, "Unknown action type: " + actionType);
                break;
        }
    }
}