package com.example.hellorfid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.hellorfid.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
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
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TagData;
import com.zebra.rfid.api3.TriggerInfo;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final Logger log = Logger.getLogger(MainActivity.class);
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private static Readers readers;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    private static RFIDReader reader;
    private static final String TAG = "DEMO";
    private TextView tagTextView;
    private Button resetButton;
    private EventHandler eventHandler;
    private Set<String> scannedTagIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        tagTextView = findViewById(R.id.TagText);
        resetButton = findViewById(R.id.resetButton);
        scannedTagIds = new HashSet<>();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetScannedTags();
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
                            // get first reader from list
                            readerDevice = availableRFIDReaderList.get(0);
                            reader = readerDevice.getRFIDReader();
                            if (!reader.isConnected()) {
                                // Establish connection to the RFID Reader
                                reader.connect();
                                ConfigureReader();
                                return true;
                            }
                        }
                    }
                } catch (InvalidUsageException e) {
                    e.printStackTrace();
                } catch (OperationFailureException e) {
                    e.printStackTrace();
                    Log.d(TAG, "OperationFailureException " + e.getVendorMessage());
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    Toast.makeText(getApplicationContext(), "Reader Connected", Toast.LENGTH_SHORT).show();

                    tagTextView.setText("Reader connected");
                }
            }
        }.execute();
    }

    private void resetScannedTags() {
        scannedTagIds.clear();
        tagTextView.setText("Reader connected\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
                // receive events from reader
                if (eventHandler == null) eventHandler = new EventHandler();
                reader.Events.addEventsListener(eventHandler);
                // HH event
                reader.Events.setHandheldEvent(true);
                // tag event with tag data
                reader.Events.setTagReadEvent(true);
                // application will collect tag using getReadTags API
                reader.Events.setAttachTagDataWithReadEvent(false);
                // set trigger mode as RFID so scanner beam will not come
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
                // set start and stop triggers
                reader.Config.setStartTrigger(triggerInfo.StartTrigger);
                reader.Config.setStopTrigger(triggerInfo.StopTrigger);
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
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

    public class EventHandler implements RfidEventsListener {
        @Override
        public void eventReadNotify(RfidReadEvents e) {
            TagData[] myTags = reader.Actions.getReadTags(100);
            if (myTags != null) {
                for (TagData tagData : myTags) {
                    Log.d(TAG, "Tag ID " + tagData.getTagID());
                    if (tagData.getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ &&
                            tagData.getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS) {
                        if (tagData.getMemoryBankData().length() > 0) {
                            Log.d(TAG, "Mem Bank Data " + tagData.getMemoryBankData());
                        }
                    }

                    // Add the scanned tag ID to the set
                    final String tagId = tagData.getTagID();
                    scannedTagIds.add(tagId);

                    // Update the TextView with all scanned tag IDs
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder allTags = new StringBuilder("Reader connected\n");
                            for (String id : scannedTagIds) {
                                allTags.append("Tag ID: ").append(id).append("\n");
                            }
                            SpannableString spannableString = new SpannableString(allTags.toString());
                            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            tagTextView.setText(spannableString);
                        }
                    });
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
                            } catch (InvalidUsageException e) {
                                Log.e(TAG, "InvalidUsageException: " + e.getMessage());
                            } catch (OperationFailureException e) {
                                Log.e(TAG, "OperationFailureException: " + e.getMessage() + ", VendorMessage: " + e.getVendorMessage());
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
                    } catch (InvalidUsageException e) {
                        Log.e(TAG, "InvalidUsageException: " + e.getMessage());
                    } catch (OperationFailureException e) {
                        Log.e(TAG, "OperationFailureException: " + e.getMessage() + ", VendorMessage: " + e.getVendorMessage());
                    }
                    return null;
                }
            }.execute();
        }
    }
}
