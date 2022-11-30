package com.anchal.bookfinder;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
        // Later
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }

        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        final int READ_TIMEOUT = 10000;
        final int CONNECTION_TIMEOUT = 10000;
        final int VALID_RESPONSE_CODE = 200;

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            if (urlConnection.getResponseCode() == VALID_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Invalid response code : " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON response", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }


    static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request. ", e);
        }

        List<Book> book_list = fetchBookFromJSON(jsonResponse);

        return book_list;
    }


    private static List<Book> fetchBookFromJSON(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject book_info = currentBook.getJSONObject("volumeInfo");

                // Extracting author name
                String author_name = "";

                if (book_info.has("authors")) {
                    JSONArray authors = book_info.getJSONArray("authors");

                    if (book_info.isNull("authors")) {
                        author_name = "Unknown Author";
                    } else {
                        author_name = (String) authors.get(0);
                    }
                } else {
                    author_name = "Unknown Author";
                }

                JSONObject sale_info = currentBook.getJSONObject("saleInfo");
                String buy_link = sale_info.getString("buyLink");


                double price = sale_info.getJSONObject("retailPrice").getDouble("amount");
                String currency = sale_info.getJSONObject("retailPrice").getString("currencyCode");

                String language = book_info.getString("language");
                String title_string = book_info.getString("title");


                JSONObject image_link = book_info.getJSONObject("imageLinks");
                String imageUrl_String = image_link.getString("smallThumbnail");
                String publisher_name = book_info.getString("publisher");

                Log.println(Log.INFO, LOG_TAG, String.valueOf(imageUrl_String));


                Book bookItem = new Book(title_string, author_name, imageUrl_String, buy_link, price, currency, language, publisher_name);
                books.add(bookItem);


            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON Result.", e);
        }

        return books;
    }
}
