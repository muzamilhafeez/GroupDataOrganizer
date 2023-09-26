package com.example.copytotext.Drawer.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.copytotext.MainActivity;
import com.example.copytotext.NewMumber.AddNewMumberFragment;
import com.example.copytotext.R;
import com.example.copytotext.UserDetailsShow.UserDetailsShowFragment;
import com.example.copytotext.databinding.ActivityDrawerBinding;
import com.example.copytotext.user.UpdateUserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class DrawerActivity extends AppCompatActivity {
ActivityDrawerBinding binding;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction ft =
                getSupportFragmentManager().beginTransaction();
        UserDetailsShowFragment m= new UserDetailsShowFragment();
        ft.replace(R.id.frame_layout, m);
        ft.commit();

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        toggle= new ActionBarDrawerToggle(this,binding.drawerLayout,
                R.string.open_nav,R.string.close_nav);
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
// to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    //calling fragment
//                    FragmentTransaction ft =
//                            getSupportFragmentManager().beginTransaction();
//                    HomeFragment m= new HomeFragment();
//                    ft.replace(R.id.frame_layout, m);
//                    ft.commit();
                    //  Toast.makeText(drwer.this, "Setting", Toast.LENGTH_LONG).show();

//                    Intent i =new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(i);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }

                if (id == R.id.nav_New_user) {
                    //calling fragment
                    loadFragment(new AddNewMumberFragment());
                }
                if (id == R.id.nav_user_details) {
                    //calling fragment
                    loadFragment(new UserDetailsShowFragment());
                }
                //This is for closing the drawer after acting on it
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                // OnBackPressed();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void openTargetActivityDialog(View view) {

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // If the navigation drawer is open, close it
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // If the navigation drawer is closed, handle the back press
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

            if (fragment instanceof AddNewMumberFragment) {
                // If the current fragment is AddNewMemberFragment, switch to a different fragment
                // For example:
                loadFragment(new UserDetailsShowFragment());
                // loadFragment(new UserDetailsFragment());
            } else   if (fragment instanceof UpdateUserFragment) {
                // If the current fragment is AddNewMemberFragment, switch to a different fragment
                // For example:
                loadFragment(new UserDetailsShowFragment());

            } else{
                // If the current fragment is not AddNewMemberFragment, proceed with the default back behavior
                super.onBackPressed();
            }
        }
    }

    private void loadFragment(Fragment f) {
        FragmentTransaction ft =
                getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, f);
        ft.commit();
    }
}