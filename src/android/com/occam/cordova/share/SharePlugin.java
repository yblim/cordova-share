package com.occam.cordova.share;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SharePlugin extends CordovaPlugin {
    public static final String TAG = "SharePlugin";

    public static final String GET_MESSAGE = "getMessage";
    public static final String UNREGISTER = "unregister";

    //private static CallbackContext context;
    //private static CallbackContext channel;
    private static Bundle cachedMessage = null;
    private static boolean foreground = false;
    private static boolean sendMetrics;

    public static final String SETTINGS = "settings";
    //private SharedPreferences preferences;

    /**
     * Gets the application context from cordova's main activity.
     * @return the application context
     */
    private Context getApplicationContext() {
        return this.cordova.getActivity().getApplicationContext();
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        //preferences = cordova.getActivity().getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    @Override
    public boolean execute(String action, JSONArray data, final CallbackContext callbackContext) {
        Log.v(TAG, "execute: action=" + action);
        foreground = true;

        if (GET_MESSAGE.equals(action)) {
            Log.v(TAG, "execute: data=" + data.toString());

            if ( cachedMessage != null ) {
                sendMessage(cachedMessage, callbackContext);
                cachedMessage = null;
            } else {
                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);
            }
            
            return true;
        } else if (UNREGISTER.equals(action)) {
            //context = null;
            callbackContext.success("unregister result msg");

            return true;
        } else {
            callbackContext.error("Invalid action : " + action);
        }

        return false;
    }

    public static void saveMessage(Bundle message) {
        if (message != null) {
            cachedMessage = message;
        }
    }
    
    public static void sendMessage(Bundle message, CallbackContext context) {
        if (message != null && context != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, convertBundleToJson(message));
            result.setKeepCallback(true);
            context.sendPluginResult(result);
        }
    }

    /**
     * Serializes a bundle to JSON.
     * @param message to be serialized
     */
    private static JSONObject convertBundleToJson(Bundle message) {
        try {
            JSONObject json;
            json = new JSONObject();
            
            JSONObject jsondata = new JSONObject();
            for (String key : message.keySet()) {
                Object value = message.get(key);
                
                json.put(key, value);
                /*
                // System data from Android
                if (key.equals("from") || key.equals("collapse_key")) {
                    json.put(key, value);
                } else if (key.equals("foreground")) {
                    json.put(key, message.getBoolean("foreground"));
                } else if (key.equals("coldstart")) {
                    json.put(key, message.getBoolean("coldstart"));
                } else {
                    // Maintain backwards compatibility
                    if (key.equals("message") || key.equals("msgcnt") || key.equals("sound") || key.equals("alert")) {
                        json.put(key, value);
                    }
                    
                    if (value instanceof String) {
                        // Try to figure out if the value is another JSON object
                        
                        String strValue = (String) value;
                        if (strValue.startsWith("{")) {
                            try {
                                JSONObject json2 = new JSONObject(strValue);
                                jsondata.put(key, json2);
                            } catch (Exception e) {
                                jsondata.put(key, value);
                            }
                            // Try to figure out if the value is another JSON array
                        } else if (strValue.startsWith("[")) {
                            try {
                                JSONArray json2 = new JSONArray(strValue);
                                jsondata.put(key, json2);
                            } catch (Exception e) {
                                jsondata.put(key, value);
                            }
                        } else {
                            jsondata.put(key, value);
                        }
                    }
                }*/
            } // while
            //json.put("payload", jsondata);
            
            Log.v(TAG, "extrasToJSON: " + json.toString());
            
            return json;
        } catch (JSONException e) {
            Log.e(TAG, "extrasToJSON: JSON exception");
        }
        return null;
    }
    
    /*public static boolean isActive() {
        return context != null;
    }*/

    public void onDestroy() {
        //context = null;
        super.onDestroy();
    }
}
