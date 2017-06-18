package com.appsforprogress.android.careerpath;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ORamirez on 6/17/2017.
 */

public class IPFetcher
{
    private static final String TAG = "IPFetcher";

    public byte[] getUrlBytes(String urlSpec) throws IOException
    {
        // Url to communicate with:
        URL url = new URL(urlSpec);
        // Open connection:
        HttpURLConnection connection  = (HttpURLConnection) url.openConnection();


        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            // Throw exception if connection rejected
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];

            // Repeatedly read from buffer:
            while ( (bytesRead = in.read(buffer)) > 0 )
            {
                // Write to output from buffer:
                out.write(buffer, 0, bytesRead);
            }

            out.close();
            return out.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    // Returns bytes as a String:
    public String getUrlString(String urlSpec) throws IOException
    {
        return new String(getUrlBytes(urlSpec));
    }

    public void fetchItems()
    {
        try {
            String webPage = "https://services.onetcenter.org/ws/mnm/interestprofiler/questions";
            /*
            String name = "apps_for_progress";
            String password = "2528txk";

            String authString = name + ":" + password;
            System.out.println("auth string: " + authString);
            byte[] authEncBytes = getUrlBytes(authString);
            */

            String authStringEnc = new String("YXBwc19mb3JfcHJvZ3Jlc3M6MjUyOHR4aw==");
            System.out.println("Base64 encoded auth string: " + authStringEnc);

            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            String result = sb.toString();

            Log.i(TAG, "*** BEGIN ***");
            Log.i(TAG, "Results: " + result);
            Log.i(TAG, "*** END ***");
        } catch (MalformedURLException e) {
            Log.i(TAG,  "Malformed Except: ", e);
        } catch (IOException e) {
            Log.i(TAG,  "IO Except: ", e);
        }
    }
}
