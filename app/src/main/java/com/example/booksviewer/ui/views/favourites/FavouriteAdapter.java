package com.example.booksviewer.ui.views.favourites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksviewer.R;
import com.example.booksviewer.domain.relation.BookWithDetails;
import com.bumptech.glide.Glide;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.BookViewHolder> {

    private List<BookWithDetails> books;
    private final OnBookClickListener listener;

    public FavouriteAdapter(OnBookClickListener listener) {
        this.listener = listener;
    }

    public void setBooks(List<BookWithDetails> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookWithDetails book = books.get(position);
        holder.bind(book, position);
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    public interface OnBookClickListener {
        void onBookClick(BookWithDetails book, int position);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        private final TextView bookTitle;
        private final TextView bookAuthors;
        private final ImageView bookThumbnail;
        private final ImageView heartIcon;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthors = itemView.findViewById(R.id.book_authors);
            bookThumbnail = itemView.findViewById(R.id.book_thumbnail);
            heartIcon = itemView.findViewById(R.id.heart_icon);
        }

        public void bind(BookWithDetails book, int position) {
            bookTitle.setText(book.bookEntity.getBookId()); // Set the book title or other properties here
            bookAuthors.setText("Author info here"); // Set authors info

            // Load book image using Glide
            Glide.with(bookThumbnail.getContext())
                    .load("image_url_here") // Replace with actual image URL or resource
                    .into(bookThumbnail);

            // Update heart icon based on book's saved status
            heartIcon.setImageResource(R.drawable.ic_heart_empty); // Update based on book saved status
        }
    }
}
