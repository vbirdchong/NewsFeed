package com.example.cccho.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by cccho on 2017/10/14.
 */

public class NewsContentLoader extends AsyncTaskLoader<List<NewsContent>>{

    private String mUrl;

    public NewsContentLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<NewsContent> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<NewsContent> newsContents = QueryUtils.fetchNewsContent(mUrl);
        return newsContents;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
