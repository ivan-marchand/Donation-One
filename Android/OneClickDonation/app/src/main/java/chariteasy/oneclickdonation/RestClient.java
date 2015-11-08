package chariteasy.oneclickdonation;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ivan on 11/8/15.
 */
public class RestClient {
    private String mServerHost = "45.33.81.168";
    private String mServerPort = "10110";
    private JSONObject mJsonPost;

    public RestClient(JSONObject jsonObject) {
        mJsonPost = jsonObject;
    }

    public RestClient() {
    }

    public String readJSONFeed(String urlString) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("RestClient", "Connection failed : " + Integer.toString(statusCode));
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            Log.d("RestClient", "Error " + e.toString());
        }
        String result = stringBuilder.toString();
        Log.d("RestClient", "JsonDoc: " + result);
        return result;
    }

    protected JSONObject get(String path) {
        JSONObject json = new JSONObject();
        if (!mServerHost.isEmpty() && !mServerPort.isEmpty()) {
            try {
                String url = "http://" + mServerHost + ":" + mServerPort + path;
                Log.d("RestClient", "GET " + path);
                json = new JSONObject(readJSONFeed(url));
            } catch (Exception e) {
                Log.d("RestClient", "Error " + e.getMessage());
            }
        }
        return json;
    }
}
