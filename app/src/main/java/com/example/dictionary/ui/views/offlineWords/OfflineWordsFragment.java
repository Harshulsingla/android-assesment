package com.example.dictionary.ui.views.offlineWords;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.MainActivity;
import com.example.dictionary.R;
import com.example.dictionary.databinding.FragmentOfflineWordsBinding;
import com.example.dictionary.domain.entity.WordDetailEntity;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OfflineWordsFragment extends Fragment implements WordDetailAdapter.OnClickListener, View.OnClickListener {

    private OfflineWordsViewModel offlineWordsViewModel;
    private RecyclerView recyclerView;
    private WordDetailAdapter wordDetailAdapter;

    private Activity activity;

    private WordDetailEntity wordDetailEntity;

    private FragmentOfflineWordsBinding binding;

    public OfflineWordsFragment() {
        // Default constructor
    }

    public static OfflineWordsFragment newInstance() {
        return new OfflineWordsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOfflineWordsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            activity = getActivity();
        }

        View parentView = activity.findViewById(R.id.searchIcon);
        parentView.setVisibility(View.VISIBLE);
        parentView.setOnClickListener(this);

        // Initialize the RecyclerView
        binding.recyclerViewOfflineWords.setLayoutManager(new LinearLayoutManager(getContext()));
        wordDetailAdapter = new WordDetailAdapter(new ArrayList<>(), this);
        //wordAdapter.setDeleteMode(true); // Set adapter to delete mode
        binding.recyclerViewOfflineWords.setAdapter(wordDetailAdapter);

        // Initialize the ViewModel
        offlineWordsViewModel = new ViewModelProvider(this).get(OfflineWordsViewModel.class);
        offlineWordsViewModel.loadOfflineWords();

        offlineWordsViewModel.getOfflineWordsList().observe(getViewLifecycleOwner(), offlineWordList -> {
            if (offlineWordList != null && !offlineWordList.isEmpty()) {
                binding.noResultsMessage.setVisibility(View.GONE);
                wordDetailAdapter.loadOfflineWords(offlineWordList);
            } else {
               binding.noResultsMessage.setVisibility(View.VISIBLE);
            }
        });

        offlineWordsViewModel.getDeletedWordResult().observe(getViewLifecycleOwner(), deletedWordResult -> {
            if (deletedWordResult != null && deletedWordResult.first) {
                wordDetailAdapter.deleteOfflineWord(deletedWordResult.second);
                Toast.makeText(activity, "Word Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Word Can't be deleted due to some internal error", Toast.LENGTH_SHORT).show();
            }
        });

        //binding.searchIcon.setOnClickListener(this);

    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClickListener(String action, WordDetailEntity wordDetailEntity) {

        if (action.equals("SeeMore")) {
            ((MainActivity) activity).openWordDetailFragment(wordDetailEntity);
        }

        if (action.equals("DeleteWord")) {

            // Show a confirmation dialog
            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this word?")
                    .setPositiveButton("Yes", (dialogInterface, which) -> {
                        // Perform the delete action here
                       offlineWordsViewModel.deleteWord(wordDetailEntity);
                    })
                    .setNegativeButton("No", (dialogInterface, which) -> dialogInterface.dismiss())
                    .create();  // Create the dialog

            dialog.show();  // Show the dialog

            // Change the color of the buttons
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            // Set the colors for the buttons
            positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.green));
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.green));
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.searchIcon){
            ((MainActivity)activity).openSearchFragment();
        }
    }
}
