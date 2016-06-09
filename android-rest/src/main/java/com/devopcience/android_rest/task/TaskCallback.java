package com.devopcience.android_rest.task;

import android.os.AsyncTask;

import com.devopcience.android_rest.client.APIClient;

/**
 * Created by brunodias on 08/06/2016.
 */
public interface TaskCallback {

    void onSuccess(APIClient apiClient, AsyncTask asyncTask);

    void onFailure(APIClient apiClient, AsyncTask asyncTask);

    void onCancel(APIClient apiClient, AsyncTask asyncTask);
}
