package com.example.booksviewer.ui.views.favourites;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.booksviewer.MainActivity;
import com.example.booksviewer.databinding.FragmentFavouriteBinding;
import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.ui.views.home.BookAdapter;
import com.example.booksviewer.utils.OnBookClickListener;
import com.example.booksviewer.utils.OnSaveIconClickListener;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteFragment extends Fragment implements OnBookClickListener, OnSaveIconClickListener {

    private FavouriteViewModel favouriteViewModel;
    private BookAdapter bookAdapter;
    private FragmentFavouriteBinding binding;

    private Activity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        Context context = getContext();

        if (getActivity() != null) {
            activity = getActivity();
        }

        // Initialize the adapter
        bookAdapter = new BookAdapter(new ArrayList<>(), context, this, this);
        binding.recyclerView.setAdapter(bookAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Observe LiveData for books
        favouriteViewModel.getBooksLiveData().observe(getViewLifecycleOwner(), bookModels -> {
            // Update the RecyclerView adapter
            bookAdapter.HandleUpdate(bookModels);

            // Toggle visibility of empty_state_component and recyclerView
            if (bookModels == null || bookModels.isEmpty()) {
                binding.emptyStateComponent.getRoot().setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.emptyStateComponent.getRoot().setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Fetch books only if they haven't been fetched yet
        if (!favouriteViewModel.isDataFetched()) {
            favouriteViewModel.fetchSavedBooks();
            favouriteViewModel.setDataFetched(true); // Mark data as fetched
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up the binding to avoid memory leaks
    }

    @Override
    public void onCardClick(BookModel book) {
        ((MainActivity) activity).openBookDetailsFragment(book);
    }

    @Override
    public void onHeartIconClick(BookModel book, int position) {
        favouriteViewModel.deleteBook(book);
        bookAdapter.HandleDelete(position);
    }
}
