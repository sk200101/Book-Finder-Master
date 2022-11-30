package com.anchal.bookfinder;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

// Created by Aman Anchal,
// Github - amananc

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int BOOK_LOADER_ID = 1;

    private boolean isConnected;
    private ListView book_list_view;
    private BookAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private View circle_spinner;
    private SearchView mSearchView;
    private String requestUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        checkConnection(conManager);

        book_list_view = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        book_list_view.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        book_list_view.setEmptyView(mEmptyStateTextView);

        circle_spinner = findViewById(R.id.loading_spinner);

        Button search_button = (Button) findViewById(R.id.search_button);
        mSearchView = (SearchView) findViewById(R.id.title_search_view);
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(true);
        mSearchView.setQueryHint("Enter a book title");

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            Log.e(LOG_TAG, "Problem connecting internet");

            circle_spinner.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection(conManager);

                if (isConnected) {
                    updateQueryUrl(mSearchView.getQuery().toString());
                    restartLoader();
                } else {
                    mAdapter.clear();

                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });

        book_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book currentBook = mAdapter.getItem(position);

                Uri buyBookUrl = Uri.parse(currentBook.getBookUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, buyBookUrl);

                startActivity(websiteIntent);
            }
        });
    }


    private String updateQueryUrl(String searchValue) {
        if (searchValue.contains(" ")) {
            searchValue = searchValue.replace(" ", "+");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://www.googleapis.com/books/v1/volumes?q=").append(searchValue).append("&filter=paid-ebooks&maxResults=40");

        requestUrl = stringBuilder.toString();
        return requestUrl;
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, requestUrl);
    }


    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        View circle_spinner = findViewById(R.id.loading_spinner);
        circle_spinner.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_books);
        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }


    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    public void restartLoader() {
        mEmptyStateTextView.setVisibility(View.GONE);
        circle_spinner.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, this);
    }

    private void checkConnection(ConnectivityManager conManager) {
        NetworkInfo connection_info = conManager.getActiveNetworkInfo();

        if (connection_info != null && connection_info.isConnectedOrConnecting()) {
            isConnected = true;
        } else {
            isConnected = false;
        }
    }
}