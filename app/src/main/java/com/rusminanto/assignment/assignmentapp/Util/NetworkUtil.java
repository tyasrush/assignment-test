package com.rusminanto.assignment.assignmentapp.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tyasrus on 18/05/16.
 */
public class NetworkUtil {

    private OnLoadDataFinishedListener onLoadDataFinishedListener;

    public void setOnLoadDataFinishedListener(OnLoadDataFinishedListener onLoadDataFinishedListener) {
        this.onLoadDataFinishedListener = onLoadDataFinishedListener;
    }

    public boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public void getData(String[] param, final HttpMethod httpMethod) throws ExecutionException, InterruptedException {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String resultData = null;
                OkHttpClient client = new OkHttpClient();
                Request request;
                if (httpMethod == HttpMethod.POST) {
                    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, params[1]);
                    request = new Request.Builder()
                            .url(params[0])
                            .post(body)
                            .build();
                    Response response;
                    try {
                        response = client.newCall(request).execute();
                        resultData = response.body().string();
                        return response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (httpMethod == HttpMethod.GET) {
                    request = new Request.Builder()
                            .url(params[0])
                            .build();
                    Response response;
                    try {
                        response = client.newCall(request).execute();
                        resultData = response.body().string();
                        return response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (onLoadDataFinishedListener != null) {
                    onLoadDataFinishedListener.onLoadDataFinished(resultData);
                }

                return null;
            }
        }.execute(param);
    }

    public String setJsonParameter(String field, String value) {
        return "{'" + field + "':'" + value + "'}";
    }

    public interface OnLoadDataFinishedListener {
        void onLoadDataFinished(String result);
    }

    enum HttpMethod {
        POST, GET
    }
}
