package com.anchal.bookfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class BookAdapter extends ArrayAdapter<Book> {
    private static final String LOG_TAG = BookAdapter.class.getSimpleName();

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Getting the current book
        final Book currentBook = getItem(position);

        // Setting the Image in the imageView.
        ImageView cover_image_view = (ImageView) listItemView.findViewById(R.id.cover_image);
        String image_url_string = currentBook.getImageUrl();

        // Setting image fetched form the image_url with help from picasso.
        Picasso.with(getContext()).load(currentBook.getImageUrl()).into(cover_image_view);


        // Setting the name of author in the author_text_view.
        TextView author_text_view = (TextView) listItemView.findViewById(R.id.author_textView);
        String author_name = currentBook.getAuthorName();
        author_text_view.setText(author_name);


        // Setting the Title of the Book into it's place.
        TextView title_text_view = (TextView) listItemView.findViewById(R.id.title_textView);
        String title = currentBook.getTitle();
        title_text_view.setText(title);


        // Setting the language of the book into it's position.
        TextView country_language_text_view = (TextView) listItemView.findViewById(R.id.country_code_textView);
        String country_language = currentBook.getLanguage();
        country_language_text_view.setText(country_language);


        // Setting the Price of the book.
        TextView price_text_view = (TextView) listItemView.findViewById(R.id.book_price_textView);
        String price = String.valueOf(formatPrice(currentBook.getPrice()));
        price_text_view.setText(price);

        // Finally setting the currency in which price has been written.
        TextView currency_text_view = (TextView) listItemView.findViewById(R.id.currency_textView);
        String currency = currentBook.getCurrency();
        currency_text_view.setText(currency);

        // Setting publisher name
        TextView publisher_text_view = (TextView) listItemView.findViewById(R.id.publisher_textView);
        String publisher_name = currentBook.getPublisher();
        publisher_text_view.setText(publisher_name);

        return listItemView;

    }

    private String formatPrice(double price) {
        DecimalFormat priceFormat = new DecimalFormat("0.00");
        return priceFormat.format(price);
    }

}
