package com.example.booksviewer.ui.views.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booksviewer.R;
import com.example.booksviewer.domain.models.BookModel;
import com.example.booksviewer.domain.models.VolumeInfoModel;
import com.example.booksviewer.utils.OnBookClickListener;
import com.example.booksviewer.utils.OnSaveIconClickListener;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final List<BookModel> books;
    private final Context context;

    private final OnBookClickListener onBookClickListener;

    private final OnSaveIconClickListener onSaveIconClickListener;

    public BookAdapter(List<BookModel> books, Context context, OnSaveIconClickListener onSaveIconClickListener ,OnBookClickListener onBookClickListener) {
        this.books = books;
        this.context = context;
        this.onBookClickListener = onBookClickListener;
        this.onSaveIconClickListener = onSaveIconClickListener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookModel book = books.get(position);
        VolumeInfoModel volumeInfo = book.getVolumeInfo();

        // Set book title
        if (volumeInfo != null && volumeInfo.getTitle() != null) {
            holder.titleTextView.setText(volumeInfo.getTitle());
        } else {
            holder.titleTextView.setText("No Title");
        }

        // Set authors (join authors with commas if there are multiple)
        if (volumeInfo != null && volumeInfo.getAuthors() != null) {
            holder.authorsTextView.setText(String.join(", ", volumeInfo.getAuthors()));
        } else {
            holder.authorsTextView.setText("No Authors");
        }

        // Load book thumbnail using Glide from the 'smallThumbnail' URL
        if (volumeInfo != null && volumeInfo.getImageLinks() != null && volumeInfo.getImageLinks().getThumbnail() != null) {
            Glide.with(context)
                    .load(volumeInfo.getImageLinks().getThumbnail())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_clear)
                    .timeout(5000)
                    .into(holder.thumbnailImageView);
        } else {
            holder.thumbnailImageView.setImageResource(R.drawable.ic_launcher_foreground); // Default image
        }

        // Set up the Heart Icon as Save/Un-save toggle
        boolean isSaved = book.isSaved(); // This is where you need to track the save state (you might need to add an `isSaved` field in your BookModel)
        if (isSaved) {
            holder.heartIcon.setImageResource(R.drawable.ic_heart_filled); // Set to filled heart
        } else {
            holder.heartIcon.setImageResource(R.drawable.ic_heart_empty); // Set to empty heart
        }

        // Set up the Heart Icon as Save/Un-save toggle
        holder.heartIcon.setOnClickListener(v -> onSaveIconClickListener.onHeartIconClick(book, position));

        holder.itemView.setOnClickListener(v -> onBookClickListener.onCardClick(book));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    // Method to apply updates (add new books or clear current ones)
    public void applyUpdates(List<BookModel> bookModels) {

        if (bookModels != null && !bookModels.isEmpty()) {
            Log.d("BookAdapteaaaaaaaaaaaar", "Books updated: " + bookModels.size());
            // If bookModels is not empty, add new books
            this.books.addAll(bookModels);
            Log.d("BookAdapter", "Books updated: " + books.size());
            notifyItemRangeInserted(books.size() - bookModels.size(), bookModels.size());
        } else {
            // If bookModels is empty, clear the books list
            int currentSize = this.books.size();
            this.books.clear();
            Log.d("BookAdapter", "Books cleared, size: " + books.size());
            if (currentSize > 0) {
                notifyItemRangeRemoved(0, currentSize);
            }
        }
    }

    public List<BookModel> getCurrentBooks() {
        return books;
    }


    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorsTextView;
        ImageView thumbnailImageView;
        ImageView heartIcon; // Heart icon to toggle save/unsave

        public BookViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.book_title);
            authorsTextView = itemView.findViewById(R.id.book_authors);
            thumbnailImageView = itemView.findViewById(R.id.book_thumbnail);
            heartIcon = itemView.findViewById(R.id.heart_icon); // Correct reference to heart icon
        }
    }

    public void HandleUpdate(List<BookModel> bookModels) {

        if (bookModels != null && !bookModels.isEmpty() && books.size() < bookModels.size()) {
            Log.d("BookAdapteaaaaaaaaaaaar", "Books updated: " + bookModels.size());
            // If bookModels is not empty, add new books
            this.books.addAll(bookModels);
            Log.d("BookAdapter", "Books updated: " + books.size());
            notifyItemRangeInserted(books.size() - bookModels.size(), bookModels.size());
        }
    }

    public void HandleDelete(int position) {
        if (position >= 0 && position < books.size()) {
            books.remove(position);
            notifyItemRemoved(position);
        }
    }
}
