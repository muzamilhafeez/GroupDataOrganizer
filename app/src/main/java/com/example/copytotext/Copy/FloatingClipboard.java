package com.example.copytotext.Copy;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.copytotext.MainActivity;


import android.Manifest;
import android.view.WindowManager;

import com.example.copytotext.R;
import com.example.copytotext.databinding.BubbleShowIconLayoutBinding;

import java.util.ArrayList;
import java.util.Set;

import retrofit2.http.Tag;

public class FloatingClipboard {
    private Context context;
    private WindowManager windowManager;
//    private View floatingView;
    private ClipboardListener clipboardListener;
    ArrayList<String> list;
    BubbleShowIconLayoutBinding binding;
    int CurrentIndex=0;

    public FloatingClipboard(Context context, ArrayList<String> list) {
        this.context = context;
        this.list=list;
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        this.floatingView = LayoutInflater.from(context).inflate(R.layout.bubble_show_icon_layout, null);
         binding = BubbleShowIconLayoutBinding.inflate(LayoutInflater.from(context));
    }

    public void show() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//            intent.setData(Uri.parse("package:" + context.getPackageName()));
//            context.startActivity(intent);
//        }

        //isAccessibilityServiceEnabled code
//                if (!isAccessibilityServiceEnabled()) {
//                    // Open Accessibility settings screen
//                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                } else {
//                    // Accessible, perform the necessary action
//                    // ...
//                    if (clipboardListener == null) {
//                        clipboardListener = new ClipboardListener(context.getApplicationContext());
//                        clipboardListener.startListening();
//                    } else {
//                        clipboardListener.stopListening();
//                        clipboardListener = null;
//                    }
//                }

//         Set the text on the floating view
//        TextView textView = (TextView) floatingView.findViewById(R.id.edit_tx_show);
        binding.editTxShow.setText(list.get(CurrentIndex));
        binding.txtViewCurrentIndex.setText(binding.txtViewCurrentIndex.getText()+": "+CurrentIndex);


// Set the button click listener
        binding.imageViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // exit
                hide();
            }
        });


        // Set the button click listener
        binding.imageViewUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentIndex<list.size()-1){
                    CurrentIndex++;
                    binding.txtViewCurrentIndex.setText("Current Index of List: "+CurrentIndex);
                    binding.editTxShow.setText(list.get(CurrentIndex));
                }

            }
        });
        // Set the button click listener
        binding.imageViewUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentIndex>0){
                    CurrentIndex--;
                    binding.txtViewCurrentIndex.setText("Current Index of List: "+CurrentIndex);
                    binding.editTxShow.setText(list.get(CurrentIndex));
                }

            }
        });
        // Set the button click listener
        binding.imageViewCopyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                copyToClipboard(binding.editTxShow.getText().toString());
            }
        });
        // Set the floating view's properties
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;

        // Add the floating view to the window manager
        windowManager.addView(binding.getRoot(), layoutParams);
    }

    // Function to copy text to the clipboard
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context,"Text copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Clipboard not available", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isAccessibilityServiceEnabled() {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + YourAccessibilityService.class.getCanonicalName();

        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
            );
        } catch (Settings.SettingNotFoundException e) {
            // Handle the exception if necessary
        }

        TextUtils.SimpleStringSplitter stringSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    context.getContentResolver(),
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

    public void hide() {
        if (binding != null) {
            windowManager.removeView(binding.getRoot());
        }
    }

}
