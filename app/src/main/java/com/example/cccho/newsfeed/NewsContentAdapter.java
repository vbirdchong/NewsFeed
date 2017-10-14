package com.example.cccho.newsfeed;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cccho on 2017/10/11.
 */

public class NewsContentAdapter extends ArrayAdapter<NewsContent> {

    static class ViewHolder {
        TextView mSectionName;
        TextView mWebTitle;
    }

    public NewsContentAdapter(Activity context, ArrayList<NewsContent> newsContents) {
        super(context, 0, newsContents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NewsContent newsContentItem = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mWebTitle = (TextView) view.findViewById(R.id.news_title);
            viewHolder.mSectionName = (TextView) view.findViewById(R.id.section_name);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mSectionName.setText(newsContentItem.getSectionName());
        viewHolder.mWebTitle.setText(newsContentItem.getTitle());

        return view;
    }
}
