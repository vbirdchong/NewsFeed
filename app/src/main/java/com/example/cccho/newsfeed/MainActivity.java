package com.example.cccho.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsContent>>{

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String TEST_REQUEST_URL =
            "http://content.guardianapis.com/search?q=debates&api-key=test";

    public static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?";

    public static final String SHOW_TAGS = "show-tags";

    public static final String CONTRIBUTOR = "contributor";

    public static final String API_KEY = "test";

    public NewsContentAdapter mNewsContentAdapter;

    private static final int NEWSFEED_LOADER_ID = 1;

    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsContentListView = (ListView) findViewById(R.id.list);
        mNewsContentAdapter = new NewsContentAdapter(this, new ArrayList<NewsContent>());
        newsContentListView.setAdapter(mNewsContentAdapter);

        mEmptyTextView = (TextView) findViewById(R.id.empty_text);
        newsContentListView.setEmptyView(mEmptyTextView);

        newsContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsContent item = mNewsContentAdapter.getItem(position);
                Uri newsContentUri = Uri.parse(item.getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, newsContentUri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        if (isNetworkConnected()) {
            getLoaderManager().initLoader(NEWSFEED_LOADER_ID, null, this);
        } else {
            mEmptyTextView.setText(R.string.network_not_connect);
            ProgressBar loadingBar = (ProgressBar) findViewById(R.id.loading_spinner);
            loadingBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<NewsContent>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String searchTerm = sharedPrefs.getString(
                getString(R.string.settings_search_term_key),
                getString(R.string.settings_search_term_default));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", searchTerm);
        uriBuilder.appendQueryParameter(SHOW_TAGS, CONTRIBUTOR);
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return new NewsContentLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsContent>> loader, List<NewsContent> data) {
        mEmptyTextView.setText(R.string.empty_data_info);
        ProgressBar loadingBar = (ProgressBar) findViewById(R.id.loading_spinner);
        loadingBar.setVisibility(View.GONE);

        mNewsContentAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mNewsContentAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsContent>> loader) {
        mNewsContentAdapter.clear();
    }
}
