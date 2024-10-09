package com.example.hellorfid.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
    TextView stepsTextView;

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
        stepsTextView = findViewById(R.id.stepsTextView);
        System.out.println("sessionManager.getStory(); mmactivity"+sessionManager.getStory());

        createDynamicButtons();
        displaySteps();

    }

    private void displaySteps() {
        String jsonStr = sessionManager.getStory();
        StringBuilder stepsBuilder = new StringBuilder();

        try {
            JSONArray operationsArray = new JSONArray(jsonStr);
            for (int i = 0; i < operationsArray.length(); i++) {
                JSONObject operation = operationsArray.getJSONObject(i);
                String operationName = operation.optString("name", "Unnamed Operation");
                JSONArray storyArray = operation.getJSONArray("story");

                // Add operation name
                stepsBuilder.append("<h6>").append(operationName).append("</h6>");

                for (int j = 0; j < storyArray.length(); j++) {
                    JSONObject action = storyArray.getJSONObject(j);
                    String description = action.optString("description", "No description");
                    stepsBuilder.append("<b>Step ").append(j + 1).append(":</b> ").append(description).append("<br><br>");
                }

                // Add extra space between operations
                if (i < operationsArray.length() - 1) {
                    stepsBuilder.append("<br><br>");
                }
            }

            stepsTextView.setText(Html.fromHtml(stepsBuilder.toString(), Html.FROM_HTML_MODE_COMPACT));
        } catch (JSONException e) {
            e.printStackTrace();
            stepsTextView.setText("Error parsing steps");
        }
    }


    private void createDynamicButtons() {
        String jsonStr = sessionManager.getStory();
        Log.d(TAG, "Final action triggered: " + jsonStr);

        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            int addedButtons = 0;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String buttonName = jsonObject.getString("name");
                JSONArray storyArray = jsonObject.getJSONArray("story");

                Button dynamicButton = createButton(buttonName, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sessionManager.setStory(jsonObject.toString());
                        sessionManager.setCaseExcutor(storyArray.toString());
                        handleButtonClick();
                    }
                });

                gridLayout.addView(dynamicButton);
                addedButtons++;
            }

            // Add placeholder buttons to complete the grid if necessary
            while (addedButtons % 2 != 0) {
                Button placeholderButton = createPlaceholderButton();
                gridLayout.addView(placeholderButton);
                addedButtons++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Button createButton(String text, View.OnClickListener onClickListener) {
        Button button = new Button(this);
        button.setText(text);
        button.setOnClickListener(onClickListener);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = 100;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(8, 8, 8, 8);
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.button_background);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setPadding(20, 20, 20, 20);
        return button;
    }

    private Button createPlaceholderButton() {
        Button button = new Button(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(8, 8, 8, 8);
        button.setLayoutParams(params);
        button.setVisibility(View.INVISIBLE);
        return button;
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