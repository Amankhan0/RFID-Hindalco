package com.example.hellorfid.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.StoryHandler;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HandheldTerminalActivity extends AppCompatActivity {

    private Button inventoryMove,cycleCount,orderButton,inboundorderButton, buttonReplace, buttonHold,buttonUnHold, buttonConsume, buttonMapping, generalStatusChange;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handheldterminal);

        SessionManager sessionManager = new SessionManager(this);




        System.out.println("sessionManager.getToken()"+sessionManager.getToken());
        System.out.println("sessionManager.getPayload()"+sessionManager.getPayload());


        // Initialize buttons
        buttonReplace = findViewById(R.id.buttonReplace);
        buttonHold = findViewById(R.id.buttonHold);
        buttonConsume = findViewById(R.id.buttonConsume);
        orderButton = findViewById(R.id.ordersButton);
        inboundorderButton = findViewById(R.id.inBoundOrder);
        buttonHold = findViewById(R.id.buttonHold);
        buttonUnHold = findViewById(R.id.unHold);
        buttonMapping = findViewById(R.id.buttonMapping);
        cycleCount= findViewById(R.id.cycleCount);
        inventoryMove = findViewById(R.id.inventoryMove);
        generalStatusChange = findViewById(R.id.generalStatusChange);

        inventoryMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sessionManager.setOptionSelected(Constants.MOVE+"_"+Constants.INVENTORY);
                    StoryHandler.inventoryMoveStory(sessionManager,HandheldTerminalActivity.this,"NA",Constants.MOVE);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                sessionManager.setOptionSelected(Constants.OUTBOUND_ORDER);
                startActivity(new Intent(HandheldTerminalActivity.this, OrderActivity.class));
            }
        });

        cycleCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    sessionManager.setOptionSelected(Constants.CYCLE_COUNT);
                    StoryHandler.cycleCountStory(sessionManager,HandheldTerminalActivity.this,"NA",Constants.CYCLE_COUNT);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
//                sessionManager.setOptionSelected(Constants.MAPPING);
                startActivity(new Intent(HandheldTerminalActivity.this, Mapping.class));
            }
        });

        inboundorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click
                sessionManager.setOptionSelected(Constants.INBOUND_ORDER);
                startActivity(new Intent(HandheldTerminalActivity.this, OrderActivity.class));
            }
        });

        buttonHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    StoryHandler.holdInventory(sessionManager,HandheldTerminalActivity.this,"NA",Constants.HOLD);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

//                try {
//                    JSONArray jsonArray=  new JSONArray();
//                    JSONObject story1 = StoryHandler.storyJsonObj("1",Constants.LOCATION,Constants.LOCATION,false,"SCAN","NO_DATA","","1");
////                    JSONObject story2 = StoryHandler.storyJsonObj("2",Constants.INVENTORY,false,"SCAN","NO_DATA");
//                    JSONObject story3 = StoryHandler.storyJsonObj("3",Constants.UPDATE,Constants.UPDATE,false,"UPDATE","NO_DATA","","0");
//                    jsonArray.put(story1);
////                    jsonArray.put(story2);
//                    jsonArray.put(story3);
//                    JSONObject finalarr = StoryHandler.storyJson("Hold Location","Hold Location",jsonArray);
//                    JSONArray jsonArray1=  new JSONArray();
//                    jsonArray1.put(finalarr);
//                    System.out.println("finalarr.toString()"+jsonArray1.toString());
//                    sessionManager.setStory(jsonArray1.toString());
//                    System.out.println("finalarr.toString()"+jsonArray1.toString());
//                    Intent intent = new Intent(HandheldTerminalActivity.this, MultiActionActivity.class);
//                    startActivity(intent);

//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
            }
        });


        buttonUnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    StoryHandler.holdInventory(sessionManager,HandheldTerminalActivity.this,"NA",Constants.UNHOLD);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // Handle Receiving button click
//                startActivity(new Intent(HandheldTerminalActivity.this, UnHoldActivity.class));
            }
        });

        buttonReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Receiving button click

                try {
                    StoryHandler.replace(sessionManager,HandheldTerminalActivity.this,"NA",Constants.INVENTORY);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

//                sessionManager.clearReplaceScan();
//                sessionManager.clearSecondReplaceScan();
//                startActivity(new Intent(HandheldTerminalActivity.this, ReplaceMainActivity.class));
            }
        });

        buttonConsume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HandheldTerminalActivity.this, ConsumeActivity.class));
            }
        });

        generalStatusChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HandheldTerminalActivity.this, GeneralStatusChangeActivity.class));
//                StoryHandler.generalStatusChangeStory(sessionManager, HandheldTerminalActivity.this, "NA", Constants.OPERATION_STATUS_CHANGE);
            }
        });

        ImageView ProfileButton = findViewById(R.id.profile);
        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HandheldTerminalActivity.this, ProfileActivity.class);
                // Clear the back stack and start the new activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Finish the current activity so it's removed from the back stack
                finish();
            }
        });
    }
}
