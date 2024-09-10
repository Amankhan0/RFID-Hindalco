package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellorfid.R;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.reader.MainActivity;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReplaceMainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextView textViewReplaceFrom, textViewReplaceTo;
    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;
    public JSONArray tagArr;
    private Button replaceButtonSubmit;

    private ApiCallBackWithToken apiCallBackWithToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_replace_main);

        sessionManager = new SessionManager(this);
        apiCallBackWithToken = new ApiCallBackWithToken(this);

        textViewReplaceFrom = findViewById(R.id.textViewReplaceFrom);
        textViewReplaceTo = findViewById(R.id.textViewReplaceTo);

        replaceButtonSubmit = findViewById(R.id.replaceButtonSubmit);

        System.out.println("sessionManager 1 --- " + sessionManager.getReplaceFirstScan());
        System.out.println("sessionManager 2 --- " + sessionManager.getReplaceSecondScan());

        textViewReplaceFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReplaceMainActivity.this, MainActivity.class);
                intent.putExtra("totalInventory", 1);
                intent.putExtra("apiUrl", Constants.addBulkTags);
                startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);
            }
        });

        textViewReplaceTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sessionManager.getReplaceFirstScan() == null){
                    Toast.makeText(ReplaceMainActivity.this, "Select Replace From Bag First", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(ReplaceMainActivity.this, MainActivity.class);
                    intent.putExtra("totalInventory", 1);
                    intent.putExtra("apiUrl", Constants.addBulkTags);
                    startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);
                }
            }
        });

        replaceButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(sessionManager.getReplaceFirstScan() == null){
                   Toast.makeText(ReplaceMainActivity.this, "Select Replace From Bag", Toast.LENGTH_SHORT).show();
               }else if(sessionManager.getReplaceSecondScan() == null){
                   Toast.makeText(ReplaceMainActivity.this, "Select Replace To Bag", Toast.LENGTH_SHORT).show();
               }else{

                   JSONObject json = new JSONObject();
                   try {
                       json.put("page",1);
                       json.put("limit",5);
                       JSONObject search = new JSONObject();
                       search.put("rfidTag",sessionManager.getReplaceFirstScan());
                       json.put("search",search);
                   } catch (JSONException e) {
                       throw new RuntimeException(e);
                   }

                   apiCallBackWithToken.Api(Constants.searchRfidTag, json, new ApiCallBackWithToken.ApiCallback() {
                       @Override
                       public JSONObject onSuccess(JSONObject responseJson) {
                           System.out.println("responseJson------ new" + responseJson);
                           String replaceSecondScan = sessionManager.getReplaceSecondScan();
                           System.out.println("replaceSecondScan: " + replaceSecondScan);

                           JSONArray arr = new JSONArray();
                           JSONObject json = new JSONObject();

                           try {

                               JSONArray content = responseJson.getJSONArray("content");
                               String firstId = "";
                               if (content.length() > 0) {
                                   firstId = content.getJSONObject(0).getString("_id");
                                   System.out.println("ID of the first item: " + firstId);
                               } else {
                                   System.out.println("The content array is empty.");
                               }
                               
                               System.out.println("ID from responseJson: " + firstId);

                               if (!firstId.isEmpty() && replaceSecondScan != null && !replaceSecondScan.isEmpty()) {
                                   json.put("_id", firstId);
                                   json.put("rfidTag", replaceSecondScan);

                                   System.out.println("json object before adding to array: " + json);

                                   arr.put(json);
                                   System.out.println("arr after adding json: " + arr);

                                   apiCallBackWithToken.Api(Constants.updateBulkTags, arr, new ApiCallBackWithToken.ApiCallback() {
                                       @Override
                                       public JSONObject onSuccess(JSONObject responseJson) {
                                           try {
                                               if (responseJson.getInt("status") == 200) {
                                                   runOnUiThread(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           Toast.makeText(ReplaceMainActivity.this, "Bag Replace Successfully", Toast.LENGTH_SHORT).show();
                                                           Intent intent = new Intent(ReplaceMainActivity.this, HandheldTerminalActivity.class);
                                                           startActivity(intent);
                                                       }
                                                   });
                                               } else {
                                                   runOnUiThread(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           Toast.makeText(ReplaceMainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                       }
                                                   });
                                               }
                                           } catch (JSONException e) {
                                               e.printStackTrace();
                                               runOnUiThread(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       Toast.makeText(ReplaceMainActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                                                   }
                                               });
                                           }

                                           System.out.println("Response processed ---- responseJson: " + responseJson);
                                           return responseJson;
                                       }
                                       @Override
                                       public void onFailure(Exception e) {
                                           Log.e("TAG", "API call failed", e);
                                       }
                                   });



                               } else {
                                   System.out.println("Skipping array population due to empty id or replaceSecondScan");
                               }
                           } catch (JSONException e) {
                               e.printStackTrace();
                               System.out.println("Exception occurred: " + e.getMessage());
                           }

                           return responseJson;
                       }
                       @Override
                       public void onFailure(Exception e) {
                           Log.e("TAG", "API call failed", e);
                           runOnUiThread(() -> {
                               Toast.makeText(ReplaceMainActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                           });
                       }
                   });

                   System.out.println("sessionManager 1 --- " + sessionManager.getReplaceFirstScan());
                   System.out.println("sessionManager 2 --- " + sessionManager.getReplaceSecondScan());
               }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MAIN_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result_key");
                try {

                    tagArr = new JSONArray(result);
                    String tag = tagArr.getString(0).toLowerCase();

                    JSONObject json = new JSONObject();
                    json.put("page",1);
                    json.put("limit",5);
                    JSONObject search = new JSONObject();
                    search.put("rfidTag",tag);
                    json.put("search",search);

                    System.out.println("json"+json);

                    if(sessionManager.getReplaceFirstScan() == null){
                        apiCallBackWithToken.Api(Constants.searchRfidTag, json, new ApiCallBackWithToken.ApiCallback() {
                            @Override
                            public JSONObject onSuccess(JSONObject responseJson) {
                                System.out.println("responseJson------"+responseJson);
                                sessionManager.setReplaceFirstScan(tag);
                                runOnUiThread(() -> {
                                    try {
                                        JSONArray content = responseJson.getJSONArray("content");
                                        if (content.length() > 0) {
                                            JSONObject firstItem = content.getJSONObject(0);
                                            String batchNumber = firstItem.optString("batchNumber", "N/A");
                                            String rfidTag = firstItem.optString("rfidTag", "N/A");
                                            String displayText = String.format("batchNumber - <b>%s</b><br>Tag ID - <b>%s</b>", batchNumber, rfidTag);
                                            textViewReplaceFrom.setText(Html.fromHtml(displayText, Html.FROM_HTML_MODE_LEGACY));
                                        }
                                        Toast.makeText(ReplaceMainActivity.this, "Tag found successfully", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        Log.e("TAG", "JSON parsing error", e);
                                        Toast.makeText(ReplaceMainActivity.this, "Error processing response", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return responseJson;
                            }
                            @Override
                            public void onFailure(Exception e) {
                                Log.e("TAG", "API call failed", e);
                                runOnUiThread(() -> {
                                    if (e instanceof ApiCallBackWithToken.ApiException &&
                                            ((ApiCallBackWithToken.ApiException) e).getStatusCode() == 404) {
                                        Toast.makeText(ReplaceMainActivity.this, "Tag not found. Please scan a valid tag.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(ReplaceMainActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                    else{
                        System.out.println("json"+json);
                        apiCallBackWithToken.Api(Constants.searchRfidTag, json, new ApiCallBackWithToken.ApiCallback() {
                            @Override
                            public JSONObject onSuccess(JSONObject responseJson) {
                                System.out.println("responseJson--responseJson--");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ReplaceMainActivity.this, "You can't replace this tag", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return responseJson;
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("TAG", "API call failed", e);

                                runOnUiThread(() -> {
                                    if (e instanceof ApiCallBackWithToken.ApiException && ((ApiCallBackWithToken.ApiException) e).getStatusCode() == 404) {
                                        System.out.println("call--------" + tag);
                                        sessionManager.setReplaceSecondScan(tag);

                                        String displayText = String.format("Tag ID - <b>%s", tag);
                                        textViewReplaceTo.setText(Html.fromHtml(displayText, Html.FROM_HTML_MODE_LEGACY));
                                        Toast.makeText(ReplaceMainActivity.this, "Tag found successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ReplaceMainActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }

                } catch (JSONException e) {
                    Log.e("TAG", "JSON parsing error", e);
                    runOnUiThread(() -> {
                        Toast.makeText(ReplaceMainActivity.this, "Error processing scan result", Toast.LENGTH_SHORT).show();
                    });
                }
            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("result cancelled");
                Intent intent = new Intent(this, HandheldTerminalActivity.class);
                startActivity(intent);
                runOnUiThread(() -> {
                    Toast.makeText(ReplaceMainActivity.this, "Operation cancelled", Toast.LENGTH_LONG).show();
                });
            }
        }
    }
}