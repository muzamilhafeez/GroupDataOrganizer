package com.example.copytotext.Copy;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

public class ClipboardListener implements ClipboardManager.OnPrimaryClipChangedListener {
    private ClipboardManager clipboardManager;
    Context context;

    public ClipboardListener(Context context) {
        this.context= context;
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void startListening() {
        clipboardManager.addPrimaryClipChangedListener(this);
    }

    public void stopListening() {
        clipboardManager.removePrimaryClipChangedListener(this);
    }

    @Override
    public void onPrimaryClipChanged() {
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            CharSequence selectedText = clipData.getItemAt(0).getText();
            if (selectedText != null) {
                // Copy the selected text to your desired destination
                // For example, you can copy it to the clipboard again
                clipboardManager.setPrimaryClip(ClipData.newPlainText("Copied Text", selectedText));

                // Show a toast or perform any other desired action
                Toast.makeText(context, "Text copied: " + selectedText, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

