package com.example.copytotext.Copy;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class YourAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
            CharSequence selectedText = event.getText().toString();
            if (!TextUtils.isEmpty(selectedText)) {
                // Copy the selected text to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Copied Text", selectedText);
                clipboardManager.setPrimaryClip(clipData);

                // Show a toast or perform any other desired action
                Toast.makeText(getApplicationContext(), "Text copied: " + selectedText, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onInterrupt() {

    }
    // Implement necessary methods and event handlers
}

