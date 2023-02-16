package com.antonymilian.socialmediafya.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.fragments.ChatsFragment;
import com.antonymilian.socialmediafya.fragments.FiltersFragment;
import com.antonymilian.socialmediafya.fragments.HomeFragment;
import com.antonymilian.socialmediafya.fragments.ProfileFragment;
import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.TokenProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    TokenProvider mTokenProvider;
    AuthProvider mAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        mTokenProvider = new TokenProvider();
        mAuthProvider = new AuthProvider();
        openFragment(new HomeFragment());
        createToken();
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    if (item.getItemId() == R.id.itemHome) {
                        //FREAGMENT HOME
                        openFragment(new HomeFragment());
                    }else if (item.getItemId() == R.id.itemChats){
                        //FREAGMENT Chats
                        openFragment(new ChatsFragment());
                    }else if (item.getItemId() == R.id.itemFiltros){
                        //FREAGMENT Filtros
                        openFragment(new FiltersFragment());
                    }else if (item.getItemId() == R.id.itemProfile){
                        //FREAGMENT Profile
                        openFragment(new ProfileFragment());
                    }
                    return true;
                }
            };

    private void createToken( ){
        mTokenProvider.create(mAuthProvider.getUid());
    }
}