package com.example.dictionary.ui.views.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.MainActivity;
import com.example.dictionary.R;
import com.example.dictionary.databinding.FragmentSearchBinding;
import com.example.dictionary.domain.models.WordModel;
import com.example.dictionary.domain.preferences.PreferenceHelper;

import java.util.ArrayList;
import java.util.Set;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class SearchFragment extends Fragment implements WordAdapter.OnClickListener {

    private SearchViewModel searchViewModel;
    private RecyclerView recyclerView;
    private WordAdapter wordAdapter;

    private Activity activity;

    private FragmentSearchBinding binding;
    private HistoryManager historyManager;


    public SearchFragment() {
        // Default constructor (no arguments)
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            activity = getActivity();
            PreferenceHelper.getInstance(activity, "search_prefs");
        }

        // Initialize the RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        wordAdapter = new WordAdapter(new ArrayList<>(), this);
        binding.recyclerView.setAdapter(wordAdapter);

        // Initialize the ViewModel
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        historyManager = new HistoryManager(getContext(), binding.historyListView);
        //setupSearchHistory();

        binding.inputSearch.setOnEditorActionListener((v, actionId, event) -> {
            // Check if the action is "Done" or "Enter"
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                String word = binding.inputSearch.getText().toString().trim(); // Get the word entered by the user
                binding.inputSearch.setText("");
                binding.inputSearch.clearFocus();
                if (!word.isEmpty()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.noResultsMessage.setVisibility(View.GONE);

                    PreferenceHelper.addToSearchHistory(word);

                    // Call the API with the user's input
                    searchViewModel.getWordDetails(word);
                    hideKeyboard(v); // Hide keyboard after submitting
                }
                return true;
            }
            return false;
        });

        searchViewModel.getWordList().observe(getViewLifecycleOwner(), wordList -> {
            if (wordList != null && !wordList.isEmpty()) {
                wordAdapter.updateSearchResult(wordList);
                binding.noResultsMessage.setVisibility(View.GONE);
            } else {
                wordAdapter.updateSearchResult(new ArrayList<>());
                if (!searchViewModel.initialRenderFlag) {
                    binding.noResultsMessage.setVisibility(View.VISIBLE);
                } else {
                    binding.noResultsMessage.setVisibility(View.GONE);
                }

            }
            binding.progressBar.setVisibility(View.GONE);
        });


    }

    private void setupSearchHistory() {
        Set<String> searchHistory = PreferenceHelper.getSearchHistory();
        historyManager.initializeHistory(searchHistory, word -> {
            binding.inputSearch.setText(word);
            searchViewModel.getWordDetails(word); // Perform search again for the selected word
        });
    }


    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClickListener(String action, WordModel word) {
        if (action.equals("SeeMore")) {
            ((MainActivity) activity).openWordDetailFragment(word);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (wordAdapter != null) {
            wordAdapter.releaseMediaPlayer(); // Release MediaPlayer to prevent leaks
        }
    }
}
