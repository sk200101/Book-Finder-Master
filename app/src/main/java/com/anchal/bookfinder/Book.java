package com.anchal.bookfinder;

public class Book {
    // Title of the book
    private String mTitle;

    // Book's Author name
    private String mAuthorName;

    // Book image URL
    private String mImageUrl;

    // Book's url
    private String mBookUrl;

    // Book's price
    private double mPrice;

    // Official currency of the country
    private String mCurrency;

    // Country language
    private String mLanguage;

    // Publisher name
    private String mPublisher;


    public Book(String title, String authorName, String imageUrl, String bookUrl, double price, String currency, String language, String publisher) {
        mTitle = title;
        mAuthorName = authorName;
        mImageUrl = imageUrl;
        mBookUrl = bookUrl;
        mPrice = price;
        mCurrency = currency;
        mLanguage = language;
        mPublisher = "By : " + publisher;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getBookUrl() {
        return mBookUrl;
    }

    public double getPrice() {
        return mPrice;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getPublisher() {
        return mPublisher;
    }
}
