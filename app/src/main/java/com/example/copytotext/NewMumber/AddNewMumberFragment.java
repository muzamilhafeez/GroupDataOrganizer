package com.example.copytotext.NewMumber;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.copytotext.Api.Api;
import com.example.copytotext.Api.FileUtil;
import com.example.copytotext.Api.RetrofitClient;
import com.example.copytotext.Api.RetrofitClientResponseString;
import com.example.copytotext.R;
import com.example.copytotext.databinding.CameraAndGalleyOpenCustomDesignBinding;
import com.example.copytotext.databinding.FragmentAddNewMumberBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddNewMumberFragment extends Fragment {
FragmentAddNewMumberBinding binding;
    int monthOffset=1;
    String selectedDateStart,selectedDateEnd;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private static final int REQUEST_IMAGE_SELECT = 2;
    byte[] imgeArr;
    Uri imgeUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentAddNewMumberBinding.inflate(getLayoutInflater());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDateStart = year + "-" + (month + 1) + "-" + day;

        binding.floatingbtnCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera code


                if (hasCameraPermission()) {
                    CameraAndGalleyOpenCustomDesignBinding Camera_and_gallery_Binding=CameraAndGalleyOpenCustomDesignBinding.inflate(getLayoutInflater());
                    // Create dialog and set content view to custom layout
                    Dialog customDialog = new Dialog(getActivity(),R.style.Model);
                    customDialog.setContentView(Camera_and_gallery_Binding.getRoot());
                    // Set additional properties for dialog
                    customDialog.setTitle("Custom Dialog");
                    customDialog.setCancelable(true);
                    customDialog.setCanceledOnTouchOutside(true);
                    customDialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);
                    // customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //btn camera
                    Camera_and_gallery_Binding.btnCameraCustom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //code

                            //open camera code
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);

                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                            customDialog.dismiss();

                        }
                    });
                    //btn gallery
                    Camera_and_gallery_Binding.btnGalleryCustom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //code
                            //open gallery code
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQUEST_IMAGE_SELECT);
                            customDialog.dismiss();
                        }
                    });
                    // Show dialog
                    customDialog.show();

                } else {
                    requestCameraPermission();
                }


//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
            }
        });
        binding.imageViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        binding.imageViewStartEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerNextMont(monthOffset);
            }
        });
        binding.txtUserDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update monthOffset whenever EditText value changes
               try{
                   if (!s.toString().isEmpty()) {
                       Calendar currentDate = Calendar.getInstance();
                       currentDate.add(Calendar.MONTH, Integer.parseInt(s.toString())); // Adding one month to the current date

                       int year = currentDate.get(Calendar.YEAR);
                       int month = currentDate.get(Calendar.MONTH);
                       int day = currentDate.get(Calendar.DAY_OF_MONTH);

                       selectedDateEnd = year + "-" + (month +1) + "-" + day;
//                       openDatePickerNextMont(monthOffset);
                   } else {
                       monthOffset = 1;
                   }
               }catch (Exception e){
                   Toast.makeText(getContext(), "pleace int input"+e, Toast.LENGTH_SHORT).show();
               }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this example
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create and show a loading dialog before making the API call
                final SweetAlertDialog loadingDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                loadingDialog.setTitleText("Loading");
                loadingDialog.setCancelable(false); // Prevent dismissing while loading
                loadingDialog.show();


                String name= binding.txtName.getText().toString();
                String city= binding.txtUserCity.getText().toString();
                String phone= binding.txtUserPhoneNo.getText().toString();
                String duration= binding.txtUserDuration.getText().toString()+" Month";
//                Toast.makeText(getContext(), ""+duration, Toast.LENGTH_SHORT).show();
                String start= selectedDateStart;
                String end= selectedDateEnd;
                if(end==null){
                    Calendar currentDate = Calendar.getInstance();
                    currentDate.add(Calendar.MONTH, 1); // Adding one month to the current date

                    int year = currentDate.get(Calendar.YEAR);
                    int month = currentDate.get(Calendar.MONTH);
                    int day = currentDate.get(Calendar.DAY_OF_MONTH);

                    end = year + "-" + (month +1) + "-" + day;
                }
                String fee= binding.txtUserPayFee.getText().toString();

               ;
                RetrofitClientResponseString client = RetrofitClientResponseString.getInstance();
                Api api = client.getMyApi();
                RetrofitClientResponseString.TimeSet=200;
                if(imgeArr!=null){
                    RequestBody requestBodyBinaryImage = RequestBody.create(MediaType.parse("application/octet-stream"), imgeArr);
                    api.NewUserAdd("createOrPic",name,city,start,end,duration,fee,phone,requestBodyBinaryImage).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if(response.isSuccessful()){
                                // Later in your code, when you want to dismiss the dialog:
                                loadingDialog.dismiss(); //add this line here
                                // 1. Success message
                                new SweetAlertDialog(getActivity())
                                        .setTitleText(""+response.body())
                                        .show();
//                                Toast.makeText(getContext(), ""+response.body(), Toast.LENGTH_SHORT).show();
                            }else {
                                // Later in your code, when you want to dismiss the dialog:
                                loadingDialog.dismissWithAnimation();
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), "api"+t, Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissWithAnimation();
                        }
                    });
                }else {
                    api.NewUserAdd("createOrPic",name,city,start,end,duration,fee,phone).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                loadingDialog.dismiss(); //add this line here
                                // 1. Success message
                                new SweetAlertDialog(getActivity())
                                        .setTitleText(""+response.body())
                                        .show();
//                                Toast.makeText(getContext(), ""+response.body(), Toast.LENGTH_SHORT).show();
                            }else {
                                loadingDialog.dismiss();
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            loadingDialog.dismiss();
                            Toast.makeText(getContext(), "api"+t, Toast.LENGTH_SHORT).show();
                        }
                    });
                }





            }
        });

        //end line
        return binding.getRoot();
    }

    private void openDatePickerNextMont(int nextMonth) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.MONTH, nextMonth); // Adding one month to the current date

        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // This method will be called when a date is selected in the dialog
                        // You can handle the selected date here
                        selectedDateEnd = year + "-" + (monthOfYear+ 1) + "-" + dayOfMonth;
                        Toast.makeText(getContext(), "Selected Date: " + selectedDateEnd, Toast.LENGTH_SHORT).show();
                    }
                },
                year, month, day
        );

        datePickerDialog.show();

    }
    private void openDatePicker() {
        // Get the current date

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Create a DatePickerDialog to display the calendar picker
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Handle the selected date
                        selectedDateStart = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        Toast.makeText(getContext(), "Selected Date: " + selectedDateStart, Toast.LENGTH_SHORT).show();
                    }
                },
                year, month, day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void openDatePickerPopulate() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Extract year, month, and day from the stored selected date if available

        String selectedDate = null;
        if (!selectedDate.isEmpty()) {
            String[] dateParts = selectedDate.split("-");
            year = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]) - 1;
            day = Integer.parseInt(dateParts[2]);
        }

        // Create a DatePickerDialog with the preselected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                    }
                    // ... Rest of the code ...
                },
                year, month, day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);//CAMERA_PERMISSION_REQUEST_CODE=100
    }
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 @Nullable Intent data) {
//camera user
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == Activity.RESULT_OK){
                Bundle bundleObj = data.getExtras();
                Bitmap bmpImage = (Bitmap) bundleObj.get("data");
                binding.imageViewUser.setImageBitmap(bmpImage);
                //save database convert byte
                BitmapDrawable bmpDrawble = (BitmapDrawable)
                        binding.imageViewUser.getDrawable();
                // Bitmap bmpImage = bmpDrawble.getBitmap();
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream();
                bmpImage.compress(Bitmap.CompressFormat.PNG,
                        100,outputStream);
                imgeArr = outputStream.toByteArray();
                //byte to bitmap
//                RetrofitClientResponseString client = RetrofitClientResponseString.getInstance();
//                Api api = client.getMyApi();
//                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), imgeArr);
//
//                api.ImageSend("saveImagebinary",requestBody).enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        Toast.makeText(getContext(), ""+response.code(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
//                    }
//                });


            }
        }//gallery use code
        else if(requestCode == REQUEST_IMAGE_SELECT){
            if(resultCode == Activity.RESULT_OK){
                imgeUri = data.getData();
                binding.imageViewUser.setImageURI(imgeUri);

                try {
                    Uri imgeUri=data.getData();
                    File file = FileUtil.from(getContext(), imgeUri);
                    // Open an input stream for the file
                    FileInputStream inputStream = new FileInputStream(file);

                    // Calculate the length of the file
                    int fileLength = (int) new File(file.getPath()).length();

                    // Create a byte array to store the file content
                    imgeArr = new byte[fileLength];

                    // Read the file content into the byte array
                    inputStream.read(imgeArr);

                    // Close the input stream
                    inputStream.close();
                    Toast.makeText(getContext(), ""+imgeArr.length, Toast.LENGTH_SHORT).show();
                    // Now you have the file content as a byte array (fileBytes)
                } catch (IOException e) {
                    // Handle any exceptions that might occur during file reading
                    e.printStackTrace();
                    Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}