package com.example.cccho.newsfeed;

/**
 * Created by cccho on 2017/10/11.
 */

public class NewsContent {

    private String mTitle;
    private String mSectionName;
    private String mWebUrl;
    private String mAuthor;
    private String mDate;

    private static final int DATA_MAX_LEN = 10;

    public NewsContent(String title, String sectionName, String webUrl, String publicatonDate, String author) {
        mTitle = title;
        mSectionName = sectionName;
        mWebUrl = webUrl;
        mDate = publicatonDate;
        mAuthor = author;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        String ret = mDate.substring(0, DATA_MAX_LEN);
        return ret;
    }
}
