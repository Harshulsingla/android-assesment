package com.example.booksviewer.ui.views.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksviewer.MainActivity;
import com.example.booksviewer.databinding.FragmentHomeBinding;
import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.utils.OnBookClickListener;
import com.example.booksviewer.utils.OnSaveIconClickListener;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements
        View.OnFocusChangeListener,
        TextView.OnEditorActionListener,
        OnBookClickListener,
        OnSaveIconClickListener {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private BookAdapter bookAdapter;
    private SearchHistoryAdapter searchHistoryAdapter;
    private final int resultsPerPage = 5;

    private int scrollPosition = 0;
    private int scrollOffset = 0; // To store the scroll offset
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        if (getActivity() != null) {
            activity = getActivity();
        }

        initAdapters();
        setupSearchBar();
        observeViewModelData();
        setupBackPressHandler();

        homeViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressBarBottom.setVisibility(View.VISIBLE); // Show ProgressBar
            } else {
                binding.progressBarBottom.setVisibility(View.GONE); // Hide ProgressBar
            }
        });
    }

    private void initAdapters() {
        bookAdapter = new BookAdapter(homeViewModel.getBooks().getValue() != null ? homeViewModel.getBooks().getValue() : new ArrayList<>(), getContext(), this, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(bookAdapter);

        searchHistoryAdapter = new SearchHistoryAdapter(new ArrayList<>(), this::handleSearchQuery);
        binding.searchHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.searchHistoryRecyclerView.setAdapter(searchHistoryAdapter);

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // Check if we are at the bottom and there are items loaded
                if (!recyclerView.canScrollVertically(1) && bookAdapter.getItemCount() > 0) {
                    loadMoreBooks();
                }
            }
        });
    }

    private void setupSearchBar() {
        binding.searchBar.searchInput.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        binding.searchBar.searchInput.setOnFocusChangeListener(this);
        binding.searchBar.searchInput.setOnEditorActionListener(this);
    }

    private void observeViewModelData() {
        homeViewModel.getBooks().observe(getViewLifecycleOwner(), bookModels -> {
            if (bookModels != null && !bookModels.equals(bookAdapter.getCurrentBooks())) {
                bookAdapter.applyUpdates(bookModels);
            }
        });

        homeViewModel.getSearchHistory().observe(getViewLifecycleOwner(), searchHistory -> {
            if (searchHistory != null) {
                searchHistoryAdapter.updateSearchHistory(searchHistory);
            }
        });
    }

    private void setupBackPressHandler() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.searchHistoryRecyclerView.getVisibility() == View.VISIBLE) {
                    toggleSearchHistoryVisibility(false);
                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    private void handleSearchQuery(String query) {
        binding.searchBar.searchInput.setText(query);
        homeViewModel.prepareApiCall(query, homeViewModel.getCurrentPage(), resultsPerPage);
        binding.searchBar.searchInput.clearFocus();
        toggleSearchHistoryVisibility(false);
    }

    private void loadMoreBooks() {
        // Get the current page from ViewModel and increase it to fetch the next set of books
        int currentPage = homeViewModel.getCurrentPage();
        currentPage++; // Increase the page
        homeViewModel.setCurrentPage(currentPage); // Update the ViewModel with the new page
        String query = binding.searchBar.searchInput.getText().toString().trim();
        if (!query.isEmpty()) {
            homeViewModel.prepareApiCall(query, currentPage * resultsPerPage, resultsPerPage);
        } else {
            Toast.makeText(getContext(), "No search query to load more results", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleSearchHistoryVisibility(boolean show) {
        binding.searchHistoryRecyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);

        if (!show) {
            closeKeyboard();
            binding.searchBar.searchInput.clearFocus();
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        toggleSearchHistoryVisibility(hasFocus);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, android.view.KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String query = binding.searchBar.searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                homeViewModel.prepareApiCall(query, homeViewModel.getCurrentPage(), resultsPerPage);
                toggleSearchHistoryVisibility(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onHeartIconClick(BookModel book, int position) {
        if (!book.isSaved()) {
            homeViewModel.saveBookToDb(book);
        } else {
            homeViewModel.unSaveBook(book);
        }
        book.setSaved(!book.isSaved());
        bookAdapter.notifyItemChanged(position);
    }

    @Override
    public void onCardClick(BookModel book) {
        String[] bookDetails = new String[]{
                book.getVolumeInfo().getTitle() != null ? book.getVolumeInfo().getTitle() : "No Title Available",
                book.getVolumeInfo().getAuthors() != null ? book.getVolumeInfo().getAuthors().toString() : "No Authors Available",
                book.getVolumeInfo().getPublisher() != null ? book.getVolumeInfo().getPublisher() : "No Publisher Available",
                book.getVolumeInfo().getPublishedDate() != null ? book.getVolumeInfo().getPublishedDate() : "No Publication Date Available",
                book.getVolumeInfo().getDescription() != null ? book.getVolumeInfo().getDescription() : "No Description Available",
                book.getVolumeInfo().getPageCount() > 0 ? String.valueOf(book.getVolumeInfo().getPageCount()) : "No Page Count Available",
                book.getVolumeInfo().getImageLinks() != null && book.getVolumeInfo().getImageLinks().getThumbnail() != null
                        ? book.getVolumeInfo().getImageLinks().getThumbnail()
                        : "No Image Available",
                book.getVolumeInfo().getPreviewLink() != null ? book.getVolumeInfo().getPreviewLink() : "No Preview Link Available"
        };
        ((MainActivity) activity).openBookDetailsFragment(bookDetails);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save the current scroll position and offset
        LinearLayoutManager layoutManager = (LinearLayoutManager) binding.recyclerView.getLayoutManager();
        if (layoutManager != null) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int topOffset = 0;
            View firstVisibleItem = layoutManager.findViewByPosition(firstVisibleItemPosition);
            if (firstVisibleItem != null) {
                topOffset = firstVisibleItem.getTop();
            }

            // Save the scroll position and offset
            scrollPosition = firstVisibleItemPosition;
            scrollOffset = topOffset;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Restore the scroll position and offset
        LinearLayoutManager layoutManager = (LinearLayoutManager) binding.recyclerView.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.scrollToPositionWithOffset(scrollPosition, scrollOffset);
        }
    }
}