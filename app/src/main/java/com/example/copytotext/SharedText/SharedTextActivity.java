package com.example.copytotext.SharedText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;


import com.example.copytotext.Copy.YourAccessibilityService;
import com.example.copytotext.R;
import com.example.copytotext.databinding.ActivityCopyTextShowListBinding;
import com.example.copytotext.databinding.ActivitySharedTextBinding;

import java.net.URLEncoder;

public class SharedTextActivity extends AppCompatActivity {
ActivitySharedTextBinding binding;
    private static final int PERMISSION_REQUEST_SEND_SMS = 1001; // Define your own request code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySharedTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
        }

//      String groupInviteLink = "GzxC7Z6ST7MB28ji6DKuUb"; // Replace with your group invite link
//    String message = "Hello, this is a message for the group!"; // Your message content
//
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("https://wa.me/" + groupInviteLink + "/?text=" + "muzamil" + "#" + message));
//        intent.setPackage("com.whatsapp");
//
//        try {
//            Log.d("kk", "Starting WhatsApp");
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            Log.e("hhh", "WhatsApp not installed", e);
//        }

        //isAccessibilityServiceEnabled code
                if (!isAccessibilityServiceEnabled()) {
                    // Open Accessibility settings screen
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    // Accessible, perform the necessary action
                    // ...
//                    if (clipboardListener == null) {
//                        clipboardListener = new ClipboardListener(context.getApplicationContext());
//                        clipboardListener.startListening();
//                    } else {
//                        clipboardListener.stopListening();
//                        clipboardListener = null;
//                    }
                }


    }
    private boolean isAccessibilityServiceEnabled() {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + YourAccessibilityService.class.getCanonicalName();

        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
            );
        } catch (Settings.SettingNotFoundException e) {
            // Handle the exception if necessary
        }

        TextUtils.SimpleStringSplitter stringSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            );

            if (settingValue != null) {
                stringSplitter.setString(settingValue);

                while (stringSplitter.hasNext()) {
                    String accessibilityService = stringSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}