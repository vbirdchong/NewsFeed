package com.example.cccho.newsfeed;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by cccho on 2017/10/11.
 */

public class NewsContentAdapter extends ArrayAdapter<NewsContent> {

    public NewsContentAdapter(Activity context, ArrayList<NewsContent> newsContents) {
        super(context, 0, newsContents);
    }


}
