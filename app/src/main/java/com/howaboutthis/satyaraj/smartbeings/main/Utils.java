package com.howaboutthis.satyaraj.smartbeings.main;

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


class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();
    private static int fetchRequest = 0;
    private static int searchRequest = 1;

    static List<String> fetchNewsData(String requestUrl, int request) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error closing input stream",e);
        }

        return extractFeatureFromJson(jsonResponse, request);

    }

    private static List<String> extractFeatureFromJson(String jsonResponse,int request) {
        List<String> mListItem = new ArrayList<>();

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }
        try{
            if (request == fetchRequest) {
                JSONArray baseJsonResponse = new JSONArray(jsonResponse);
                JSONObject object;
                String name;

                for (int i = 0; i < baseJsonResponse.length(); i++) {
                    object = baseJsonResponse.getJSONObject(i);
                    name = object.getString("login");
                    mListItem.add(name);
                }
                return mListItem;

            }else if (request == searchRequest){
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);
                int count = baseJsonResponse.getInt("total_count");
                if (count == 0)
                    return null;
                else{
                    JSONArray jsonArray = baseJsonResponse.getJSONArray("items");
                    JSONObject object;
                    String name;

                    for (int i = 0; i<jsonArray.length();i++){
                        object = jsonArray.getJSONObject(i);
                        name = object.getString("login");
                        mListItem.add(name);
                    }
                    return mListItem;
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG,"Problem parsing the News JSON results",e);
        }
        return null;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse ="";

        if(url ==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream= null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error response code: "+ urlConnection.getResponseCode());
            }
        } catch (IOException e) {
           Log.e(LOG_TAG,"Problem retrieving the News JSON results.",e);
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line !=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try{
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Error with creating URL", e);
        }
        return url;
    }

}
