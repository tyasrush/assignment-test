package com.rusminanto.assignment.assignmentapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tyasrus on 18/05/16.
 *
 * this class for handle request to server
 *
 */
public class NetworkUtil {

    private OnLoadDataFinishedListener onLoadDataFinishedListener;

    public void setOnLoadDataFinishedListener(OnLoadDataFinishedListener onLoadDataFinishedListener) {
        this.onLoadDataFinishedListener = onLoadDataFinishedListener;
    }

    /**
     * method for checking internet connection
     *
     * @param context context is like representation of intent in android, so context is mark for where activity belong
     * @return boolean status, true = connected, false =  not connected
     */
    public boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /**
     * method for load data from server
     *
     * @param param array string to set required parameter
     * @param httpMethod cursor for request method type
     * @param aCase cursor for loading type ui
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void getData(String[] param, final HttpMethod httpMethod, final Case aCase) throws ExecutionException, InterruptedException {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String resultData;
                OkHttpClient client = new OkHttpClient();
                Request request;
                if (httpMethod == HttpMethod.POST) {
                    RequestBody requestBody = null;
                    if (aCase == Case.LOGIN) {
                        requestBody = new FormBody.Builder()
                                .add("username", params[1])
                                .add("password", params[2])
                                .build();
                    }

                    if (aCase == Case.LOAD_BY) {
                        requestBody = new FormBody.Builder()
                                .add("city", params[1])
                                .build();
                    }

                    request = new Request.Builder()
                            .url(params[0])
                            .post(requestBody)
                            .build();
                    Response response;
                    try {
                        response = client.newCall(request).execute();
                        resultData = response.body().string();
                        /**
                         * if data are successfully loaded, then I use this listener to send data into ui
                         */
                        if (onLoadDataFinishedListener != null) {
                            onLoadDataFinishedListener.onLoadDataFinished(resultData);
                        }

//                        return response.body().string();
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

                        /**
                         * if data are successfully loaded, then I use this listener to send data into ui
                         */
                        if (onLoadDataFinishedListener != null) {
                            onLoadDataFinishedListener.onLoadDataFinished(resultData);
                        }

//                        return response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        }.execute(param);
    }

    /**
     * listener for receiving data
     */
    public interface OnLoadDataFinishedListener {
        void onLoadDataFinished(String result);
    }

    /**
     * cursor for http method
     */
    public enum HttpMethod {
        POST, GET
    }

    /**
     * cursor for loading type
     */
    public enum Case {
        LOGIN, LOAD_BY
    }
}
