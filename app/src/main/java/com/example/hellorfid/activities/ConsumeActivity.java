package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.reader.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class    ConsumeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;
    public JSONArray tagArr;
    private ApiCallBackWithToken apiCallBackWithToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consume);

//        apiCallBackWithToken = new ApiCallBackWithToken(this);

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("totalInventory", 2);
//        intent.putExtra("apiUrl", Constants.addBulkTags);
//        startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);

        System.out.println("blalbalalblalbal");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MAIN_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result_key");
                try {
                    tagArr = new JSONArray(result);
                    processTagsAndHold();
                } catch (JSONException e) {
                    handleError("Error processing data", e);
                }
            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("result cancelled");
                navigateToHandheldTerminal("Operation cancelled");
            }
        }
    }

    private void processTagsAndHold() {
        new Thread(() -> {
            try {
                JSONArray processedTags = new JSONArray();
                CountDownLatch latch = new CountDownLatch(tagArr.length());
                AtomicBoolean allSuccessful = new AtomicBoolean(true);

                for (int i = 0; i < tagArr.length(); i++) {
                    String tag = tagArr.getString(i).toLowerCase();
                    JSONObject json = prepareJsonForTag(tag);

                    apiCallBackWithToken.Api(Constants.searchRfidTag, json, new ApiCallBackWithToken.ApiCallback() {
                        @Override
                        public JSONObject onSuccess(JSONObject responseJson) {
                            if (allSuccessful.get()) {
                                processSuccessResponse(responseJson, processedTags);
                            }
                            latch.countDown();
                            return responseJson;
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.e("TAG", "API call failed for tag: " + tag, e);
                            allSuccessful.set(false);
                            showToast("Unknown tags scan" + tag);
                            latch.countDown();
                        }
                    });
                }

                latch.await();

                if (allSuccessful.get()) {
                    hitHoldTagApi(processedTags.toString());
                } else {
                    runOnUiThread(() -> {
                        showToast("Unknown tags scan");
                        navigateToHandheldTerminal("Unknown tags scan");
                    });
                }

            } catch (Exception e) {
                handleError("Unknown tags scan", e);
            }
        }).start();
    }

    private JSONObject prepareJsonForTag(String tag) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("page", 1);
        json.put("limit", 1);
        JSONObject search = new JSONObject();
        search.put("rfidTag", tag);
        json.put("search", search);
        return json;
    }

    private void processSuccessResponse(JSONObject responseJson, JSONArray processedTags) {
        try {
            JSONArray content = responseJson.getJSONArray("content");
            JSONObject firstObject = content.getJSONObject(0);
            String _id = firstObject.getString("_id");
            String tag = firstObject.getString("rfidTag");
            JSONObject json = new JSONObject();
            json.put("_id", _id);
            json.put("rfidTag", tag);
            json.put("opreationStatus", "CONSUME");
            synchronized (processedTags) {
                processedTags.put(json);
            }
            showToast("Processed tag: " + tag);
        } catch (Exception e) {
            Log.e("TAG", "Error processing success response", e);
            showToast("Unknown tags scan");
        }
    }

    private void hitHoldTagApi(String tagArrString) {
        try {
            JSONArray newJson = new JSONArray(tagArrString);
            apiCallBackWithToken.Api(Constants.updateBulkTags, newJson, new ApiCallBackWithToken.ApiCallback() {
                @Override
                public JSONObject onSuccess(JSONObject responseJson) {
                    Log.i("TAG", "Tags Consume Successfully: " + responseJson);
                    navigateToHandheldTerminal("Tags Consume Successfully");
                    return responseJson;
                }

                @Override
                public void onFailure(Exception e) {
                    handleError("Unknown tags scan", e);
                }
            });
        } catch (JSONException e) {
            handleError("Unknown tags scan", e);
        }
    }

    private void navigateToHandheldTerminal(String message) {
        Intent intent = new Intent(ConsumeActivity.this, HandheldTerminalActivity.class);
        startActivity(intent);
        showToast(message);
    }

    private void handleError(String message, Exception e) {
        Log.e("TAG", message, e);
        showToast(message);
        navigateToHandheldTerminal(message);
    }

    private void showToast(final String message) {
        runOnUiThread(() -> {
            if (!isFinishing()) {
                Toast.makeText(ConsumeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}