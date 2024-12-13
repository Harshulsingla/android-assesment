package com.example.booksviewer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.ui.views.bookDetails.BookDetailsFragment;
import com.example.booksviewer.ui.views.favourites.FavouriteAdapter;
import com.example.booksviewer.ui.views.home.HomeFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        if(savedInstanceState == null){
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