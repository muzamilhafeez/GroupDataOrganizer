package com.example.copytotext.Adapter;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.copytotext.Api.Api;
import com.example.copytotext.Api.GobalAccessData;
import com.example.copytotext.Api.RetrofitClientResponseString;
import com.example.copytotext.MainActivity;
import com.example.copytotext.R;
import com.example.copytotext.UserDetailsShow.UserDetailsShowFragment;
import com.example.copytotext.databinding.RecyclerviewCopyTextShowListCellBinding;
import com.example.copytotext.databinding.RecyclerviewUserDetailsCellBinding;
import com.example.copytotext.model.UserDetail;
import com.example.copytotext.user.UpdateUserFragment;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.UserDetailsViewHolder>{
    private ArrayList<UserDetail> List;
    private Context context;
    String selectedDateStart;

    public UserDetailsAdapter(ArrayList<UserDetail> List, Context context) {
        this.List = List;
        this.context = context;


    }
    @NonNull
    @Override
    public UserDetailsAdapter.UserDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewUserDetailsCellBinding b  =RecyclerviewUserDetailsCellBinding.
                inflate(LayoutInflater
                                .from(parent.getContext()),
                        parent, false);

        UserDetailsViewHolder vh = new UserDetailsViewHolder(b);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailsAdapter.UserDetailsViewHolder holder, int position) {
        UserDetail user = List.get(position);
        // Use the currentindex variable to perform operations or access data associated with the ViewHolder later in your code
//
//        holder.binding.txtName.setText(""+position);
        holder.binding.txtName.setText("Name\n"+user.getName());
        holder.binding.txtTotalMonth.setText(""+user.getDuration());
        holder.binding.txtUserPhoneNo.setText("Phone No\n"+user.getPhone()+"");
        // Assuming you have a User object with getStartDate() and getEndDate() methods
        // Define the date format pattern to match the input date string

        holder.binding.txtReamingDays.setText("Remaining Days\n"+user.getRemaingDays());

//        Toast.makeText(context, ""+user.getStartdate()+":"+user.getEnddate(), Toast.LENGTH_SHORT).show();
////// Calculate the time difference in milliseconds



// Convert milliseconds to days


        holder.binding.imageViewWhatsappShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(!user.getPhone().isEmpty()){
                        String mobile = user.getPhone();
                        String msg = "محترم آپ کا ایک ماہ پورا ہو چکا ہے برائے کرم اپنی  فیس ادا کر دیجیے 200روپیہ \n" +
                                "03410071708\n" +
                                "Muhammad Muzamil Hafeez \n" +
                                "ایزی پیسہ";
//                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobile + "&text=" + msg)));
                        Uri uri = Uri.parse("smsto:" + mobile);
                        Intent whatsappIntent = new Intent(Intent.ACTION_SENDTO, uri);
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, msg);
                        context. startActivity(Intent.createChooser(whatsappIntent, "Share with"));
                        // Check if WhatsApp is installed
//                        if (whatsappIntent.resolveActivity(packageManager) != null) {
//                            // You can also try using the "smsto" URI to send a message directly to a contact
//
//                        }else {
//                            Toast.makeText(context, "not", Toast.LENGTH_SHORT).show();
//                        }
                    }

        }catch (Exception e){
            //whatsapp app not install
                    Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }
            }
        });
        holder.binding.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+user.getStartdate().split("T")[0], Toast.LENGTH_SHORT).show();
                GobalAccessData.userDetail= user;

                FragmentTransaction ft =
                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                UpdateUserFragment m= new UpdateUserFragment();
                ft.replace(R.id.frame_layout, m);
                ft.commit();
            }
        });
        holder.binding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You won't be able to recover this Text!")
                        .setConfirmText("Delete!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // Create and show a loading dialog before making the API call
                                final SweetAlertDialog loadingDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                                loadingDialog.setTitleText("Loading");
                                loadingDialog.setCancelable(false); // Prevent dismissing while loading
                                loadingDialog.show();
                                RetrofitClientResponseString client = RetrofitClientResponseString.getInstance();
                                Api api = client.getMyApi();
                                RetrofitClientResponseString.TimeSet=200;
                                api.UserDelete("delete",user.getId()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if(response.isSuccessful()){
                                            Toast.makeText(context, ""+response.body(), Toast.LENGTH_SHORT).show();
                                            List.remove(user);
                                            filterList(List);
                                            loadingDialog.dismiss(); //add this line here
                                            sDialog.dismissWithAnimation();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        loadingDialog.dismiss(); //add this line here
                                        Toast.makeText(context, ""+t, Toast.LENGTH_SHORT).show();
                                        sDialog.dismissWithAnimation();
                                    }
                                });
//end api
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
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class UserDetailsViewHolder extends RecyclerView.ViewHolder {
        RecyclerviewUserDetailsCellBinding binding;//<----
        public UserDetailsViewHolder(
                @NonNull RecyclerviewUserDetailsCellBinding itemView) {
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
    public void filterList(ArrayList<UserDetail> filteredList) {
        List = filteredList;
        notifyDataSetChanged();
    }
}
