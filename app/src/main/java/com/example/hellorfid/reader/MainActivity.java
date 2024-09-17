package com.example.hellorfid.reader;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellorfid.activities.AddBatchFormActivity;
import com.example.hellorfid.activities.BatchActivity;
import com.example.hellorfid.activities.HandheldTerminalActivity;
import com.example.hellorfid.constants.Constants;
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
import com.zebra.rfid.api3.Antennas;
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
//    private SessionManagerBag sessionManagerBag;
    private SessionManager sessionManager;

    private int totalInventoryToScan;
    private String apiUrl;
    private ApiCallBackWithToken apiCallBackWithToken;

    private Button resetButton;
    private Button submitButton;
    private EventHandler eventHandler;
    private Set<String> scannedTagIds;

    private RecyclerView recyclerView;
    private TagAdapter tagAdapter;
    private List<Tag> tagList;
    private MediaPlayer mediaPlayer;
    private ArrayList tagListArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiCallBackWithToken = new ApiCallBackWithToken(this);


        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer = MediaPlayer.create(this, R.raw.invalid_tag);
//        sessionManagerBag = new SessionManagerBag(this);
        sessionManager = new SessionManager(this);

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



//        ImageView AllScreenBackBtn = findViewById(R.id.all_screen_back_btn);
//        AllScreenBackBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, AddBatchActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//            }
//        });

        tagTextView = findViewById(R.id.totalScan1);
//        sessionManagerBag = new SessionManagerBag(this);

        totalInventoryToScan = getIntent().getIntExtra("totalInventory",0);
        apiUrl = getIntent().getStringExtra("apiUrl");

        Log.d(TAG, "Total tags to scan initialized to: " + totalInventoryToScan);

        resetButton = findViewById(R.id.submitButton1);
        submitButton = findViewById(R.id.submitButton);
        scannedTagIds = new HashSet<>();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetScannedTags();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("new array list---"+tagListArr.toString());

                Intent resultIntent = new Intent();
                resultIntent.putExtra("result_key", tagListArr.toString());
                setResult(RESULT_OK, resultIntent);
                finish();

//                JSONArray submitJson = createSubmitJson();
//                ApiHit(submitJson);
            }
        });
        initializeRecyclerView();

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
                            if (!reader.isConnected()) {
                                reader.connect();
                                ConfigureReader();
                                return true;
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

    public void ApiHit(JSONArray submitJson ){
        try {

            apiCallBackWithToken.Api(apiUrl, submitJson, new ApiCallBackWithToken.ApiCallback() {
                @Override
                public JSONObject onSuccess(JSONObject responseJson) {
                    System.out.println("onclick----submitJson"+submitJson);
                    batchUpdate(sessionManager.getBatch());
                    return responseJson;
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("TAG", "API call failed", e);
                }
            });


        } catch (Exception e) {
            Log.e(TAG, "Error in submit button onClick", e);
            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void batchUpdate(String batchData){

        System.out.println("onclick----submitcallJson"+batchData);

        try {
            // Extract the "data" object from the response

            JSONObject jsonObject = new JSONObject(batchData);

            String batchId = jsonObject.getString("batchId");
            JSONObject batchUpdateJson = new JSONObject();
            batchUpdateJson.put("id" , batchId);
            batchUpdateJson.put("isTagAdded" , true);
            System.out.println("batchUpdateJson-----"+batchUpdateJson);
            apiCallBackWithToken.Api(Constants.updateBatch, batchUpdateJson, new ApiCallBackWithToken.ApiCallback() {
                @Override
                public JSONObject onSuccess(JSONObject responseJson) {
                    System.out.println("responseJson-----dsfdsf"+responseJson);
                    Intent intent = new Intent(MainActivity.this, BatchActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Batch added successfully", Toast.LENGTH_SHORT).show();
                    return responseJson;
                }
                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "API call failed", e);
                }
            });

        } catch (JSONException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("id------"+batchData);
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
        updateTagCountDisplay();
        updateSubmitButtonState();

    }

    private JSONArray createSubmitJson() {
        JSONArray submitJson;
        try {
            System.out.println("scannedTagIds: " + scannedTagIds);
            System.out.println("apiUrl: " + apiUrl);

            submitJson = new JSONArray();

            String batch = sessionManager.getBatch();
            JSONObject batchObject = new JSONObject(batch);

            for (String rfidTag : scannedTagIds) {
                JSONObject tagObject = new JSONObject();
                tagObject.put("rfidTag", rfidTag.toLowerCase());

                System.out.println("batchObject---" + batchObject);
                System.out.println("tagObject---" + tagObject);

                Iterator<String> keys = batchObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (!key.equals("totalInventory")) {
                        tagObject.put(key, batchObject.get(key));
                    }
                }

                submitJson.put(tagObject);
            }

            System.out.println("onclick----submitJson" + submitJson.toString());

        } catch (Exception e) {
            Log.e("TAG", "Error creating JSON object", e);
            return null;
        }
        System.out.println("submitJson----"+submitJson) ;
        return submitJson;
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
            try {
                if (eventHandler == null) eventHandler = new EventHandler();
                reader.Events.addEventsListener(eventHandler);
                reader.Events.setHandheldEvent(true);
                reader.Events.setTagReadEvent(true);
                reader.Events.setAttachTagDataWithReadEvent(false);
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
                reader.Config.setStartTrigger(triggerInfo.StartTrigger);
                reader.Config.setStopTrigger(triggerInfo.StopTrigger);
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

    public void addTagToList(String tagId) {
        scannedTagIds.add(tagId);  // Add the new tag first
        int currentSize = scannedTagIds.size();
        boolean isOverLimit = currentSize > totalInventoryToScan;

        tagListArr.add(tagId);

        tagList.add(new Tag(tagId, isOverLimit));
        tagAdapter.notifyItemInserted(tagList.size() - 1);

        updateTagCountDisplay();
        updateSubmitButtonState();

    }

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
        boolean shouldDisable = isAnyTagOverLimit() || scannedTagIds.size() != totalInventoryToScan;
        submitButton.setEnabled(!shouldDisable);
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
                                addTagToList(tagId);
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
}


