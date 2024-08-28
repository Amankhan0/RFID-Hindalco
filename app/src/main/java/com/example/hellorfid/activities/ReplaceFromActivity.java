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
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;

public class ReplaceFromActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MAIN_ACTIVITY = 1001;
    public JSONArray tagArr;
    private ApiCallBackWithToken apiCallBackWithToken;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_replacefrom);

        sessionManager = new SessionManager(this);

        apiCallBackWithToken = new ApiCallBackWithToken(this);

        if(sessionManager.getReplaceFirstScan() == null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("totalInventory", 1);
            intent.putExtra("apiUrl", Constants.addBulkTags);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, REQUEST_CODE_MAIN_ACTIVITY);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MAIN_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result_key");
                try {
                    if (result != null) {
                        tagArr = new JSONArray(result);
                        if (tagArr.length() > 0) {
                            String tag = tagArr.getString(0).toLowerCase();
                            sessionManager.setReplaceFirstScan(tag);
                            Log.d("ReplaceFromActivity successfully", "Tag processed successfully: " + tag);
                            Intent intent = new Intent(this, ReplaceToActivity.class);
                            startActivity(intent);
                        } else {
                            Log.e("ReplaceFromActivity empty", "Tag array is empty");
                        }
                    } else {
                        Log.e("ReplaceFromActivity null", "Result is null");
                    }
                } catch (JSONException e) {
                    Log.e("ReplaceFromActivity Error", "JSON Parsing Error", e);
                    Intent intent = new Intent(this, HandheldTerminalActivity.class);
                    startActivity(intent);
                    runOnUiThread(() -> {
                        Toast.makeText(ReplaceFromActivity.this, "Operation failed due to JSON error", Toast.LENGTH_LONG).show();
                    });
                    throw new RuntimeException(e);
                }
                System.out.println("result---" + result);

            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("result cancelled");
                Intent intent = new Intent(this, HandheldTerminalActivity.class);
                startActivity(intent);
                runOnUiThread(() -> {
                    Toast.makeText(ReplaceFromActivity.this, "Operation cancelled", Toast.LENGTH_LONG).show();
                });
            }
        }
    }
}