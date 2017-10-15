package com.example.cccho.newsfeed;

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

import static com.example.cccho.newsfeed.MainActivity.LOG_TAG;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by cccho on 2017/10/14.
 */

public class QueryUtils {

    private static String TEST_DATA =
            "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":5,\"startIndex\":1,\"pageSize\":10,\"currentPage\":1,\"pages\":1,\"results\":[{\"id\":\"technology/apple\",\"type\":\"keyword\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webTitle\":\"Apple\",\"webUrl\":\"https://www.theguardian.com/technology/apple\",\"apiUrl\":\"https://content.guardianapis.com/technology/apple\",\"references\":[]},{\"id\":\"technology/apple-tv\",\"type\":\"keyword\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webTitle\":\"Apple TV\",\"webUrl\":\"https://www.theguardian.com/technology/apple-tv\",\"apiUrl\":\"https://content.guardianapis.com/technology/apple-tv\",\"references\":[]},{\"id\":\"technology/wwdc\",\"type\":\"keyword\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webTitle\":\"Apple WWDC\",\"webUrl\":\"https://www.theguardian.com/technology/wwdc\",\"apiUrl\":\"https://content.guardianapis.com/technology/wwdc\",\"references\":[]},{\"id\":\"technology/apple-watch\",\"type\":\"keyword\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webTitle\":\"Apple Watch\",\"webUrl\":\"https://www.theguardian.com/technology/apple-watch\",\"apiUrl\":\"https://content.guardianapis.com/technology/apple-watch\",\"references\":[]},{\"id\":\"technology/apple-music\",\"type\":\"keyword\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webTitle\":\"Apple Music\",\"webUrl\":\"https://www.theguardian.com/technology/apple-music\",\"apiUrl\":\"https://content.guardianapis.com/technology/apple-music\",\"references\":[]}]}}";

    public QueryUtils() {

    }

    public static List<NewsContent> fetchNewsContent(String requestUrl) {

        URL url = createUrl(requestUrl);
        Log.i(LOG_TAG, "Request URL:" + url.toString());
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in make http request: " + e);
        }

        List<NewsContent> newsContents = extractDataFromJson(jsonResponse);
        return newsContents;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "URL error response: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem occur when read data from json: " + e);
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

    private static List<NewsContent> extractDataFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<NewsContent> newsContents = new ArrayList<>();

        try {
            final String STATUS_OK = "ok";

            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONObject response = baseJsonObject.getJSONObject("response");
            if (!response.getString("status").equals(STATUS_OK) ) {
                Log.w(LOG_TAG, "Json response status not correct: " + response.getString("status"));
                return null;
            }

            //Log.i(LOG_TAG, "total response: " + response.toString()); // for debug

            JSONArray responseResults = response.getJSONArray("results");
            for (int i = 0; i < responseResults.length(); i++) {
                JSONObject currentItem = responseResults.getJSONObject(i);
                //Log.i(LOG_TAG, "item:" + i + "\n" + currentItem.toString()); // for debug
                String webTitle = "";
                if (currentItem.has("webTitle")) {
                    webTitle = currentItem.getString("webTitle");
                }

                String sectionName = "";
                if (currentItem.has("sectionName")) {
                    sectionName = currentItem.getString("sectionName");
                }

                String webUrl = "";
                if (currentItem.has("webUrl")) {
                    webUrl = currentItem.getString("webUrl");
                }

                String webPublicationDate = "";
                if (currentItem.has("webPublicationDate")) {
                    webPublicationDate = currentItem.getString("webPublicationDate");
                }

                String authorWebTitle = "";
                if (currentItem.has("tags")) {
                    JSONArray tags = currentItem.getJSONArray("tags");

                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject item = tags.getJSONObject(j);
                        if (item.has("webTitle")) {
                            authorWebTitle = item.getString("webTitle");
                        }
                    }
                }

                newsContents.add(new NewsContent(webTitle, sectionName, webUrl, webPublicationDate, authorWebTitle));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Decode json info error: " + e);
        }

        return newsContents;
    }
}
