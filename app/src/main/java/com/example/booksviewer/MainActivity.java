package com.example.booksviewer;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.booksviewer.data.repository.Repository;
import com.example.booksviewer.databinding.ActivityMainBinding;
import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.ui.views.bookDetails.BookDetailsFragment;
import com.example.booksviewer.ui.views.favourites.FavouriteFragment;
import com.example.booksviewer.ui.views.home.HomeFragment;
import androidx.appcompat.app.AppCompatDelegate;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private ActivityMainBinding binding; // View Binding instance

    @Inject
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        // Use Handler.postDelayed() to avoid conflicts with fragment transactions
        new Handler().postDelayed(() -> {
            String savedTheme = repository.getThemeMode();
            updateTheme(savedTheme);
        }, 100);

        // Set click listeners
        binding.darkModeIcon.setOnClickListener(this);
        binding.lightModeIcon.setOnClickListener(this);
        binding.favoritesIcon.setOnClickListener(this);

        if (savedInstanceState == null) {
            openHomeFragment();
        }
    }

    private void updateTheme(String themeMode) {
        Log.d("ThemeChange", "Setting theme mode to: " + themeMode);
        if ("dark".equals(themeMode)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            binding.darkModeIcon.setVisibility(View.GONE);
            binding.lightModeIcon.setVisibility(View.VISIBLE);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            binding.darkModeIcon.setVisibility(View.VISIBLE);
            binding.lightModeIcon.setVisibility(View.GONE);
        }
    }

    public void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Ensure fragment is replaced only if it's not already in the fragment manager
        if (fragmentManager.findFragmentByTag(tag) == null) {
            transaction.replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    public void openHomeFragment() {
        replaceFragment(new HomeFragment(), "HomeFragment");
    }

    public void openFavouritesFragment() {
        replaceFragment(new FavouriteFragment(), "FavouriteFragment");
    }

    public void openBookDetailsFragment(BookModel book) {
        BookDetailsFragment fragment = BookDetailsFragment.newInstance(book);
        replaceFragment(fragment, "BookDetailsFragment");
    }

    public FragmentManager getGlobalFragmentManager() {
        return fragmentManager;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == binding.darkModeIcon.getId()) {
            // Handle dark mode button click
            runOnUiThread(() -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                binding.darkModeIcon.setVisibility(View.GONE);
                binding.lightModeIcon.setVisibility(View.VISIBLE);
                repository.saveThemeMode("dark");
            });
        } else if (view.getId() == binding.lightModeIcon.getId()) {
            // Handle light mode button click
            runOnUiThread(() -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                binding.lightModeIcon.setVisibility(View.GONE);
                binding.darkModeIcon.setVisibility(View.VISIBLE);
                repository.saveThemeMode("light");
            });
        } else if (view.getId() == binding.favoritesIcon.getId()) {
            // Open the favourites fragment
            openFavouritesFragment();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof HomeFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
