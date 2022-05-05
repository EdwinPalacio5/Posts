package com.bim.posts.API;


public interface CallbackRequest {

    void onRequestComplete(String response, int statusCode, String errorMessage);

}
