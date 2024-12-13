package com.example.dictionary;



import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dictionary.databinding.ActivityMainBinding;
import com.example.dictionary.domain.entity.WordDetailEntity;
import com.example.dictionary.domain.models.WordModel;
import com.example.dictionary.ui.views.offlineWords.OfflineWordsFragment;
import com.example.dictionary.ui.views.search.SearchFragment;
import com.example.dictionary.ui.views.word_detail.WordDetailFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    SearchFragment searchFragment;

    WordDetailFragment wordDetailFragment;

    OfflineWordsFragment offlineWordsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState==null){
            openOfflineWordsFragment();
        }
    }
    public void openOfflineWordsFragment(){
        offlineWordsFragment = OfflineWordsFragment.newInstance();
        replaceFragment(offlineWordsFragment, "OfflineWordsFragment");
    }

    public void openSearchFragment() {
        searchFragment = SearchFragment.newInstance();
        replaceFragment(searchFragment, "SearchFragment");
    }

    public void openWordDetailFragment(WordDetailEntity wordDetailEntity){
        wordDetailFragment = WordDetailFragment.newInstance(wordDetailEntity);
        replaceFragment(wordDetailFragment, "WordDetailFragment");
    }

    private void replaceFragment(Fragment fragment, String tag){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment, tag).addToBackStack(tag).commit();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (currentFragment instanceof OfflineWordsFragment) {
            finishAffinity();
        }else {
            super.onBackPressed();
        }
    }

}
