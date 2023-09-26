package com.example.copytotext.Adapter;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Instrumentation;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.copytotext.R;
import com.example.copytotext.databinding.RecyclerviewCopyTextShowListCellBinding;

import java.util.ArrayList;

public class CopyTextShowListAdapter extends RecyclerView.Adapter<CopyTextShowListAdapter.CopyTextShowListViewHolder>{
    private ArrayList<String> List;
    private Context context;

    public CopyTextShowListAdapter(ArrayList<String> List, Context context) {
        this.List = List;
        this.context = context;

    }
    @NonNull
    @Override
    public CopyTextShowListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewCopyTextShowListCellBinding b  =RecyclerviewCopyTextShowListCellBinding.
                inflate(LayoutInflater
                                .from(parent.getContext()),
                        parent, false);

        CopyTextShowListViewHolder vh = new CopyTextShowListViewHolder(b);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CopyTextShowListViewHolder holder, int position) {
        int currentindex = holder.getAdapterPosition();
        // Use the currentindex variable to perform operations or access data associated with the ViewHolder later in your code
//
        holder.binding.txtViewShow.setText(List.get(currentindex));
        holder.binding.txtViewTitle.setText(currentindex+"");
        holder.binding.imageViewCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipboard(List.get(currentindex));
            }
        });
        holder.binding.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(currentindex);
            }
        });
        holder.binding.imageViewShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendWhatsAppMessageTo(List.get(currentindex));
            }
        });
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class CopyTextShowListViewHolder extends RecyclerView.ViewHolder {
        RecyclerviewCopyTextShowListCellBinding binding;//<----
        public CopyTextShowListViewHolder(
                @NonNull RecyclerviewCopyTextShowListCellBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;//<----
        }
    }

    public void sendWhatsAppMessageTo(String whatsappid) {

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, whatsappid);
        try {
            context.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }


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
    private void showAlertDialog(int Edit_to_Index) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        relativeLayout.setId(View.generateViewId());

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(View.generateViewId());
        linearLayout.setBackgroundResource(R.drawable.recyclerview_background_cell);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            linearLayout.setLeftTopRightBottom(0, 90, 0, dpToPx(8));
        }

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                dpToPx(80), dpToPx(80));
        imageLayoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(imageLayoutParams);
        imageLayoutParams.setMargins(0, dpToPx(8), 0, dpToPx(8));
        imageView.setImageResource(R.drawable.edit_show_icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER);


        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText("Edit text");
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

        EditText editText = new EditText(context);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        editText.setText(List.get(Edit_to_Index));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        editText.setTypeface(null, Typeface.BOLD);
        editText.setHint("Please Edit text");
        editText.setPadding(0, 0, 0, dpToPx(50));

        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM,linearLayout.getId());


        Button button = new Button(context);
        button.setLayoutParams(buttonLayoutParams);
        button.setText("Save");
        button.setTextColor(context.getResources().getColor(R.color.black));
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
                //update text in list
                List.set(Edit_to_Index, editText.getText().toString());
                //update list one item again calling
                filterList(List);
                // Handle button click event
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    public void filterList(ArrayList<String> filteredList) {
        List = filteredList;
        notifyDataSetChanged();
    }
}