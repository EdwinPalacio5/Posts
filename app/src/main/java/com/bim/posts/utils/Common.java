package com.bim.posts.utils;

public interface Common {

    int _success_request = 200; //--> Success Request

    String URL_POSTS = "https://jsonplaceholder.typicode.com/posts";
    String URL_COMMENTS = URL_POSTS.concat("/%d/comments");
    String URL_PHOTOS = URL_POSTS.concat("/%d/photos");

}
