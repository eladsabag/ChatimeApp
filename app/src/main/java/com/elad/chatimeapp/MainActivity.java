package com.elad.chatimeapp;

import static com.elad.chatimeapp.utils.Util.openHtmlTextDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.elad.chatimeapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.my_nav_host_fragment);
        if (navHostFragment == null) return;
        NavController navController = navHostFragment.getNavController();

        navController.addOnDestinationChangedListener(onDestinationChangedListener);

        initSupportActionBarView();
    }

    private void initSupportActionBarView() {
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.primaryColor)));
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.terms_of_use) {
            openHtmlTextDialog(this, "terms_of_use.html");
        } else if (item.getItemId() == R.id.privacy_policy) {
            openHtmlTextDialog(this, "privacy_policy.html");
        }
        return super.onOptionsItemSelected(item);
    }

    private final NavController.OnDestinationChangedListener onDestinationChangedListener = (navController, navDestination, bundle) -> {
        if (getSupportActionBar() == null) return;
        if (navDestination.getId() == R.id.splash_dest) {
            getSupportActionBar().hide();
            return;
        }
        getSupportActionBar().show();
    };
}