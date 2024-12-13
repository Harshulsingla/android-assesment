package com.example.booksviewer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.booksviewer.data.repository.Repository;
import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.ui.views.bookDetails.BookDetailsFragment;
import com.example.booksviewer.ui.views.home.HomeFragment;
import androidx.appcompat.app.AppCompatDelegate;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private ImageView darkModeIcon;
    private ImageView lightModeIcon;

    @Inject
    Repository repository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();


        darkModeIcon = findViewById(R.id.dark_mode_icon);
        lightModeIcon = findViewById(R.id.light_mode_icon);

        // Check saved theme mode and set the app's theme
        String savedTheme = repository.getThemeMode();
        if ("dark".equals(savedTheme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            darkModeIcon.setVisibility(View.GONE);
            lightModeIcon.setVisibility(View.VISIBLE);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            darkModeIcon.setVisibility(View.VISIBLE);
            lightModeIcon.setVisibility(View.GONE);
        }

        // Toggle dark/light mode on click
        darkModeIcon.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            darkModeIcon.setVisibility(View.GONE);
            lightModeIcon.setVisibility(View.VISIBLE);
            repository.saveThemeMode("dark");
        });

        lightModeIcon.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            lightModeIcon.setVisibility(View.GONE);
            darkModeIcon.setVisibility(View.VISIBLE);
            repository.saveThemeMode("light");
        });

        if (savedInstanceState == null) {
            openHomeFragment();
        }
    }


    public void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(tag);
        transaction.commit();
    }

    public void openHomeFragment() {

        replaceFragment(new HomeFragment(), "HomeFragment");
    }

//    public void openFavouriteFragment() {
//        replaceFragment(new FavouriteFragment(), "FavouriteFragment");
//    }

    public void openBookDetailsFragment(BookModel book) {
        BookDetailsFragment fragment = BookDetailsFragment.newInstance(book);
        replaceFragment(fragment, "BookDetailsFragment");
    }

    public FragmentManager getGlobalFragmentManager() {
        return fragmentManager;
    }
}