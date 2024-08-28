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

public class HoldActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;
    public JSONArray tagArr;
    private ApiCallBackWithToken apiCallBackWithToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hold);

        apiCallBackWithToken = new ApiCallBackWithToken(this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("totalInventory", 2);
        intent.putExtra("apiUrl", Constants.addBulkTags);
        startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MAIN_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result_key");

                try {
                    JSONArray tagArr = new JSONArray(result);
                    System.out.println("result" + tagArr);
                    JSONArray holdJsonArray = new JSONArray();
                    for (int i = 0; i < tagArr.length(); i++) {
                        JSONObject holdJson = new JSONObject();
                        holdJson.put("rfidTag", tagArr.getString(i).toLowerCase());
                        holdJson.put("opreationStatus", "HOLD");
                        holdJsonArray.put(holdJson);
                    }
                    System.out.println("holdJsonArray: " + holdJsonArray);

                    apiCallBackWithToken.Api(Constants.updateBulkTags, holdJsonArray, new ApiCallBackWithToken.ApiCallback() {
                        @Override
                        public JSONObject onSuccess(JSONObject responseJson) {
                            System.out.println("responseJson tag update----" + responseJson);
                            Intent intent = new Intent(HoldActivity.this, HandheldTerminalActivity.class);
                            startActivity(intent);
                            runOnUiThread(() -> {
                                Toast.makeText(HoldActivity.this, "Tags Hold Successfully", Toast.LENGTH_LONG).show();
                            });
                            return responseJson;
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Intent intent = new Intent(HoldActivity.this, HandheldTerminalActivity.class);
                            startActivity(intent);
                            runOnUiThread(() -> {
                                Toast.makeText(HoldActivity.this, "Failed to update", Toast.LENGTH_LONG).show();
                            });
                            Log.e("TAG", "API call failed", e);
                        }
                    });

                } catch (JSONException e) {
                    Intent intent = new Intent(HoldActivity.this, HandheldTerminalActivity.class);
                    startActivity(intent);
                    runOnUiThread(() -> {
                        Toast.makeText(HoldActivity.this, "Error processing data", Toast.LENGTH_LONG).show();
                    });
                    throw new RuntimeException(e);
                }

                System.out.println("result---" + result);

            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("result cancelled");
                Intent intent = new Intent(this, HandheldTerminalActivity.class);
                startActivity(intent);
                runOnUiThread(() -> {
                    Toast.makeText(HoldActivity.this, "Operation cancelled", Toast.LENGTH_LONG).show();
                });
            }
        }
    }
}