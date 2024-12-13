package com.example.booksviewer.ui.views.bookDetails;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.booksviewer.R;
import com.example.booksviewer.databinding.FragmentBookDetailsBinding;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BookDetailsFragment extends Fragment {

    private static final String ARG_BOOK = "book";
    private FragmentBookDetailsBinding binding;
    private String[] bookDetails;

    public static BookDetailsFragment newInstance(String[] bookDetails) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_BOOK, bookDetails); // Passing the book details as an array
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookDetails = getArguments().getStringArray(ARG_BOOK); // Get the book details array
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookDetailsBinding.inflate(inflater, container, false);

        if (bookDetails != null) {
            updateUI(bookDetails);
        }

        binding.favoriteButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
            }
        });

        binding.bookPreviewLinkButton.setOnClickListener(v -> {
            if (bookDetails.length > 7) {  // Ensuring the preview link exists in the array
                openBookPreview(bookDetails[7]);
            }
        });

        return binding.getRoot();
    }

    private void updateUI(String[] bookDetails) {
        if (bookDetails.length >= 7) {
            binding.bookTitle.setText(bookDetails[0]);
            binding.bookAuthors.setText(bookDetails[1]);
            binding.bookPublisher.setText(bookDetails[2]);
            binding.bookPublishedDate.setText(bookDetails[3]);
            binding.bookDescription.setText(bookDetails[4]);
            binding.bookPageCount.setText(bookDetails[5]);

            Glide.with(this)
                    .load(bookDetails[6])
                    .into(binding.bookThumbnail);


        }
    }

    private void openBookPreview(String previewLink) {
        if (previewLink != null && !previewLink.isEmpty()) {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    .build();
            customTabsIntent.launchUrl(getContext(), Uri.parse(previewLink));
        } else {
            Toast.makeText(getContext(), "Preview link not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
