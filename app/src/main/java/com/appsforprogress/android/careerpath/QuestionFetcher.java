package com.appsforprogress.android.careerpath;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ORamirez on 6/17/2017.
 */

public class QuestionFetcher
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

    public List<Question> fetchItems()
    {
        List<Question> items = new ArrayList<>();

        try
        {
            String webPage = "https://services.onetcenter.org/ws/mnm/interestprofiler/questions?start=1&end=60";

            /*
            -- Build specific webpage urls:
            String url = Uri.parse(webPage).buildUpon()
                    .appendQueryParameter("start", "1")
                    .appendQueryParameter("end", "60")
                    .build()
                    .toString();

            String xmlString = getUrlString(url);
            */

            /*
            String name = "";
            String password = "";

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

            while ((numCharsRead = isr.read(charArray)) > 0)
            {
                sb.append(charArray, 0, numCharsRead);
            }

            String result = sb.toString();

            Log.i(TAG, "*** BEGIN ***");
            Log.i(TAG, "Results: " + result);
            Log.i(TAG, "*** END ***");

            // Parse XML Feed for questions:
            parseItems(items, result);

        } catch (XmlPullParserException xpe) {
            Log.i(TAG,  "XML Parser Except: ", xpe);
        } catch (IOException ioe) {
            Log.i(TAG,  "IO Except: ", ioe);
        }

        // Insert items into DB:


        return items;
    }

    private void parseItems(List<Question> questionList, String xmlFeed) throws IOException, XmlPullParserException
    {
        // In a data item we care about:
        boolean inDataItemTag = false;
        // What tag we are in:
        String currentTagName = "";

        // Current item
        Question question = null;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        // Read the String content from API:
        parser.setInput(new StringReader(xmlFeed));

        int eventType = parser.getEventType();

        // XML Parser generates events for each tag:
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            switch (eventType)
            {
                case XmlPullParser.START_TAG:

                    currentTagName = parser.getName();

                    if (currentTagName.equals("question"))
                    {
                        inDataItemTag = true;
                        question = new Question();
                        question.setScore(0);
                        question.setAnswered(false);
                        questionList.add(question);
                    }
                    break;

                case XmlPullParser.END_TAG:

                    if (parser.getName().equals("question"))
                    {
                        inDataItemTag = false;
                    }
                    currentTagName = "";
                    break;

                case XmlPullParser.TEXT:

                    if (inDataItemTag && question != null)
                    {
                        switch (currentTagName)
                        {
                            case "area":
                                Log.i(TAG,  "Category: " + parser.getText());
                                question.setCategory(parser.getText());
                                break;

                            case "text":
                                Log.i(TAG,  "Text: " + parser.getText());
                                question.setText(parser.getText());
                                break;

                            default:
                                break;
                        }
                    }
                    break;
            }

            // Move on to the next tag element:
            eventType = parser.next();
        }
    }
}
