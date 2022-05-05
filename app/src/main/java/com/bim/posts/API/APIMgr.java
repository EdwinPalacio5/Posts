package com.bim.posts.API;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class APIMgr {
        private static APIMgr instance;
        private RequestQueue requestQueue;

        private static Context ctx;

        private APIMgr(Context context) {
            ctx = context;
            requestQueue = getRequestQueue();
        }

        public static synchronized APIMgr getInstance(Context context) {
            if (instance == null) {
                instance = new APIMgr(context);
            }
            return instance;
        }

        public RequestQueue getRequestQueue() {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
            }
            return requestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }
    }


