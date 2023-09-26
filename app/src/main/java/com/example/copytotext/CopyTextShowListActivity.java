package com.example.copytotext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.copytotext.Adapter.CopyTextShowListAdapter;
import com.example.copytotext.Copy.FloatingClipboard;
import com.example.copytotext.databinding.ActivityCopyTextShowListBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CopyTextShowListActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 101;
    ActivityCopyTextShowListBinding binding;

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler();
    ArrayList listWithServiceNameAdd=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityCopyTextShowListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        showAlertDialog();

        for (String msg :MainActivity.textList) {
           msg= ""+binding.editTxtCopy.getText()+"\n"+msg;
            listWithServiceNameAdd.add(msg);
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        binding.btnHideServiceText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                binding.btnHideServiceText.setAnimation(animation);
                if(binding.btnHideServiceText.getText().toString().contains("Hide")){
                    binding.btnHideServiceText.setText("Show");
                    listWithServiceNameAdd.clear();
                    for (String msg :MainActivity.textList) {
                        msg= ""+binding.editTxtCopy.getText()+"\n"+msg;
                        listWithServiceNameAdd.add(msg);
                    }
                    CopyTextShowListAdapter adapter = new
                            CopyTextShowListAdapter(MainActivity.textList, CopyTextShowListActivity.this);
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    binding.RecycerviewTextShow.setLayoutManager(manager);
                    binding.RecycerviewTextShow.setHasFixedSize(true);
                    binding.RecycerviewTextShow.
                            setAdapter(adapter);
                }else{
                    binding.btnHideServiceText.setText("Hide");
                    CopyTextShowListAdapter adapter = new
                            CopyTextShowListAdapter(listWithServiceNameAdd, CopyTextShowListActivity.this);
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    binding.RecycerviewTextShow.setLayoutManager(manager);
                    binding.RecycerviewTextShow.setHasFixedSize(true);
                    binding.RecycerviewTextShow.
                            setAdapter(adapter);
                }

            }
        });
        binding.brnNewWindows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
                    // Permission not granted, request it from the user
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
                } else {
                    // Permission already granted, proceed with showing the floating view
                    FloatingClipboard floatingClipboard = new FloatingClipboard(CopyTextShowListActivity.this, listWithServiceNameAdd);
                    floatingClipboard.show();
                }

                // Check if the SYSTEM_ALERT_WINDOW permission is granted
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext()
//                        )) {
//
//                    // Permission not granted, open the permission request screen
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                    startActivity(intent);
//                } else {
//                    if (ContextCompat.checkSelfPermission(CopyTextShowListActivity.this, android.Manifest.permission.SYSTEM_ALERT_WINDOW)
//                            != PackageManager.PERMISSION_GRANTED) {
//
//                        // Request the permission
//                        ActivityCompat.requestPermissions(
//                                (Activity) CopyTextShowListActivity.this,
//                                new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
//                                100);
//                    }
//                    // Permission already granted, proceed with showing the floating view
//                    FloatingClipboard floatingClipboard= new FloatingClipboard(CopyTextShowListActivity.this,listWithServiceNameAdd );
//                    floatingClipboard.show();
//                }
            }
        });
        // Get the images to share.
        // Get the texts to share.
        //NOTE : please use with country code first 2digits without plus signed
//        try {
//            String mobile = "923410071708";
//            String msg = "Its Working";
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobile + "&text=" + msg)));
//        }catch (Exception e){
//            //whatsapp app not install
//        }


//        Uri uri = Uri.parse("https://wa.me/GzxC7Z6ST7MB28ji6DKuUb");
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);

//        // Get reference to the ClipboardManager
//        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//
//        // Set the clipboard content
//        ClipData clip = ClipData.newPlainText("label","iii");
//        clipboard.setPrimaryClip(clip);
//
//        SystemClock.sleep(3000);
//
//        executor.execute(() -> {
//
//
//            for (int i = 1; i <= 2; i++) {
//                // Perform paste action
//                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_CTRL_LEFT);
//                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_V);
//
//
//            }

//            // Run any UI-related code on the main thread using the Handler
//            handler.post(() -> {
//                // UI-related code here...
//            });
//        });

//        String groupId = "IL7uQ5sjRZQBoucvLocFh2"; // The group ID of the WhatsApp group
//
//// Create the intent with the WhatsApp group ID
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("content://com.android.contacts/data/" + groupId));
//
//// Verify if WhatsApp is installed on the device
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        } else {
//            // If WhatsApp is not installed, open the group invite link in a web browser
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.whatsapp.com/" + groupId)));
//        }







//        Uri uri = Uri.parse("smsto:" +"+923410071708");
//        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
//        i.setPackage("com.whatsapp");
//        startActivity(Intent.createChooser(i, ""));
//


//        try {
//            String encodedMessage = URLEncoder.encode(message, "UTF-8");
//            String url = "https://api.whatsapp.com/send?text=" + encodedMessage + "&group=" + groupId;







//        String phoneNumberWithCountryCode = "LeOBjOgzsy061Zg4zJjBDT";
//        String message = "Hallo";
//
//        startActivity(
//                new Intent(Intent.ACTION_VIEW,
//                        Uri.parse(
//                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, message)
//                        )
//                )
//        );
//
//// Add the LinearLayout to the parent layout (e.g., a ConstraintLayout or a parent LinearLayout)
//        parentLayout.addView(linearLayout);

        CopyTextShowListAdapter adapter = new
                CopyTextShowListAdapter(listWithServiceNameAdd, CopyTextShowListActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        binding.RecycerviewTextShow.setLayoutManager(manager);
        binding.RecycerviewTextShow.setHasFixedSize(true);
        binding.RecycerviewTextShow.
                setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(getApplicationContext())) {
                // Permission granted, proceed with showing the floating view
                FloatingClipboard floatingClipboard = new FloatingClipboard(CopyTextShowListActivity.this, listWithServiceNameAdd);
                floatingClipboard.show();
            } else {
                // Permission not granted, handle this case
                // You can show a message or take appropriate action here
            }
        }
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
        textView.setText("Edit text");
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
        editText.setPadding(0, 0, 0, dpToPx(20));


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