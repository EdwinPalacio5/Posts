package com.bim.posts.API;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.toolbox.StringRequest;
import com.bim.posts.utils.AppMgr;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DLHttp{
    private String StringResponse;
    private int statusCode;
    private String errorMessage;
    public int method;
    public CallbackRequest callbackRequest;
    public String urlPath;
    public JSONObject body;


    public void execute(){
        StringRequest stringRequest = new StringRequest(method, urlPath,
            response -> {
                StringResponse = response;
                callbackRequest.onRequestComplete(StringResponse, statusCode, errorMessage);
            }, error -> {
                errorMessage = error.getMessage();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    statusCode = error.networkResponse.statusCode;
                    errorMessage = "Server error";
                }
                callbackRequest.onRequestComplete(StringResponse, statusCode, errorMessage);
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            public byte[] getBody() {
                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Nullable
            @Override
            public Response.ErrorListener getErrorListener() {
                return super.getErrorListener();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppMgr.ApiMgr.getRequestQueue().add(stringRequest);

    }


}
