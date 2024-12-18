package com.example.hellorfid.constants;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.hellorfid.activities.OrderActivity;
import com.example.hellorfid.activities.WhichProductLocationOrZone;
import com.example.hellorfid.dump.ApiCallBackWithToken;
import com.example.hellorfid.model.CommanModel;
import com.example.hellorfid.session.SessionManager;
import com.example.hellorfid.utils.JwtDecoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Helper {

    private static SessionManager sessionManager;



    public static String commanParser(String data, Boolean obj, CommanModel commanModel,Context context,JSONArray productIds) throws JSONException {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }

        System.out.println("data- >>>> "+data);

        if (!obj) {
            // Get the JSONArray from the data
            JSONArray jsonArray = tag(data);
            System.out.println("jsonArray....."+jsonArray);
            JSONArray finalArray = new JSONArray();

            JSONObject product = productIds.getJSONObject(0);
            JSONObject productId = product.getJSONObject("productId");

            // Loop through the JSONArray
            for (int i = 0; i < jsonArray.length(); i++) {
                String rfidTag = jsonArray.getString(i);


                System.out.println("i>>>>>"+productId.getString("_id"));

                try {



                    JSONObject jsonObject = new JSONObject(Constants.addTagJson);

                    // Add key-value pairs to the JSONObject
                    jsonObject.put("batchId", commanModel.getBatchID());
                    jsonObject.put("rfidTag", rfidTag.toLowerCase());
                    jsonObject.put("orderId", commanModel.getOrderId());
                    jsonObject.put("currentLocation", sessionManager.getBuildingId());
                    jsonObject.put("buildingId", sessionManager.getBuildingId());
                    jsonObject.put("tagPlacement", productId.getString("_id"));
                    jsonObject.put("product_id", productId.getString("_id"));
                    jsonObject.put("tagType", "Inventory");
                    jsonObject.put("tagInfo", productId.getString("productName"));
                    jsonObject.put("dispatchTo", commanModel.getDispatchTo());
                    jsonObject.put("readerId", null);
                    jsonObject.put("status", "EMPTY");
                    jsonObject.put("movementStatus", commanModel.getMovementStatus());
                    // Convert the JSONObject to a string and print it
                    finalArray.put(jsonObject);
                } catch (JSONException e) {
                    System.out.println("error Create new scratch file from selection"+e.getMessage());
                    e.printStackTrace();
                }
            }

            return finalArray.toString();
        }

        return null;
    }


//    public static JSONObject commanUpdate(ApiCallBackWithToken apiCallBackWithToken, String URL,
//                                          String whereKey,String wherevalue,String andKey, String andValue) throws JSONException {
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put(whereKey, wherevalue);
//                    jsonObject.put(andKey, andValue);
//                    return internalHit(apiCallBackWithToken,URL,jsonObject);
//                } catch (JSONException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//        return null;
//    }
//

    public static JSONObject commanUpdate(ApiCallBackWithToken apiCallBackWithToken, String URL,JSONObject jsonObject) throws JSONException, InterruptedException {

        return internalHit(apiCallBackWithToken, URL, jsonObject);
    }

    static JSONArray tag(String data) throws JSONException {
        return new JSONArray(data);
    }

    public static JSONObject getSearchJson(int page,int limit,JSONObject search ) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("page", page);
        requestBody.put("limit", limit);
        requestBody.put("search", search);

        return  requestBody;
    }

    public static JSONObject commanHitApi(ApiCallBackWithToken apiCallBackWithToken, String URL, String requestBody) throws JSONException, InterruptedException {
        JSONArray jsonArray = new JSONArray(requestBody);

        final JSONObject[] result = new JSONObject[1];
        final boolean[] isComplete = {false};

        apiCallBackWithToken.Api(URL, jsonArray, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                System.out.println("Response---->>>>>" + responseJson);
                result[0] = responseJson;
                isComplete[0] = true;
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("TAG", "API call failed", e);
                isComplete[0] = true;
            }
        });

        // Wait for the API call to complete
        while (!isComplete[0]) {
            Thread.sleep(100);
        }

        return result[0];
    }


    private static JSONObject internalHit(ApiCallBackWithToken apiCallBackWithToken, String URL, JSONObject requestBody) throws JSONException, InterruptedException {
//        JSONObject jsonObject = new JSONObject(requestBody);
        final JSONObject[] result = new JSONObject[1];
        final boolean[] isComplete = {false};

        System.out.println("requestBody---"+requestBody);

        apiCallBackWithToken.Api(URL, requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                System.out.println("Response---->>>>>" + responseJson);
                result[0] = responseJson;
                isComplete[0] = true;
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("TAG", "API call failed", e);
                isComplete[0] = true;
            }
        });

        // Wait for the API call to complete
        while (!isComplete[0]) {
            Thread.sleep(100);
        }

        return result[0];
    }


    public static JSONObject commanHitApi(ApiCallBackWithToken apiCallBackWithToken, String URL, JSONObject requestBody,Boolean obj) throws JSONException, InterruptedException {
//        JSONObject jsonObject = new JSONObject(requestBody);
        final JSONObject[] result = new JSONObject[1];
        final boolean[] isComplete = {false};
        apiCallBackWithToken.Api(URL, requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                System.out.println("Response---->>>>>" + responseJson);
                result[0] = responseJson;
                isComplete[0] = true;
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("TAG", "API call failed", e);
                isComplete[0] = true;
            }
        });

        // Wait for the API call to complete
        while (!isComplete[0]) {
            Thread.sleep(100);
        }

        return result[0];
    }



    public static JSONObject commanHitApi(ApiCallBackWithToken apiCallBackWithToken, String URL, JSONObject requestBody) throws JSONException, InterruptedException {
//        JSONObject jsonObject = new JSONObject(requestBody);
        System.out.println("response json tag 2---->>>"+requestBody.toString());
        final JSONObject[] result = new JSONObject[1];
        final boolean[] isComplete = {false};
        apiCallBackWithToken.Api(URL, requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                System.out.println("Response---->>>>>" + responseJson);
                result[0] = responseJson;
                isComplete[0] = true;
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("API call failed -->>>>"+URL+ e);
//                Log.e("TAG", "API call failed -->"+URL, e );
                isComplete[0] = true;
            }
        });

        // Wait for the API call to complete
        while (!isComplete[0]) {
            Thread.sleep(100);
        }

        return result[0];
    }

    public static JSONObject commanHitApi(ApiCallBackWithToken apiCallBackWithToken, String URL, JSONArray requestBody) throws JSONException, InterruptedException {
//        JSONObject jsonObject = new JSONObject(requestBody);
        System.out.println("response json tag 2---->>>"+requestBody.toString());
        final JSONObject[] result = new JSONObject[1];
        final boolean[] isComplete = {false};
        apiCallBackWithToken.Api(URL, requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {
                System.out.println("Response---->>>>>" + responseJson);
                result[0] = responseJson;
                isComplete[0] = true;
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("TAG", "API call failed", e);
//                Toast.makeText();
                isComplete[0] = true;

            }
        });

        // Wait for the API call to complete
        while (!isComplete[0]) {
            Thread.sleep(100);
        }

        return result[0];
    }


    public static JSONObject setCommanData(ApiCallBackWithToken apiCallBackWithToken, String URL, JSONObject requestBody) throws JSONException, InterruptedException {
//        JSONObject jsonObject = new JSONObject(requestBody);
        System.out.println("Check Request 1"+ requestBody+ " URL:"+URL);
        final JSONObject[] result = new JSONObject[1];
        final boolean[] isComplete = {false};
        apiCallBackWithToken.Api(URL, requestBody, new ApiCallBackWithToken.ApiCallback() {
            @Override
            public JSONObject onSuccess(JSONObject responseJson) {

                System.out.println("Check Request 2"+ responseJson);
                result[0] = responseJson;
                isComplete[0] = true;
                return responseJson;
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Check Request 3"+ e);

                Log.e("TAG", "API call failed", e);
                isComplete[0] = true;
            }
        });

        // Wait for the API call to complete
        while (!isComplete[0]) {
            Thread.sleep(100);
        }

        System.out.println("Check Request 4"+ "Completed "+result[0] );

        return result[0];
    }


    public static JSONObject lookingForCheck(JSONArray jsonArray,JSONArray compare,JSONArray err,JSONObject object) throws JSONException {
        boolean isCheck = true;
        JSONArray reponse = new JSONArray();
        JSONObject outJson = new JSONObject();
        System.out.println("JSON-----CHEK"+jsonArray.toString());
        for (int i = 0; i < jsonArray.length(); i++) {

            for(int n=0;n<compare.length();n++){

                System.out.println("loop check type "+jsonArray.getString(i));
                System.out.println("loop check checkKey "+compare.getString(i));
                System.out.println("loop check error "+err.getString(i));
                System.out.println("loop check actualData "+object.getString(compare.getString(i)));
                System.out.println("loop check ");
                System.out.println("loop check  *****************************************");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("check",jsonArray.getString(i).equals(object.getString(compare.getString(n))));
                jsonObject.put("error",err.getString(n));
                jsonObject.put("checkingTo",compare.getString(n));
                jsonObject.put("compareValue",jsonArray.getString(i));
                jsonObject.put("checkingWith",object.getString(compare.getString(n)));
                jsonObject.put("tag",object.getString("rfidTag"));
                System.out.println("object check"+jsonObject);
                reponse.put(jsonObject);
            }

        }
        try {
            for (int i = 0; i < reponse.length(); i++) {
                if (!reponse.getJSONObject(i).getBoolean("check")){
                    return reponse.getJSONObject(i);
                }
            }
            JSONObject allOK = new JSONObject();
            allOK.put("check",true);
            return allOK;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }


    public static void setDataInSession(SessionManager sessionManager,String jsonString){
        System.out.println("Check Request 5.1 "+jsonString);
        sessionManager.setCommandata(jsonString);
    }

    public static void setUserDetails(SessionManager sessionManager){

        JSONObject decodedToken = null;
        try {
            decodedToken = JwtDecoder.decoded(sessionManager.getToken());
            System.out.println("decodedToken-- "+decodedToken);
            String userId = decodedToken.getString("userId");

            sessionManager.setUserId(userId);// Assuming 'sub' claim contains the user ID
            System.out.println("userId"+userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("decodedToken"+decodedToken);
    }

    public static JSONObject TagJson () {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("batchId", "NA");
            jsonObject.put("rfidTag", "NA");
            jsonObject.put("product_id", "NA");
            jsonObject.put("currentLocation", "NA");
            jsonObject.put("dispatchTo", "NA");
            jsonObject.put("batchNumber", "NA");
            jsonObject.put("orderId", "NA");
            jsonObject.put("opreationStatus", "NA");
            jsonObject.put("buildingId", "NA");
            jsonObject.put("readerId", "NA");
            jsonObject.put("status", "NA");
            jsonObject.put("zoneIds", "NA");
            jsonObject.put("buildingIds", "NA");
            jsonObject.put("locationIds", "NA");
            jsonObject.put("movementStatus", "NA");
            jsonObject.put("batching", false);
            jsonObject.put("weight", 0);
            jsonObject.put("isError", false);
            jsonObject.put("tagType", "NA");
            jsonObject.put("tagInfo", "NA");
            jsonObject.put("tagPlacement", "NA");
            jsonObject.put("tagMovementInfo", "NA");
            jsonObject.put("tagMovementTime", "NA");
            jsonObject.put("tagLotNumber", "NA");
            jsonObject.put("tagWeight", 0);
            jsonObject.put("tagWeightUnit", "NA");
            jsonObject.put("tagWeightInfo", "NA");
            jsonObject.put("createdBy", "NA");
            jsonObject.put("updatedBy", "NA");
            // Helper method to add non-empty strings
            // Use the jsonObject as needed
            String jsonString = jsonObject.toString();
            System.out.println(jsonString);

            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }




    }





