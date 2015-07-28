package com.occam.cordova.share;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import android.net.Uri;
import java.util.ArrayList;

public class ShareHandlerActivity extends Activity {
    private static String TAG = "ShareHandlerActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

        // Get intent, action and MIME type
        Intent intent = getIntent();
        Bundle message = new Bundle();// = intent.getExtras();
        
        String action = intent.getAction();
        String type = intent.getType();
        
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                message.putString("type", handleSendImage(intent)); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
        
        
        
        SharePlugin.saveMessage(message);

        finish(); // 현재 Activity 끝내기.

        forceMainActivityReload();
    }
    
    private String handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            //imageUri.imageFilePath
            // Update UI to reflect image being shared
            return imageUri.getPath().toString();
        }
        return null;
    }
    
    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
    
    /*
     * Forces the main activity to re-launch if it's unloaded.
     */
    private void forceMainActivityReload() {
        PackageManager pm = getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
        startActivity(launchIntent);
    }
    
}