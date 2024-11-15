package com.example.hellorfid.reader;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellorfid.R;
import com.example.hellorfid.activities.ActionActivity;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.constants.Helper;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.dump.Tag;
import com.example.hellorfid.dump.TagAdapter;
import com.example.hellorfid.session.SessionManager;
import com.zebra.rfid.api3.POWER_EVENT;

import com.zebra.rfid.api3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements TagAdapter.OnTagDeleteListener {

    private static final String TAG = "DEMO";
    private static Readers readers;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    private static RFIDReader reader;
private POWER_EVENT power_event;
    private TextView successScanTextView;
    private TextView errorScanTextView;
    private String stroyId;
    private SessionManager sessionManager;
    private int totalInventoryToScan = 0;
    private ApiCallBackWithToken apiCallBackWithToken;
    private Button resetButton;
    public static Button submitButton;
    public static Button failButton;

    private EventHandler eventHandler;
    private Set<String> scannedTagIds;

    private RecyclerView recyclerView;
    private TagAdapter successTagAdapter;
    private TagAdapter errorTagAdapter;
    private List<Tag> successTagList;
    private List<Tag> errorTagList;
    private boolean isShowingSuccessTags = true;

    private MediaPlayer mediaPlayer;
    private ArrayList tagListArr;

    private TextView actionName;
    JSONArray lookingFor;
    JSONArray keyCheck;
    String supportData;
    JSONArray errorCode;

//    private static final int MIN_POWER = 0;
//    private static final int MAX_POWER = 700; // Example max power, may vary by device
//    private static final int POWER_STEP = 10;


    private TextView currentPowerTextView;
    private SeekBar rangeSeekBar;

    private static final int MIN_POWER = 0;
    private static final int MAX_POWER = 300;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyCheck = new JSONArray();
        lookingFor = new JSONArray();
        errorCode = new JSONArray();
        sessionManager = new SessionManager(this);


//        increaseRangeButton = findViewById(R.id.increaseRangeButton);
//        decreaseRangeButton = findViewById(R.id.decreaseRangeButton);
//        currentPowerTextView = findViewById(R.id.currentPowerTextView);

        rangeSeekBar = findViewById(R.id.rangeSeekBar);
//        currentPowerTextView = findViewById(R.id.currentPowerTextView);


        setupRangeSeekBar();


        apiCallBackWithToken = new ApiCallBackWithToken(this);

        actionName = findViewById(R.id.actionName);


        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer = MediaPlayer.create(this, R.raw.invalid_tag);
        actionName.setText("Scanning for " + sessionManager.getCheckTagOn());
        startExecutor();
        initializeRecyclerView();
        tagListArr = new ArrayList<>();

        successScanTextView = findViewById(R.id.totalScan1);
        errorScanTextView = findViewById(R.id.errorScan);

        successScanTextView.setOnClickListener(v -> toggleTagView(true));
        errorScanTextView.setOnClickListener(v -> toggleTagView(false));



        totalInventoryToScan = parseInt(sessionManager.getSetScanCount() == null ? "0" : sessionManager.getSetScanCount());

        failButton = findViewById(R.id.failButton);
        resetButton = findViewById(R.id.submitButton1);
        submitButton = findViewById(R.id.submitButton);
        scannedTagIds = new HashSet<>();
        submitButton.setEnabled(false);

        if (sessionManager.getOptionSelected().equals(Constants.RECHECK)){
            failButton.setVisibility(View.VISIBLE);
            failButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("orderId", sessionManager.getOrderData().getString("_id"));
                        jsonObject.put("orderStatus", Constants.RECHECK_FAILED);
                        jsonObject.put("operationStatus", Constants.RECHECK_FAILED);
                        JSONObject res = Helper.commanUpdate(apiCallBackWithToken, Constants.updateOrder,jsonObject);
                        if (res != null) {
                            Toast.makeText(getApplicationContext(), "Order Recheck Failed clicked", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


//                    try {
////                        saveDataUsingId();
////                        startExecutor();
////                        resetButton.performClick();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetScannedTags();
            }
        });


//        if (increaseRangeButton != null) {
//            increaseRangeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        increaseRange(reader);
//                        updateCurrentPowerDisplay();
//                    } catch (InvalidUsageException | OperationFailureException e) {
//                        System.out.println("increase button Exception----->"+e);
//                        Toast.makeText(MainActivity.this, "Failed to increase range", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } else {
//            Log.e("MainActivity", "increaseRangeButton is null");
//        }

//        if (decreaseRangeButton != null) {
//            decreaseRangeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        decreaseRange(reader);
//                        updateCurrentPowerDisplay();
//                        System.out.println("decreease button clicked ");
//                    } catch (InvalidUsageException | OperationFailureException e) {
//                        System.out.println("Exception----->"+e.getMessage());
//                        Toast.makeText(MainActivity.this, "Failed to decrease range", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } else {
//            Log.e("MainActivity", "decreaseRangeButton is null");
//        }


//
//        increaseRangeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    increaseRange(reader);
//                    updateCurrentPowerDisplay();
//                } catch (InvalidUsageException | OperationFailureException e) {
//                    Toast.makeText(MainActivity.this, "Failed to increase range", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        decreaseRangeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    decreaseRange(reader);
//                    updateCurrentPowerDisplay();
//                } catch (InvalidUsageException | OperationFailureException e) {
//                    Toast.makeText(MainActivity.this, "Failed to decrease range", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataUsingId();
                startExecutor();
                resetButton.performClick();
            }
        });

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

        updateScanCountsDisplay();
        updateSubmitButtonState();
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        successTagList = new ArrayList<>();
        errorTagList = new ArrayList<>();

        successTagAdapter = new TagAdapter(this, successTagList, this);
        errorTagAdapter = new TagAdapter(this, errorTagList, this);

        recyclerView.setAdapter(successTagAdapter);
    }

    private void toggleTagView(boolean showSuccessTags) {
        isShowingSuccessTags = showSuccessTags;
        recyclerView.setAdapter(isShowingSuccessTags ? successTagAdapter : errorTagAdapter);
        updateScanCountsDisplay();
    }

    private void resetScannedTags() {
        scannedTagIds.clear();
        successTagList.clear();
        errorTagList.clear();
        tagListArr.clear();
        successTagAdapter.notifyDataSetChanged();
        errorTagAdapter.notifyDataSetChanged();
        submitButton.setEnabled(false);
        updateScanCountsDisplay();
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
            try {
                if (eventHandler == null) eventHandler = new EventHandler();
                reader.Events.addEventsListener(eventHandler);
                reader.Events.setHandheldEvent(true);
                reader.Events.setTagReadEvent(true);
                reader.Events.setAttachTagDataWithReadEvent(false);
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
            } catch (InvalidUsageException | OperationFailureException e) {
                e.printStackTrace();
            }
        }
    }


    private void setupRangeSeekBar() {
        rangeSeekBar.setMax(MAX_POWER - MIN_POWER);
        rangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Do nothing here
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing here
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int currentPower = seekBar.getProgress() + MIN_POWER;
                updateCurrentPowerDisplay(currentPower);
                try {
                    setPower(reader, currentPower);
                } catch (InvalidUsageException | OperationFailureException e) {
                    Toast.makeText(MainActivity.this, "Failed to set range", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateCurrentPowerDisplay(int power) {
//        currentPowerTextView.setText("Current Power: " + power);
        Toast.makeText(getApplicationContext(), "Current Power: " + power, Toast.LENGTH_SHORT).show();
    }

    public static void setPower(RFIDReader reader, int power) throws InvalidUsageException, OperationFailureException {
        Antennas.AntennaRfConfig rfConfig = reader.Config.Antennas.getAntennaRfConfig(1);
        System.out.println("rfConfig" + rfConfig);
        rfConfig.setTransmitPowerIndex(power);
        reader.Config.Antennas.setAntennaRfConfig(1, rfConfig);
    }

    public static int getCurrentPower(RFIDReader reader) throws InvalidUsageException, OperationFailureException {
        Antennas.AntennaRfConfig rfConfig = reader.Config.Antennas.getAntennaRfConfig(1);
        return rfConfig.getTransmitPowerIndex();
    }

//    private void initializeRangeControl() {
//        try {
//            int currentPower = getCurrentPower(reader);
//            rangeSeekBar.setProgress(currentPower - MIN_POWER);
//            updateCurrentPowerDisplay(currentPower);
//        } catch (InvalidUsageException | OperationFailureException e) {
//            Toast.makeText(this, "Failed to get current power", Toast.LENGTH_SHORT).show();
//        }
//    }

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

    private void updateScanCountsDisplay() {
        runOnUiThread(() -> {
            successScanTextView.setText("Success scan: " + successTagList.size()+"/"+totalInventoryToScan);
            errorScanTextView.setText("Error scan: " + errorTagList.size());

            successScanTextView.setBackgroundResource(isShowingSuccessTags ? R.drawable.selected_background : R.drawable.unselected_background);
            errorScanTextView.setBackgroundResource(isShowingSuccessTags ? R.drawable.unselected_background : R.drawable.selected_background);
        });
    }

    public void addTagToList(String tagId) throws JSONException, InterruptedException {

        scannedTagIds.add(tagId);
        int currentSize = scannedTagIds.size();
        boolean isOverLimit = currentSize > totalInventoryToScan;
        JSONObject s = new JSONObject();
        s.put("rfidTag", tagId);
        JSONObject searchJson = Helper.getSearchJson(1, 1, s);
        JSONObject res = Helper.commanHitApi(apiCallBackWithToken, Constants.searchRfidTag, searchJson);
        System.out.println("res------>>>>>"+res);

        if (res != null) {

            if (res.has("content") && res.getJSONArray("content").length() > 0) {
                JSONObject obj = res.getJSONArray("content").getJSONObject(0);
                String tagType = obj.getString("tagType");
//                JSONObject check = Helper.lookingForCheck(lookingFor, keyCheck,errorCode,obj);
//                System.out.println("check----#####"+check);

                if(sessionManager.getOptionSelected().equals(Constants.INBOUND) || sessionManager.getOptionSelected().equals(Constants.OUTBOUND)){
                    //INBOUND START
                    if(sessionManager.getCheckTagOn().equals(Constants.LOCATION)) {
                        if(sessionManager.getBuildingId().equals(obj.getString("currentLocation")) && obj.getString("tagType").equals(Constants.LOCATION)) {
                            addSuccessTag(tagId, isOverLimit, obj);
                            tagCheckBtnEnable(obj,1);
                        }else {
                            obj.put("tagType",Constants.LOCATION.equals(obj.getString("tagType"))?"BLD_ERR":obj.getString("tagType"));
                            addErrorTag(tagId, isOverLimit, obj);
                        }
                    }else if(sessionManager.getCheckTagOn().equals(Constants.INVENTORY) && obj.getString("opreationStatus").equals(Constants.ACTIVE) || obj.getString("opreationStatus").equals(Constants.ORDER_RECEIVED_FR) ||
                            obj.getString("opreationStatus").equals(sessionManager.getOptionSelected().equals(Constants.INBOUND)?Constants.ORDER_RECEIVING:Constants.ORDER_PICKING)) {

                            String product_id = sessionManager.getProductData().getJSONObject("productId").getString("_id");

                        if(product_id.equals(obj.getString("product_id")) && obj.getString("tagType").equals(Constants.INVENTORY)) {
                            addSuccessTag(tagId, isOverLimit, obj);
                            if(successTagList.size()>0){
                                submitButton.setEnabled(true);
                            }
                        }else {
                            obj.put("tagType",Constants.INVENTORY.equals(obj.getString("tagType"))?"PD_ERR":obj.getString("tagType"));
                            addErrorTag(tagId, isOverLimit, obj);
                        }
                    }else {
                        obj.put("tagType",obj.getString("opreationStatus"));
                        addErrorTag(tagId, isOverLimit, obj);
                    }

                    //INBOUND END
                }
                else if(sessionManager.getOptionSelected().equals(Constants.RECHECK)){

                    if(sessionManager.getCheckTagOn().equals(Constants.INVENTORY) && obj.getString("opreationStatus").equals(Constants.DISPATCHED) || obj.getString("opreationStatus").equals(Constants.RECHECKING)) {
                        String order_id = sessionManager.getOrderData().getString("_id");
                        if(order_id.equals(obj.getString("orderId")) && obj.getString("tagType").equals(Constants.INVENTORY)) {
                           if(errorTagList.size()==0){
                               submitButton.setEnabled(true);
                           } else{
                                 submitButton.setEnabled(false);
                           }
                            addSuccessTag(tagId, isOverLimit, obj);
                        }else {
                            if(!obj.getString("tagType").equals(Constants.VEHICLE)){
                                submitButton.setEnabled(false);
                                obj.put("tagType",Constants.INVENTORY.equals(obj.getString("tagType"))?"PD_ERR":obj.getString("tagType"));
                                addErrorTag(tagId, isOverLimit, obj);
                            }

                        }
                    }else {
                        obj.put("tagType",obj.getString("opreationStatus"));
                        addErrorTag(tagId, isOverLimit, obj);
                    }

                    //INBOUND END
                }else if(sessionManager.getOptionSelected().equals(Constants.CYCLE_COUNT)){
                    //INBOUND START
                    if(sessionManager.getCheckTagOn().equals(Constants.LOCATION)) {
                        if(sessionManager.getBuildingId().equals(obj.getString("currentLocation")) && obj.getString("tagType").equals(Constants.LOCATION)) {
                            addSuccessTag(tagId, isOverLimit, obj);
                            tagCheckBtnEnable(obj,1);
                        }else {
                            obj.put("tagType",Constants.LOCATION.equals(obj.getString("tagType"))?"BLD_ERR":obj.getString("tagType"));
                            addErrorTag(tagId, isOverLimit, obj);
                        }
                    }

                    if(sessionManager.getCheckTagOn().equals(Constants.INVENTORY)) {
                        if(sessionManager.getBuildingId().equals(obj.getString("currentLocation")) && obj.getString("tagType").equals(Constants.INVENTORY)) {
                            submitButton.setEnabled(true);
                            addSuccessTag(tagId, isOverLimit, obj);
                        }else {
                            obj.put("tagType",Constants.INVENTORY.equals(obj.getString("tagType"))?"BLD_ERR":obj.getString("tagType"));
                            addErrorTag(tagId, isOverLimit, obj);
                        }
                    }

                    //INBOUND END
                }else if(sessionManager.getOptionSelected().equals(Constants.HOLD) || sessionManager.getOptionSelected().equals(Constants.UNHOLD)){
                    //INBOUND START
                    if(sessionManager.getCheckTagOn().equals(Constants.LOCATION)) {
                        if(sessionManager.getBuildingId().equals(obj.getString("currentLocation")) && obj.getString("tagType").equals(Constants.LOCATION)) {
                            addSuccessTag(tagId, isOverLimit, obj);
                            tagCheckBtnEnable(obj,1);
                        }else {
                            obj.put("tagType",Constants.LOCATION.equals(obj.getString("tagType"))?"BLD_ERR":obj.getString("tagType"));
                            addErrorTag(tagId, isOverLimit, obj);
                        }
                    }else if(sessionManager.getCheckTagOn().equals(Constants.INVENTORY)) {
                        if(sessionManager.getBuildingId().equals(obj.getString("currentLocation")) && obj.getString("tagType").equals(Constants.INVENTORY)) {
                            submitButton.setEnabled(true);
                            addSuccessTag(tagId, isOverLimit, obj);
                        }else {
                            obj.put("tagType",Constants.INVENTORY.equals(obj.getString("tagType"))?"BLD_ERR":obj.getString("tagType"));
                            addErrorTag(tagId, isOverLimit, obj);
                        }
                    }

                    //INBOUND END
                }else if(sessionManager.getOptionSelected().equals(Constants.MOVE)){
                    //INBOUND START
                    if(sessionManager.getCheckTagOn().equals(Constants.LOCATION)) {
                        if(sessionManager.getBuildingId().equals(obj.getString("currentLocation")) && obj.getString("tagType").equals(Constants.LOCATION)) {
                            addSuccessTag(tagId, isOverLimit, obj);
                            tagCheckBtnEnable(obj,1);
                        }else {
                            obj.put("tagType",Constants.LOCATION.equals(obj.getString("tagType"))?"BLD_ERR":obj.getString("tagType"));
                            addErrorTag(tagId, isOverLimit, obj);
                        }
                    }else if(sessionManager.getCheckTagOn().equals(Constants.INVENTORY) ) {
                        JSONArray storyArray = new JSONArray(sessionManager.getCaseExcutor());
                        JSONObject action = storyArray.getJSONObject(0);
                        String data = action.getString("data");
                        JSONArray dataArray = new JSONArray(data);
                        JSONObject dataObj = dataArray.getJSONObject(0);
                        System.out.println("data hcek------->>>"+dataObj.getString("locationIds"));

                         String currentLocationId =  sessionManager.getCaseExcutor();
                         if(!dataObj.getString("locationIds").equals(obj.getString("locationIds"))) {
                             if (sessionManager.getBuildingId().equals(obj.getString("currentLocation")) && obj.getString("tagType").equals(Constants.INVENTORY)) {
                                 submitButton.setEnabled(true);
                                 addSuccessTag(tagId, isOverLimit, obj);
                             } else {
                                 obj.put("tagType", Constants.INVENTORY.equals(obj.getString("tagType")) ? "BLD_ERR" : obj.getString("tagType"));
                                 addErrorTag(tagId, isOverLimit, obj);
                             }
                         }else {
                             obj.put("tagType", Constants.INVENTORY.equals(obj.getString("tagType")) ? "SM_LOC_ERR" : obj.getString("tagType"));
                             addErrorTag(tagId, isOverLimit, obj);
                         }
                    }

                    //INBOUND END
                }else if(sessionManager.getOptionSelected().equals(Constants.REPLACE)){
                        sessionManager.setCheckTagOn(actionName.getText().toString());

                    if(actionName.getText().equals("REPLACE FROM")){
                        //INBOUND START
                        if (sessionManager.getBuildingId().equals(obj.getString("currentLocation")))
                        {
                            if (!obj.getString("tagType").equals(Constants.NEW_TAG)) {
                                addSuccessTag(tagId, isOverLimit, obj);
                                tagCheckBtnEnable(obj, 1);
                            } else {

                                addErrorTag(tagId, isOverLimit, obj);
                            }
                        }else {
                            obj.put("tagType", "BLD_ERR");
                            addErrorTag(tagId, isOverLimit, obj);
                        }
                    }


                    //INBOUND END
                }else if(sessionManager.getOptionSelected().equals(Constants.OPERATION_STATUS_CHANGE) ){
                    //INBOUND START
                    System.out.println("check opreaton"+sessionManager.getOptionSelected());

                        if(sessionManager.getBuildingId().equals(obj.getString("currentLocation")) && obj.getString("tagType").equals(Constants.INVENTORY)) {
                            submitButton.setEnabled(true);
                            addSuccessTag(tagId, isOverLimit, obj);
                        }else {
                            obj.put("tagType",Constants.INVENTORY.equals(obj.getString("tagType"))?"BLD_ERR":obj.getString("tagType"));
                            addErrorTag(tagId, isOverLimit, obj);
                        }


                    //INBOUND END
                }else if(sessionManager.getOptionSelected().equals("WEIGHING_SCALE")){

                    if(sessionManager.getCheckTagOn().equals(Constants.WeighingScale)) {
                        if(obj.getString("tagType").equals(Constants.WeighingScale)) {
                            addSuccessTag(tagId, isOverLimit, obj);
                            tagCheckBtnEnable(obj,1);
                        }else {
                            obj.put("tagType",Constants.LOCATION.equals(obj.getString("tagType"))?"BLD_ERR":obj.getString("tagType"));
                            addErrorTag(tagId, isOverLimit, obj);
                        }
                    }

                    if(sessionManager.getCheckTagOn().equals(Constants.NOZZLE)) {
                        if( obj.getString("tagType").equals(Constants.NOZZLE)) {
                            submitButton.setEnabled(true);
                            addSuccessTag(tagId, isOverLimit, obj);
                        }else {
                            obj.put("tagType",Constants.INVENTORY.equals(obj.getString("tagType"))?"BLD_ERR":obj.getString("tagType"));
                            addErrorTag(tagId, isOverLimit, obj);
                        }
                    }
                }else {
                    System.out.println("check opreaton"+sessionManager.getOptionSelected());
                    addErrorTag(tagId, isOverLimit, obj);
                }
            }
        } else {

            JSONObject tagJson = Helper.TagJson();
            tagJson.put("tagType", Constants.NEW_TAG);
            tagJson.put("rfidTag", tagId);
//            JSONObject check = Helper.lookingForCheck(lookingFor, keyCheck,errorCode,tagJson);
//            System.out.println("check----new tag CHeck#####"+check);
            if(sessionManager.getOptionSelected().equals(Constants.INBOUND)){

                if(sessionManager.getNewTagsAllowed()){
                    System.out.println("sessionManager.getNewTagsAllowed()-----" + sessionManager.getNewTagsAllowed());
                    if(tagJson.getString("tagType").equals(Constants.NEW_TAG) && sessionManager.getCheckTagOn().equals(Constants.INVENTORY)) {
                        System.out.println("check----new tag run CHeck#####");
//                        tagCheckBtnEnable(tagJson,parseInt(sessionManager.getProductData().getString("quantity")));

                        addSuccessTag(tagId, isOverLimit, tagJson);
                        if(successTagList.size()>0){
                            submitButton.setEnabled(true);
                        }
                    }else {
                        System.out.println("check----new tag fail CHeck#####");
                        addErrorTag(tagId, isOverLimit, tagJson);
                    }
                }else {
                    addErrorTag(tagId, isOverLimit, tagJson);
                }

            }else if(sessionManager.getOptionSelected().equals(Constants.MAPPING)){
                System.out.println("check Mapping---->>>"+sessionManager.getOptionSelected());
                if(tagJson.getString("tagType").equals(Constants.NEW_TAG)) {
                    System.out.println("check----new tag run CHeck#####");
                    addSuccessTag(tagId, isOverLimit, tagJson);
                    if(successTagList.size()==1){
                        submitButton.setEnabled(true);
                    }else {
                        submitButton.setEnabled(false);
                    }

                }else {
                    System.out.println("check----new tag fail CHeck#####");
                    addErrorTag(tagId, isOverLimit, tagJson);
                }
            }else if(sessionManager.getOptionSelected().equals(Constants.REPLACE)){
                if(actionName.getText().equals("REPLACE TO")){
                    sessionManager.setCheckTagOn(actionName.getText().toString());
                    addSuccessTag(tagId, isOverLimit, tagJson);
                    tagCheckBtnEnable(tagJson,1);
                }else {
                    addErrorTag(tagId, isOverLimit, tagJson);
                }
            }



            else {
                System.out.println("check Mapping error---->>>"+sessionManager.getOptionSelected());

                addErrorTag(tagId, isOverLimit, tagJson);
            }
        }

        updateScanCountsDisplay();
        updateSubmitButtonState();
    }

    public void addSuccessTag(String tagId, boolean isOverLimit, JSONObject obj) throws JSONException {
        Tag newTag = new Tag(tagId, isOverLimit, obj.getString("tagType"));
        successTagList.add(newTag);
        tagListArr.add(obj.toString());
        successTagAdapter.notifyItemInserted(successTagList.size() - 1);
//        submitButton.setEnabled(true);
    }

    public void addErrorTag(String tagId, boolean isOverLimit, JSONObject obj) throws JSONException {
        Tag newTag = new Tag(tagId, isOverLimit, obj.getString("tagType"));
        errorTagList.add(newTag);
        errorTagAdapter.notifyItemInserted(errorTagList.size() - 1);
//        submitButton.setEnabled(true);
    }

    public void tagCheckBtnEnable(JSONObject tagJson,int size) throws JSONException {

        if(successTagList.size()==size){
            submitButton.setEnabled(true);
        }else {
            tagJson.put("tagType","CNT_ERR");
            Toast.makeText(getApplicationContext(), "Please scan only one tag", Toast.LENGTH_SHORT).show();
            submitButton.setEnabled(false);
        }
    }

    @Override
    public void onTagDelete(String tagId) {
        tagListArr.remove(tagId);
        scannedTagIds.remove(tagId);

        boolean removedFromSuccess = removeTagFromList(successTagList, tagId);
        boolean removedFromError = removeTagFromList(errorTagList, tagId);

        if (removedFromSuccess) {
            successTagAdapter.notifyDataSetChanged();
        }
        if (removedFromError) {
            errorTagAdapter.notifyDataSetChanged();
        }

        updateScanCountsDisplay();
        updateSubmitButtonState();
    }

    private boolean removeTagFromList(List<Tag> list, String tagId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTagNumber().equals(tagId)) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }

    private boolean isAnyTagOverLimit() {
        for (Tag tag : successTagList) {
            if (tag.isOverLimit()) {
                playOverLimitSound();
                return true;
            }
        }
        for (Tag tag : errorTagList) {
            if (tag.isOverLimit()) {
                playOverLimitSound();
                return true;
            }
        }
        return false;
    }

    private void playOverLimitSound() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void updateSubmitButtonState() {
//        submitButton.setEnabled(!successTagList.isEmpty() || !errorTagList.isEmpty());
    }

    public class EventHandler implements RfidEventsListener {
        @Override
        public void eventReadNotify(RfidReadEvents e) {
            TagData[] myTags = reader.Actions.getReadTags(100);
            if (myTags != null) {
                for (TagData tagData : myTags) {
                    final String tagId = tagData.getTagID();
                    if (!scannedTagIds.contains(tagId)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    addTagToList(tagId);
                                } catch (JSONException | InterruptedException ex) {
                                    ex.printStackTrace();
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
        System.out.println("Excute Clicked");
        try {
            JSONArray storyArray = new JSONArray(sessionManager.getCaseExcutor());
            for (int i = 0; i < storyArray.length(); i++) {
                System.out.println("Excute Clicked in Loop");

                JSONObject action = storyArray.getJSONObject(i);
                String actionType = action.getString("actionType");
                boolean isExcuted = action.getBoolean("isExcuted");
                System.out.println("Excute Clicked in isExcuted "+isExcuted);

                if (!isExcuted) {
                    System.out.println("Excute Clicked in check "+isExcuted);

                    action.put("isExcuted", true);
                    stroyId = action.getString("id");
                    if(!action.getString("caseName").equals(Constants.UPDATE)){
                        sessionManager.setCheckTagOn(action.getString("caseName"));
                    }
                    totalInventoryToScan = parseInt(action.getString("scanQty"));
                    sessionManager.setSetScanCount(action.getString("scanQty"));
                    actionName.setText(action.getString("actionName"));
                    sessionManager.setCaseExcutor(storyArray.toString());
                    lookingFor = action.getJSONArray("lookingFor");
                    keyCheck = action.getJSONArray("keyCheck");
                    supportData = action.getString("supportData");

                    System.out.println("supportData--->"+sessionManager.getCaseExcutor());

                    System.out.println("action Type0000000>"+actionType);
                    handleCase(actionType, action, storyArray);
                    break;
                }else {
                    System.out.println("Excute Clicked in check fail "+isExcuted);

                }
            }
        } catch (JSONException e) {
            System.out.println("Excute Clicked in check fail "+e.getMessage());
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

    public void handleCase(String actionType, JSONObject item, JSONArray jsonArray) throws JSONException {
        switch (actionType) {
            case Constants.UPDATE:
                System.out.println("Action activity");
                startActivity(new Intent(MainActivity.this, ActionActivity.class));
                break;
            default:
                Log.w(TAG, "Unknown action type: " + actionType);
                break;
        }
    }
}


