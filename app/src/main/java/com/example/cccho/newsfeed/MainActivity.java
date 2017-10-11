package com.example.cccho.newsfeed;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsContent>>{

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String TEST_REQUEST_URL =
            "http://content.guardianapis.com/search?q=debates&api-key=test";

    public NewsContentAdapter mNewsContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsContentListView = (ListView) findViewById(R.id.list);
        mNewsContentAdapter = new NewsContentAdapter(this, new ArrayList<NewsContent>());
        newsContentListView.setAdapter(mNewsContentAdapter);

        //TODO: 添加空内容和进度条的显示

    }

    @Override
    public Loader<List<NewsContent>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<NewsContent>> loader, List<NewsContent> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<NewsContent>> loader) {

    }
}
