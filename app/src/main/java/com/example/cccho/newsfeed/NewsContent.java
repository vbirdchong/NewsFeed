package com.example.cccho.newsfeed;

/**
 * Created by cccho on 2017/10/11.
 */

public class NewsContent {

    private String mTitle;
    private String mSectionName;
    private String mWebUrl;
    private String mAuthor = null;
    private String mDate = null;

    public NewsContent(String title, String sectionName, String webUrl) {
        mTitle = title;
        mSectionName = sectionName;
        mWebUrl = webUrl;
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
}
