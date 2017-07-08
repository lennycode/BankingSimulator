package com.grande.bank.bankingsimulator.Utilities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.net.CookiePolicy;
import java.net.URL;
import java.util.HashMap;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;



import java.net.CookiePolicy;
import java.net.URL;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {

    private int viewId = -1;

    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // restore the view id
        if (savedInstanceState != null && savedInstanceState.containsKey("myViewId")) {
            viewId = savedInstanceState.getInt("myViewId");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the view ID
        if (viewId > 0) {
            outState.putInt("myViewId", viewId);
        }
    }


    public void setActivityView(View view) {
        viewId = view.getId();
    }


    public DownloadFragmentWebPageTask DownloadFactory(AsyncResponse callBack) {
        return new DownloadFragmentWebPageTask(callBack);
    }

    public class DownloadFragmentWebPageTask extends AsyncTask<String, Void, Object> {
        public AsyncResponse delegate = null;//Call back interface
        java.net.CookieManager cookieManager = new java.net.CookieManager();
        Response currResponse;
        ProgressBar progressBar;
        //Removed. no cookies
        //HashMap<String, String> simpleCookies = AppCookieStore.simpleCookies;


        public DownloadFragmentWebPageTask(AsyncResponse asyncResponse, ProgressBar progress) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor

            progressBar = progress;
        }

        public DownloadFragmentWebPageTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor


        }

        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            // progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(String... urls) {
            // we use the OkHttp library from https://github.com/square/okhttp
            OkHttpClient client;
            try {

                OkHttpClient.Builder builder = new OkHttpClient.Builder();

                cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                client = new OkHttpClient.Builder()
                        //  .sslSocketFactory(sslContext.getSocketFactory()).
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            String reqMode = "default";
                     ;
//            try {
//                reqMode = urls[1];
//            } catch (Exception e) {
//                reqMode = "default";
//            }

            Request request = null;
            String px = "";
            try {
                URL thisURL = new URL(urls[0]);
                Log.v("URL-REQUEST", thisURL.toString());
                //default, pcapi
                if (reqMode.equals("default")) {
                    request = basicRequest(thisURL);
                } else if (reqMode.equals("img")) {
                    request = basicRequest(thisURL);
                } else if (reqMode.equals("post")) {

                   // request = postRequest(thisURL, urls[2], urls[3]);
                } else {
                    return "";
                }

                Response response = client.newCall(request).execute();
                String respbody = response.body().string();

                if (response.isSuccessful()) {

                    return respbody;
                } else {
                    return "";
                }


            } catch (Exception e) {
                Log.v("prb", e.toString());
            }

            return "";
        }

        Request postRequest(URL uRl, String post, String ref) {
            try {
                MediaType STD = MediaType.parse("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                RequestBody body = RequestBody.create(STD, post);
                return new Request.Builder()
                        .url(uRl)
                        .post(body)
                        .header("Host", uRl.getHost())
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0")

                        .addHeader("Accept-Language", "en-US,en;q=0.5")
                        .addHeader("Referer", ref)

                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Cache-Control", "no-cache")
                        .build();
            } catch (Exception e) {
                return null;
            }

        }


        Request basicRequest(URL url) {
            try {
                return new Request.Builder()
                        .url(url)
                        //add cookies or headers
//                        .header("Host", url.getHost())
//                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0")
//                        .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,**/;q=0.8")
//                        .addHeader("Accept-Language", "en-US,en;q=0.5")
//                        .addHeader("DNT", "1")
//                        .addHeader("Connection", "keep-alive")
//                        .addHeader("Upgrade-Insecure-Requests", "1")
                        .build();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {


            try {

                delegate.processFinish((String) result);
            } catch (Exception e) {
                Log.v("ERX", "Unable to create Response");
            }


        }


    }
}