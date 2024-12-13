package com.example.dictionary;



import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dictionary.domain.models.WordModel;
import com.example.dictionary.ui.views.search.SearchFragment;
import com.example.dictionary.ui.views.word_detail.WordDetailFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    SearchFragment searchFragment;

    WordDetailFragment wordDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState==null){
            openSearchFragment();
        }
    }

    public void openSearchFragment() {
        searchFragment = SearchFragment.newInstance();
        replaceFragment(searchFragment, "SearchFragment");
    }

    public void openWordDetailFragment(WordModel wordModel){
        wordDetailFragment = WordDetailFragment.newInstance(wordModel);
        replaceFragment(wordDetailFragment, "WordDetailFragment");
    }

    private void replaceFragment(Fragment fragment, String tag){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment, tag).addToBackStack(tag).commit();
    }

}
