package com.example.copytotext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.copytotext.SharedText.SharedTextActivity;
import com.example.copytotext.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
    private TextView textView;
    private Button copyButton;
    String pattern2 = "\\[\\d{2}/\\d{2}, \\d{1,2}:\\d{2} [ap]m\\] [^:]+: ";
    String pattern = "\\s*\\[\\d{2}/\\d{2}, \\d{1,2}:\\d{2} [ap]m\\] \\w*\\s*\\w*\\s*\\w*:";
    String patternNumber = "\\s*\\[\\d{2}/\\d{2}, \\d{1,2}:\\d{2} [ap]m\\] \\+\\d*\\s*\\d*\\s*\\d*[:]*";
   static ArrayList<String> textList = new ArrayList<>();
    ArrayList<String> patternServiceNameList = new ArrayList<>();

    // Get the editor to modify SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        patternServiceNameList.add("چودری میسج سروس");
        patternServiceNameList.add("چوہدری میسج سروس");
        patternServiceNameList.add("03481412450");
        patternServiceNameList.add("😜👈چوہدری میسج سروس 03481412450🤗🤔🤓");
        patternServiceNameList.add("چوہدری میسج سروس");
        // Retrieve a string value (default value is optional)
        // Obtain reference to SharedPreferences
        SharedPreferences sharedPreferences  = getApplicationContext().
                getSharedPreferences("store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        String value = sharedPreferences.getString("key", null);
        if(value!=null){
            patternServiceNameList.clear();
            patternServiceNameList.addAll(new Gson().fromJson(value,new TypeToken<ArrayList<String>>(){}.getType()));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, patternServiceNameList);
            binding.spinnerRemoveText.setAdapter(adapter);
        }else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, patternServiceNameList);
            binding.spinnerRemoveText.setAdapter(adapter);
        }

        binding.btnSharedAutoMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), SharedTextActivity.class);
                startActivity(i);

            }
        });
        binding.imageAddRemoveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();

            }
        });
        binding.imageDeleteRemoveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You won't be able to recover this Text!")
                        .setConfirmText("Delete!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                patternServiceNameList.remove(binding.spinnerRemoveText.getSelectedItem().toString());
                                editor.putString("key", new Gson().toJson(patternServiceNameList));
                                editor.commit();
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                                        android.R.layout.simple_list_item_1, patternServiceNameList);
                                binding.spinnerRemoveText.setAdapter(adapter);
                                sDialog.dismissWithAnimation();

                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();


            }
        });

        // Show the bubble
        binding.btnCopyTextShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveTextAndChangeInList();
                Intent i = new Intent(getApplicationContext(), CopyTextShowListActivity.class);
                startActivity(i);
            }
        });

        // Set OnClickListener for the Copy button
        binding.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                String textToCopy = binding.editTxtPast.getText().toString();
                copyToClipboard(textToCopy);



                // Check if the SYSTEM_ALERT_WINDOW permission is granted
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this
//                        )) {
//                    // Permission not granted, open the permission request screen
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                    startActivity(intent);
//                } else {
//                    // Permission already granted, proceed with showing the floating view
//                    FloatingClipboard floatingClipboard= new FloatingClipboard(MainActivity.this);
//                    floatingClipboard.show();
//                }


            }
        });


        binding.btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.editTxtCopy.setText(getFromClipboard());

                binding.editTxtPast.setText(RemoveTextAndChangeInList());
            }
        });
    }
    private  String RemoveTextAndChangeInList(){
        String clipboardText = getFromClipboard();
        try {
            textList.clear();
             // Assume you have a method getFromClipboard() to retrieve the text from the clipboard
            // Check if the pattern matches the clipboard text only if brackets are present
            if(clipboardText.isEmpty()){
                Toast.makeText(MainActivity.this, "Please Copy text", Toast.LENGTH_SHORT).show();
            }else {
                Pattern regexPattern = Pattern.compile(pattern2);
                Matcher matcher = regexPattern.matcher(clipboardText);
                boolean isPatternMatched = matcher.find();
                for (String patternName : patternServiceNameList) {
                    if (Pattern.compile(Pattern.quote(patternName)).matcher(clipboardText).find()) {
                        clipboardText = clipboardText.replaceAll(Pattern.quote(patternName), "");
                        // Trim the text and perform further operations
                    }
                }

                if (isPatternMatched) {
                    clipboardText = RemoveText(clipboardText, pattern2);
                    System.out.println("Pattern matches the clipboard text with brackets");
                    // Trim the text and perform further operations
                }
                if (Pattern.compile(patternNumber).matcher(clipboardText).find()) {
                    clipboardText = RemoveText(clipboardText, patternNumber);
                    System.out.println("Pattern matches the clipboard text with brackets");
                    // Trim the text and perform further operations
                }
                if (Pattern.compile(pattern).matcher(clipboardText).find()) {
                    clipboardText = RemoveText(clipboardText, pattern);
                    System.out.println("Pattern matches the clipboard text with brackets");
                    // Trim the text and perform further operations

                }else{
                    if(textList.size()==0){
                        textList.add(clipboardText);
                    }

                }
            }
        }catch (Exception e){
            Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
        }
        return clipboardText;
    }
    // Function to copy text to the clipboard
    private String RemoveText(String text , String pattern) {

        String[] lines = text.split(pattern);

        for (String line : lines) {
            if(!line.isEmpty()){
                textList.add(line);
            }

        }
        String trimmedText = text.replaceAll(pattern, ""+binding.editTxtCopy.getText()+"\n");
        return trimmedText;
    }
    // Function to copy text to the clipboard
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            showToast("Text copied to clipboard");
        } else {
            showToast("Clipboard not available");
        }
    }

    private String getFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (clipboard != null && clipboard.hasPrimaryClip()) {
            ClipData clipData = clipboard.getPrimaryClip();
            ClipData.Item item = clipData.getItemAt(0);

            return item.getText().toString();
        }
        return null;
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void showAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(View.generateViewId());
        linearLayout.setBackgroundResource(R.drawable.recyclerview_background_cell);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                dpToPx(80), dpToPx(80));
        imageLayoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(imageLayoutParams);
        imageLayoutParams.setMargins(0, dpToPx(8), 0, dpToPx(8));
        imageView.setImageResource(R.drawable.edit_show_icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER);


        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText("Enter text to Remove");
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

        EditText editText = new EditText(this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
//        editText.setText("Everything is working");
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        editText.setTypeface(null, Typeface.BOLD);
        editText.setHint("Please Edit text");
        editText.setPadding(0, 0, 0, dpToPx(60));


        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM,linearLayout.getId());

        Button button = new Button(this);
        button.setLayoutParams(buttonLayoutParams);
        button.setText("Save");
        button.setTextColor(getResources().getColor(R.color.black));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        button.setBackgroundResource(R.drawable.button_gradient);
//        button.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));

        linearLayout.removeAllViews();
        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        relativeLayout.removeAllViews();
        linearLayout.addView(editText);

        relativeLayout.addView(linearLayout);
        relativeLayout.addView(button);


        dialogBuilder.setView(relativeLayout);

        AlertDialog alertDialog = dialogBuilder.create();
        //create button reference
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click event
                patternServiceNameList.add(editText.getText().toString());
                // Obtain reference to SharedPreferences
                SharedPreferences sharedPreferences  = getApplicationContext().
                        getSharedPreferences("store", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                // Save a string value

                editor.putString("key", new Gson().toJson(patternServiceNameList));
                // Apply the changes
                editor.commit();
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}
