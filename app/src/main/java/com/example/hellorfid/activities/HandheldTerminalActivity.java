package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.Helper;
import com.example.hellorfid.constants.StoryHandler;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.model.HomeModel;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HandheldTerminalActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private GridLayout buttonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handheldterminal);

        sessionManager = new SessionManager(this);
        ApiCallBackWithToken apiCallBackWithToken = new ApiCallBackWithToken(this);

        buttonContainer = findViewById(R.id.buttonContainer);
        try {
            JSONObject jsonObjectForRole = new JSONObject(sessionManager.getPayload());
            String roleId = jsonObjectForRole.getString("roleId");
            JSONObject searchObject = new JSONObject();
            searchObject.put("_id", roleId);
            JSONObject s = Helper.getSearchJson(1, 1, searchObject);

            JSONObject resOfRoles = Helper.commanHitApi(apiCallBackWithToken, Constants.searchRole, s);
            System.out.println("resOfRoles====>>>>"+resOfRoles);
            sessionManager.setRole(resOfRoles.toString());

            addButtonsBasedOnRole();

        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }

        ImageView ProfileButton = findViewById(R.id.profile);
        ProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HandheldTerminalActivity.this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void addButtonsBasedOnRole() {
        List<ButtonInfo> buttonInfoList = new ArrayList<>();

        buttonInfoList.add(new ButtonInfo("Outbound Orders", v -> {
            sessionManager.setScreenType(Constants.TWO);
            sessionManager.setOptionSelected(Constants.OUTBOUND_ORDER);
            startActivity(new Intent(HandheldTerminalActivity.this, OrderActivity.class));
        }));

        buttonInfoList.add(new ButtonInfo("Inbound Orders", v -> {
            sessionManager.setOptionSelected(Constants.INBOUND_ORDER);
            startActivity(new Intent(HandheldTerminalActivity.this, OrderActivity.class));
        }));
        buttonInfoList.add(new ButtonInfo("Hold", v -> {
            try {

                sessionManager.setOptionSelected(Constants.HOLD);

                StoryHandler.holdInventory(sessionManager, HandheldTerminalActivity.this, "NA", Constants.HOLD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));
        buttonInfoList.add(new ButtonInfo("Un Hold", v -> {
            try {
                sessionManager.setOptionSelected(Constants.UNHOLD);

                StoryHandler.holdInventory(sessionManager, HandheldTerminalActivity.this, "NA", Constants.UNHOLD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        buttonInfoList.add(new ButtonInfo("Replace", v -> {
            try {
                sessionManager.setOptionSelected(Constants.REPLACE);
                StoryHandler.replace(sessionManager, HandheldTerminalActivity.this, "NA", Constants.INVENTORY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        buttonInfoList.add(new ButtonInfo("Consume", v -> {

//            sessionManager.setOptionSelected(Constants.WeighingScale);
//            try {
//                StoryHandler.WeighingScale(sessionManager, HandheldTerminalActivity.this, "NA", Constants.INVENTORY);
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }

        }
        ));


        buttonInfoList.add(new ButtonInfo("Mapping", v -> {
                    sessionManager.setOptionSelected(Constants.MAPPING);
                    startActivity(new Intent(HandheldTerminalActivity.this, Mapping.class));
                }
        ));
        buttonInfoList.add(new ButtonInfo("Cycle Count", v -> {
            try {
                sessionManager.setScreenType(Constants.TWO);

                sessionManager.setOptionSelected(Constants.CYCLE_COUNT);
                StoryHandler.cycleCountStory(sessionManager, HandheldTerminalActivity.this, "NA", Constants.CYCLE_COUNT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));
        buttonInfoList.add(new ButtonInfo("Move", v -> {
            try {
                sessionManager.setScreenType(Constants.TWO);

                sessionManager.setOptionSelected(Constants.MOVE);
                StoryHandler.inventoryMoveStory(sessionManager, HandheldTerminalActivity.this, "NA", Constants.MOVE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        buttonInfoList.add(new ButtonInfo("General Status Change", v -> {
                sessionManager.setOptionSelected(Constants.OPERATION_STATUS_CHANGE);
                startActivity(new Intent(HandheldTerminalActivity.this, GeneralStatusChangeActivity.class))  ;          }

        ));

        buttonInfoList.add(new ButtonInfo("Recheck Order", v -> {
            sessionManager.setScreenType(Constants.ONE);
            sessionManager.setOptionSelected(Constants.RECHECK);
            startActivity(new Intent(HandheldTerminalActivity.this, OrderActivity.class));
        }


        ));

        buttonInfoList.add(new ButtonInfo("Weighing Scale", v -> {

            sessionManager.setOptionSelected(Constants.WeighingScale);
            try {
                StoryHandler.WeighingScale(sessionManager, HandheldTerminalActivity.this, "NA", Constants.INVENTORY);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        ));


        int addedButtons = 0;
        for (ButtonInfo buttonInfo : buttonInfoList) {
            try {
                String roleValue = sessionManager.getRole(buttonInfo.text);
                System.out.println("key===>>>>: " + buttonInfo.text + " result: " + roleValue);
                if (roleValue != null && roleValue.equals("true")) {
                    Button button = createButton(buttonInfo);
                    buttonContainer.addView(button);
                    addedButtons++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Add placeholder buttons to complete the grid if necessary
        while (addedButtons % 2 != 0) {
            Button placeholderButton = createPlaceholderButton();
            buttonContainer.addView(placeholderButton);
            addedButtons++;
        }
    }

    private Button createButton(ButtonInfo buttonInfo) {
        Button button = new Button(this);
        button.setText(buttonInfo.text);
        button.setOnClickListener(buttonInfo.onClickListener);
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

    private static class ButtonInfo {
        String text;
        View.OnClickListener onClickListener;

        ButtonInfo(String text, View.OnClickListener onClickListener) {
            this.text = text;
            this.onClickListener = onClickListener;
        }
    }

}