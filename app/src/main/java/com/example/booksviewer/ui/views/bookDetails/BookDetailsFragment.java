package com.example.booksviewer.ui.views.bookDetails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.booksviewer.R;
import com.example.booksviewer.databinding.FragmentBookDetailsBinding;
import com.example.booksviewer.domain.models.BookModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BookDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_BOOK = "book";
    private FragmentBookDetailsBinding binding;

    private BookDetailsViewModel viewModel;
    private BookModel bookDetails;

    private Context context;

    public static BookDetailsFragment newInstance(BookModel book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOK, book); // Passing the book details as an object
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        if (getArguments() != null) {
            bookDetails = (BookModel) getArguments().getSerializable(ARG_BOOK);
        }

        viewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);

        if (bookDetails != null) {
            Log.d("hhhhhhhhhhhh", String.valueOf(bookDetails.isSaved()));
            updateUI(bookDetails);
        }

        // Check initial saved state
        if (bookDetails != null) {
            binding.favoriteButton.setChecked(!bookDetails.isSaved());
        }

        // Handle favorite button click and print its state
        binding.favoriteButton.setOnClickListener(this);

        // Handle preview link button
        binding.bookPreviewLinkButton.setOnClickListener(v -> {
            if (bookDetails != null) {
                openBookPreview(bookDetails.getVolumeInfo().getPreviewLink());
            }
        });
    }


    private void updateUI(BookModel book) {
        if (book != null) {
            binding.bookTitle.setText(book.getVolumeInfo().getTitle() != null ? book.getVolumeInfo().getTitle() : "N/A");
            binding.bookAuthors.setText(book.getVolumeInfo().getAuthors() != null && !book.getVolumeInfo().getAuthors().isEmpty() ? String.join(", ", book.getVolumeInfo().getAuthors()) : "N/A");
            binding.bookPublisher.setText(book.getVolumeInfo().getPublisher() != null ? book.getVolumeInfo().getPublisher() : "N/A");
            binding.bookPublishedDate.setText(book.getVolumeInfo().getPublishedDate() != null ? book.getVolumeInfo().getPublishedDate() : "N/A");
            binding.bookDescription.setText(book.getVolumeInfo().getDescription() != null ? book.getVolumeInfo().getDescription() : "N/A");
            binding.bookPageCount.setText(book.getVolumeInfo().getPageCount() > 0 ? String.valueOf(book.getVolumeInfo().getPageCount()) : "N/A");

            // Load the book thumbnail
            if (book.getVolumeInfo().getImageLinks() != null && book.getVolumeInfo().getImageLinks().getThumbnail() != null) {
                Glide.with(this)
                        .load(book.getVolumeInfo().getImageLinks().getThumbnail())
                        .into(binding.bookThumbnail);
            }
        }
    }

    private void openBookPreview(String previewLink) {
        if (previewLink != null && !previewLink.isEmpty()) {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    .build();
            customTabsIntent.launchUrl(context, Uri.parse(previewLink));
        } else {
            Toast.makeText(context, "Preview link not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }

    @Override
    public void onClick(View v) {
        boolean isChecked = binding.favoriteButton.isChecked();
        Log.d("Favorite Button", "Button checked state: " + isChecked);

        // Handle state changes or other actions
        viewModel.toggleBookSaveState(isChecked,bookDetails);

    }
}
