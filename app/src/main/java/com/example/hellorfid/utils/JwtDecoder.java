package com.example.hellorfid.utils;
import android.util.Base64;
import android.util.Log;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class JwtDecoder {

    private static final String TAG = "JwtDecoder";

    public static JSONObject decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d(TAG, "Header: " + getJson(split[0]));
            Log.d(TAG, "Body: " + getJson(split[1]));

            return new JSONObject(getJson(split[1]));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error decoding JWT", e);
            throw new Exception("Error decoding JWT", e);
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}