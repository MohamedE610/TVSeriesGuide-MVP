package com.example.e610.tvappseriesguide.Data.RemoteDataSource;

import android.os.AsyncTask;
import android.util.Log;

import com.example.e610.tvappseriesguide.Data.DataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteData extends AsyncTask<Void, Void, String> implements Serializable{
    private String Url_Key;
    private String id;
    public static String Basic_Url = "http://api.themoviedb.org/3/tv";
    public static String PopSeries_Url = "/popular?api_key=d6464c49a9503b302888a4a139f1ed70";
    public static String TopSeries_Url = "/top_rated?api_key=d6464c49a9503b302888a4a139f1ed70";
    public static String TrailersSeries_Url = "/season/1/videos?api_key=d6464c49a9503b302888a4a139f1ed70";
    public static String recommendations_Url = "/recommendations?api_key=d6464c49a9503b302888a4a139f1ed70";



    public RemoteData() {
    }


    public RemoteData(String Key, String id) {
        Url_Key = Key;
        this.id = id;

    }

    DataSource.CallBacks networkOperations;

    public void setNetworkOperations(DataSource.CallBacks networkResponse) {
        this.networkOperations = networkResponse;
    }

    public String get_Data(String UrlKey) {
        HttpURLConnection urlConnect = null;
        BufferedReader reader = null;
        String JsonData = null;
        try {
            String UrlWithKey = UrlKey;
            URL url = new URL(UrlWithKey);
            urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setRequestMethod("GET");
            urlConnect.connect();
            InputStream inputStream = urlConnect.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            JsonData = buffer.toString();
            Log.d("JSON", JsonData);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            return null;
        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return JsonData;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String JsonData = "";
       // JsonData = get_Data(Basic_Url + PopSeries_Url);
        if (Url_Key.equals("Popular Movies"))
            JsonData = get_Data(Basic_Url + PopSeries_Url);
        else if (Url_Key.equals("Top Rated Movies"))
            JsonData = get_Data(Basic_Url + TopSeries_Url);
        else if (Url_Key.equals("TrailerModel"))
            JsonData = get_Data(Basic_Url + "/" + id + TrailersSeries_Url);
        else if (Url_Key.equals("Recommendations"))
            JsonData = get_Data(Basic_Url + "/" + id + recommendations_Url);


        return JsonData;
    }

    @Override
    protected void onPostExecute(String JsonData) {
        networkOperations.remoteDataLoaded(JsonData);
    }

}