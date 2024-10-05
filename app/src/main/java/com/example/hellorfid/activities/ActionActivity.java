package com.example.hellorfid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hellorfid.R;
import com.example.hellorfid.constants.CaseExecutorHandler;
import com.example.hellorfid.constants.Constants;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.reader.MainActivity;
import com.example.hellorfid.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActionActivity extends AppCompatActivity {

    ApiCallBackWithToken apiCallBackWithToken;
    SessionManager sessionManager;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_action);
        sessionManager = new SessionManager(this);
        System.out.println("result---->>>"+sessionManager.getCaseExcutor());
        System.out.println("result---->>>"+sessionManager.getStory());
        apiCallBackWithToken = new ApiCallBackWithToken(this);
        sessionManager.setPendingOps("true");
        tv = findViewById(R.id.activityTextView);
//        tv.setText("Executing Case");
        try {
            CaseExecute();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public void CaseExecute() throws JSONException, InterruptedException {
        JSONObject jsonObject = new JSONObject(sessionManager.getStory());
        switch (jsonObject.getString("caseName")) {
            case "Hold Location":
          //      JSONObject res = CaseExecutorHandler.holdUsingLocationTag(sessionManager.getCaseExcutor(),apiCallBackWithToken);
                System.out.println("result---->>> excuted case "+jsonObject.toString());
                break;
            case "Unhold Location":
                JSONObject res1 = CaseExecutorHandler.unHoldUsingLocationTag(sessionManager.getCaseExcutor(),apiCallBackWithToken);
                System.out.println("result---->>> excuted case "+res1);
                break;
            case Constants.INBOUND:
                JSONObject res2 = CaseExecutorHandler.order(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager);
                System.out.println("result---->>> excuted case "+res2);
                break;
            case Constants.OUTBOUND:
                JSONObject res12 = CaseExecutorHandler.order(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager);
                System.out.println("result---->>> excuted case "+res12);
                break;
            case Constants.VEHICLE:
                JSONObject res3 = CaseExecutorHandler.vehicleMap(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager);
                System.out.println("result---->>> excuted case "+res3);
                break;
            case Constants.ZONE:
                JSONObject res4 = CaseExecutorHandler.zoneCase(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager);
                System.out.println("result---->>> excuted case "+res4);
                break;
            case Constants.LOCATION:
                JSONObject res5 = CaseExecutorHandler.locationCase(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager);
                System.out.println("result---->>> excuted case location "+res5);
                break;
            case Constants.REPLACE:
                JSONObject res6 = CaseExecutorHandler.replcaeCase(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager);
                System.out.println("result---->>> excuted case replace ");
                break;
            case Constants.HOLD+"_"+Constants.INVENTORY :
                JSONObject res7 = CaseExecutorHandler.inventoryStatusUpdate(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager,Constants.HOLD);
                System.out.println("result---->>> excuted case hold ");
                break;
            case Constants.UNHOLD+"_"+Constants.INVENTORY :
                JSONObject res8 = CaseExecutorHandler.inventoryStatusUpdate(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager,Constants.ACTIVE);
                System.out.println("result---->>> excuted case hold ");
                break;
            case Constants.CYCLE_COUNT:
                JSONObject res9 = CaseExecutorHandler.cycleCount(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager,Constants.ACTIVE);
                System.out.println("result---->>> CYCLE COUNT ");
                break;
            case Constants.MOVE:
                JSONObject res10 = CaseExecutorHandler.moveInventoryToLocation(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager,Constants.ACTIVE);
                System.out.println("result---->>> Move ");
                break;

            case Constants.OPERATION_STATUS_CHANGE:
                JSONObject res11 = CaseExecutorHandler.inventoryStatusUpdate(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager,sessionManager.getOptionSelected());
                tv.setText(res11.toString());
                System.out.println("OPERATION_STATUS_CHANGE called");
                break;
            case Constants.RECHECK:
                JSONObject res13 = CaseExecutorHandler.reCheckOrder(sessionManager.getCaseExcutor(),apiCallBackWithToken,sessionManager,sessionManager.getOptionSelected());
                tv.setText(res13.toString());
                System.out.println("OPERATION_STATUS_CHANGE called");
                break;
            default:
                break;
        }
    }
}