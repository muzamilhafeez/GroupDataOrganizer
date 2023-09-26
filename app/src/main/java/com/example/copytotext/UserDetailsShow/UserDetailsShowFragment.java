package com.example.copytotext.UserDetailsShow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.copytotext.Adapter.UserDetailsAdapter;
import com.example.copytotext.Api.Api;
import com.example.copytotext.Api.RetrofitClientResponseString;
import com.example.copytotext.R;
import com.example.copytotext.databinding.FragmentUserDetailsShowBinding;
import com.example.copytotext.model.UserDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.appcompat.app.ActionBar; // if using AppCompat


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserDetailsShowFragment extends Fragment {
FragmentUserDetailsShowBinding binding;
    UserDetailsAdapter adapter;
    ArrayList<UserDetail> dataList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentUserDetailsShowBinding.inflate(getLayoutInflater());
        //start
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        //recyclerview search code
        binding.editTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
//
                if(dataList!=null) {
                    ArrayList<UserDetail> filteredList = new ArrayList<>();
                    for (UserDetail item : dataList) {
                        if (item.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
//                recyclerview function calling
                    adapter.filterList(filteredList);
                }
            }
        });

        RetrofitClientResponseString client = RetrofitClientResponseString.getInstance();
        Api api = client.getMyApi();
        api.getAllUserDeatils("get").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject jsonObject= new JSONObject(response.body().toString());
                        JSONArray jsonArray= jsonObject.getJSONArray("items");
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<UserDetail>>(){}.getType();
                         dataList   = gson.fromJson(jsonArray.toString(), type);
//                        ------------------start remainingdays----------
//                        Calendar calendar = Calendar.getInstance();
//                        int year = calendar.get(Calendar.YEAR);
//                        int month = calendar.get(Calendar.MONTH);
//                        int day = calendar.get(Calendar.DAY_OF_MONTH);
//                        String  selectedDateStart = year + "-" + (month + 1) + "-" + day;
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        int index=0;
//                        for (UserDetail user: dataList) {
//                            try {
//                                Date startDate = dateFormat.parse(selectedDateStart);
//                                Date endDate = dateFormat.parse(user.getEnddate());
//                                long timeDifferenceMillis = endDate.getTime() - startDate.getTime();
//                                int  daysDifference = (int) (timeDifferenceMillis / (1000 * 60 * 60 * 24));
//                                    dataList.get(index).setRemaingDays(String.valueOf(daysDifference));
//                                    index++;
//                            }catch (Exception e){
//
//                            }
//
//                        }
//
//                        // Sort dataList based on remaining days in ascending order
//                        Collections.sort(dataList, new Comparator<UserDetail>() {
//                            @Override
//                            public int compare(UserDetail user1, UserDetail user2) {
//                                int remainingDays1 = Integer.parseInt(user1.getRemaingDays());
//                                int remainingDays2 = Integer.parseInt(user2.getRemaingDays());
//                                return Integer.compare(remainingDays1, remainingDays2);
//                            }
//                        });
//                ------------------end remainingdays----------
//                        Toast.makeText(getContext(), ""+dataList.get(0).getStartdate(), Toast.LENGTH_SHORT).show();
                         adapter = new
                            UserDetailsAdapter(dataList, getActivity());
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                    binding.RecycerviewUserDetails.setLayoutManager(manager);
                    binding.RecycerviewUserDetails.setHasFixedSize(true);
                    binding.RecycerviewUserDetails.
                            setAdapter(adapter);
                        binding.loadingProgressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if (actionBar != null) {
                        actionBar.setTitle("Group data: "+dataList.size()+" members.");
                    }
//                    Toast.makeText(getContext(), ""+response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
                binding.loadingProgressBar.setVisibility(View.GONE);
            }
        });



        //end
        return binding.getRoot();
    }
}