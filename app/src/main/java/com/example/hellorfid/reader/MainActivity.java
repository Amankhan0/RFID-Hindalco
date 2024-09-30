package com.example.hellorfid.reader;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.health.connect.datatypes.units.Power;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.velocity.runtime.directive.Parse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellorfid.activities.ActionActivity;
import com.example.hellorfid.activities.AddBatchFormActivity;
import com.example.hellorfid.activities.BatchActivity;
import com.example.hellorfid.activities.HandheldTerminalActivity;
import com.example.hellorfid.activities.MultiActionActivity;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.Helper;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.R;
import com.example.hellorfid.session.SessionManager;
import com.example.hellorfid.dump.Tag;
import com.example.hellorfid.dump.TagAdapter;
import com.example.hellorfid.session.SessionManagerBag;
import com.zebra.rfid.api3.ENUM_TRANSPORT;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.RfidEventsListener;
import com.zebra.rfid.api3.RfidReadEvents;
import com.zebra.rfid.api3.RfidStatusEvents;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.POWER_EVENT;
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TagData;
import com.zebra.rfid.api3.TriggerInfo;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements TagAdapter.OnTagDeleteListener {

    private static final Logger log = Logger.getLogger(MainActivity.class);
    private static Readers readers;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    private static RFIDReader reader;
    private static final String TAG = "DEMO";
    private TextView tagTextView;
    private   String stroyId;
    private boolean isError = false;
    //    private SessionManagerBag sessionManagerBag;
    private SessionManager sessionManager;

    private int totalInventoryToScan = 0;
    private ApiCallBackWithToken apiCallBackWithToken;
    private Button resetButton;
    public static Button submitButton;
    private EventHandler eventHandler;
    private Set<String> scannedTagIds;

    private RecyclerView recyclerView;
    private TagAdapter tagAdapter;
    private List<Tag> tagList;
    private MediaPlayer mediaPlayer;
    private ArrayList tagListArr;

    private TextView actionName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiCallBackWithToken = new ApiCallBackWithToken(this);
        actionName = findViewById(R.id.actionName);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer = MediaPlayer.create(this, R.raw.invalid_tag);
//        sessionManagerBag = new SessionManagerBag(this);
        sessionManager = new SessionManager(this);
        actionName.setText("Scanning for "+sessionManager.getCheckTagOn());
        startExecutor();
        initializeRecyclerView();
        tagListArr = new ArrayList<>();

        if (mediaPlayer == null) {
            Log.e(TAG, "MediaPlayer initialization failed. Check if the audio file exists in res/raw.");
        } else {
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "MediaPlayer is prepared and ready to play.");
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
                    return false;
                }
            });
        }



        tagTextView = findViewById(R.id.totalScan1);

        totalInventoryToScan =  parseInt(sessionManager.getSetScanCount()==null?"0":sessionManager.getSetScanCount());

        Log.d(TAG, "Total tags to scan initialized to: " + totalInventoryToScan);

        resetButton = findViewById(R.id.submitButton1);
        submitButton = findViewById(R.id.submitButton);
        scannedTagIds = new HashSet<>();
        submitButton.setEnabled(false);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetScannedTags();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveDataUsingId();
                Log.d(TAG, "savedData: " + sessionManager.getCaseExcutor());
                startExecutor();
                resetButton.performClick();

            }
        });

        // SDK
        if (readers == null) {
            readers = new Readers(this, ENUM_TRANSPORT.SERVICE_SERIAL);
        }

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    if (readers != null) {
                        availableRFIDReaderList = readers.GetAvailableRFIDReaderList();
                        if (availableRFIDReaderList != null && !availableRFIDReaderList.isEmpty()) {
                            readerDevice = availableRFIDReaderList.get(0);
                            reader = readerDevice.getRFIDReader();
                            System.out.println("Check Connection 0"+reader.isConnected());

                            if (!reader.isConnected()) {
                                reader.connect();
                                System.out.println("Check Connection 1"+reader.isConnected());
                                ConfigureReader();
                                return true;
                            }else {
                                System.out.println("Check Connection 3"+reader.isConnected());

                            }
                        }
                    }
                } catch (InvalidUsageException | OperationFailureException e) {
                    e.printStackTrace();
                    Log.d(TAG, "OperationFailureException " + e.getMessage());
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    Toast.makeText(getApplicationContext(), "Reader Connected", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();

        updateTagCountDisplay();
        updateSubmitButtonState();

    }



    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tagList = new ArrayList<>();
        tagAdapter = new TagAdapter(this, tagList, this);
        recyclerView.setAdapter(tagAdapter);

    }

    private void resetScannedTags() {
        System.out.println("scannedTagIds" + scannedTagIds.toString());
//        sessionManagerBag.clearSession();
        scannedTagIds.clear();
        tagList.clear();
        tagListArr.clear();
        tagAdapter.notifyDataSetChanged();
        isError = false;
        submitButton.setEnabled(false);
        updateTagCountDisplay();
        updateSubmitButtonState();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ConfigureReader() {
        if (reader.isConnected()) {
            TriggerInfo triggerInfo = new TriggerInfo();
            triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
            System.out.println("Check Connection"+ triggerInfo.toString());
            try {
                if (eventHandler == null) eventHandler = new EventHandler();
                reader.Events.addEventsListener(eventHandler);
                reader.Events.setHandheldEvent(true);
                reader.Events.setTagReadEvent(true);
                reader.Events.setAttachTagDataWithReadEvent(false);
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
//                reader.Config.setStartTrigger(triggerInfo.StartTrigger);
//                reader.Config.setStopTrigger(triggerInfo.StopTrigger);

                System.out.println("Check Connection"+ reader.toString());

            } catch (InvalidUsageException | OperationFailureException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (reader != null) {
                reader.Events.removeEventsListener(eventHandler);
                reader.disconnect();
                Toast.makeText(getApplicationContext(), "Disconnecting reader", Toast.LENGTH_LONG).show();
                reader = null;
                readers.Dispose();
                readers = null;
            }
        } catch (InvalidUsageException | OperationFailureException e) {
            e.printStackTrace();
        }
    }

    private void updateTagCountDisplay() {
        runOnUiThread(() -> {
            tagTextView.setText("Total scan - " + scannedTagIds.size() + "/" + totalInventoryToScan);
        });
    }

    public void addTagToList(String tagId) throws JSONException, InterruptedException {
        scannedTagIds.add(tagId);  // Add the new tag first
        int currentSize = scannedTagIds.size();
        boolean isOverLimit = currentSize > totalInventoryToScan;
        JSONObject s = new JSONObject();
        s.put("rfidTag",tagId);
        JSONObject searchJson = Helper.getSearchJson(1,1,s);
        System.out.println("response json tag 1 ---->>>"+searchJson.toString());

        JSONObject res = Helper.commanHitApi(apiCallBackWithToken,Constants.searchRfidTag,searchJson);

        if(res!=null) {
            System.out.println("response json tag 3---->>>" + res.toString());

            if (res.has("content") && res.getJSONArray("content").length() > 0) {
                tagListArr.add(res.getJSONArray("content").get(0).toString());
                JSONObject obj = res.getJSONArray("content").getJSONObject(0);
                if(sessionManager.getCheckTagOn().equals(obj.getString("tagType"))) {
                    if(sessionManager.getBuildingId().equals(obj.getString("currentLocation"))) {
                        tagList.add(new Tag(tagId, isOverLimit, obj.getString("tagType")));
                        tagAdapter.notifyItemInserted(tagList.size() - 1);
                        updateTagCountDisplay();
                        submitButton.setEnabled(true);
                    }else {
                        tagList.add(new Tag(tagId, isOverLimit, "WRONG_BUILDING"));
                        tagAdapter.notifyItemInserted(tagList.size() - 1);
                        updateTagCountDisplay();
                        submitButton.setEnabled(false);
                        isError = true;
                    }
                }else {
                    tagList.add(new Tag(tagId, isOverLimit, "Wrong ops "+obj.getString("tagType")));
                    tagAdapter.notifyItemInserted(tagList.size() - 1);
                    updateTagCountDisplay();
                    submitButton.setEnabled(false);
                    isError = true;
                }
            } else {
                System.out.println("response json tag 6 tag not in db");
            }


        }else{


            if(!isError){
                submitButton.setEnabled(true);
            }


//            if(!sessionManager.getCheckTagOn().equals(Constants.INVENTORY)){
//                submitButton.setEnabled(false);
//            }else {
//            }

            JSONObject tagJson = Helper.TagJson();
            tagJson.put("tagType",Constants.NEW_TAG);
            tagJson.put("rfidTag",tagId);
            tagListArr.add(tagJson.toString());
            tagList.add(new Tag(tagId, isOverLimit,tagJson.getString("tagType")));
            tagAdapter.notifyItemInserted(tagList.size() - 1);
            updateTagCountDisplay();
//            updateSubmitButtonState();
            System.out.println("null tag found"+res);
        }

    }


//    public void TagOps(){
//        JSONObject tagJson = Helper.TagJson();
//        tagJson.put("tagType","New Tags");
//        tagJson.put("rfidTag",tagId);
//        tagListArr.add(tagJson.toString());
//        tagList.add(new Tag(tagId, isOverLimit,tagJson.getString("tagType")));
//        tagAdapter.notifyItemInserted(tagList.size() - 1);
//        updateTagCountDisplay();
//        updateSubmitButtonState();
//    }

    @Override
    public void onTagDelete(String tagId) {
        tagListArr.remove(tagId);
        scannedTagIds.remove(tagId);
        updateTagCountDisplay();
        updateSubmitButtonState();
    }



    private boolean isAnyTagOverLimit() {
        for (Tag tag : tagList) {
            if (tag.isOverLimit()) {
                playOverLimitSound();
                return true;
            }
        }
        return false;
    }

    private void playOverLimitSound() {
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                Log.d(TAG, "Playing over-limit sound.");
            } else {
                Log.d(TAG, "Sound is already playing.");
            }
        } else {
            Log.e(TAG, "MediaPlayer is null, cannot play sound.");
        }
    }

    private void updateSubmitButtonState() {
//        boolean shouldDisable = isAnyTagOverLimit() || scannedTagIds.size() != totalInventoryToScan;
//        submitButton.setEnabled(!shouldDisable);
    }


    public class EventHandler implements RfidEventsListener {
        @Override
        public void eventReadNotify(RfidReadEvents e) {
            TagData[] myTags = reader.Actions.getReadTags(100);
            if (myTags != null) {
                for (TagData tagData : myTags) {
                    final String tagId = tagData.getTagID();
                    if (!scannedTagIds.contains(tagId)) {  // Check if the tag is not already scanned
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    addTagToList(tagId);
                                } catch (JSONException ex) {
                                    throw new RuntimeException(ex);
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
                    }
                }
            }
        }


        @Override
        public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
            Log.d(TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType());
            if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {
                    performInventoryOperationWithRetry();
                }
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                reader.Actions.Inventory.stop();
                            } catch (InvalidUsageException | OperationFailureException e) {
                                Log.e(TAG, "Exception: " + e.getMessage());
                            }
                            return null;
                        }
                    }.execute();
                }
            }
        }

        private void performInventoryOperationWithRetry() {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        if (!reader.getHostName().equals("CHARGING")) {
                            reader.Actions.Inventory.perform();
                        } else {
                            Log.e(TAG, "Operation not allowed: Reader is charging");
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Reader is charging. Operation not allowed.", Toast.LENGTH_SHORT).show());
                        }
                    } catch (InvalidUsageException | OperationFailureException e) {
                        Log.e(TAG, "Exception: " + e.getMessage());
                    }
                    return null;
                }
            }.execute();
        }
    }

    private void startExecutor() {
        try {
            JSONArray storyArray = new JSONArray(sessionManager.getCaseExcutor());
            for (int i = 0; i < storyArray.length(); i++) {
                JSONObject action = storyArray.getJSONObject(i);
                String actionType = action.getString("actionType");
                boolean isExcuted = action.getBoolean("isExcuted");
                if (!isExcuted) {
                    Log.d(TAG, "Action already executed: " + action);
                    action.put("isExcuted", true);
                    stroyId = action.getString("id");
                    sessionManager.setCheckTagOn(action.getString("caseName"));
                    totalInventoryToScan = parseInt(action.getString("scanQty"));
                    sessionManager.setSetScanCount(action.getString("scanQty"));
                    actionName.setText(action.getString("actionName"));

                    sessionManager.setCaseExcutor(storyArray.toString());

                    handleCase(actionType,action,storyArray);
                    break;
                }
                Log.d(TAG, "Final action triggered: " + action);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void saveDataUsingId (){
        try {
            JSONArray storyArray = new JSONArray(sessionManager.getCaseExcutor());
            for (int i = 0; i < storyArray.length(); i++) {
                JSONObject action = storyArray.getJSONObject(i);
                String id = action.getString("id");
                if (stroyId.equals(id)) {
                    Log.d(TAG, "Action already executed: " + action);
                    action.put("isExcuted", true);
                    action.put("data", tagListArr.toString());
                    sessionManager.setCaseExcutor(storyArray.toString());
                    break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleCase(String actionType,JSONObject item,JSONArray jsonArray) throws JSONException {
        switch (actionType) {
            case Constants.UPDATE:
                startActivity(new Intent(MainActivity.this, ActionActivity.class));
                break;
            default:
                Log.w(TAG, "Unknown action type: " + actionType);
                break;
        }
    }
}


