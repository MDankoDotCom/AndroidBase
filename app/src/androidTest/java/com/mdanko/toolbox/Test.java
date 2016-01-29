package com.mdanko.toolbox;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.mdanko.toolbox.web.WebRequest;

import java.util.HashMap;

public class Test {
    public Test() {
        new WebRequest<MetaModel>()
                .http(Request.Method.GET)
                .model(MetaModel.class)
                .url("https://esports-api.thescore.com/meta/android")
                .wrapper("meta")
                .onSuccess(new Response.Listener<MetaModel>() {
                    @Override
                    public void onResponse(MetaModel response) {
                        Log.i("MDanko", response.toString());
                    }
                })
                .execute();

        new WebRequest<WrappedMetaModel>()
                .http(Request.Method.GET)
                .model(WrappedMetaModel.class)
                .url("https://esports-api.thescore.com/meta/android")
                .onSuccess(new Response.Listener<WrappedMetaModel>() {
                    @Override
                    public void onResponse(WrappedMetaModel response) {
                        Log.i("MDanko", response.toString());
                    }
                })
                .execute();
    }

    class WrappedMetaModel {
        MetaModel meta;
    }

    class MetaModel {
        public Urls urls;
        public Texts texts;
        public HashMap<String, String> ad_unit_ids;

        class Urls {
            public String privacy;
            public String terms_of_use;
            public String copyright;
            public String policies;
            public String news_css;
            public String news_js;
            public String attributions;
            public String play_store;
        }

        class Texts {
            public String about;
            public String about_translation_key;
        }
    }
}
