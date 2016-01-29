package com.mdanko.toolbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.mdanko.toolbox.web.WebRequest;
import com.mdanko.toolbox.web.WebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebService.init(getApplicationContext());
        new QuickTest();
    }
}

class QuickTest {
    public QuickTest() {
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

        new WebRequest<League[]>()
                .http(Request.Method.GET)
                .model(League[].class)
                .url("https://api.thescore.com/leagues")
                .onSuccess(new Response.Listener<League[]>() {
                    @Override
                    public void onResponse(League[] response) {
                        Log.i("MDanko", response.toString());
                    }
                })
                .execute();
    }
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

class League {
    //League
    public float daily_rollover_offset;
    public String daily_rollover_time;
    public String default_section;
    public String name;
    public String sport_name;
    public String sport_short_name;
    public boolean has_standings;
    public boolean has_leaders;
    public boolean has_photos;
    public boolean has_player_headshots;
    public String season_type;
    public String web_url;

    // both
    public boolean is_spotlight;
    public String spotlight_name;
    public boolean has_news; // breaking news is supported
    public String medium_name;
    public String short_name;
    public String slug;

    //Federation
    public String full_name;
    public String web_name;
    public boolean active; // unused
    public String dates_uri;
    public String events_uri;
    public ArrayList<League> leagues;

    public String competition_type;
    public int ordinal; // league order within federation

    // Contains the field name used to group standings. We treat it as a boolean,
    // if not null/empty then use the "group" field on standings for grouping. See SPORTS-544.
    public String grouped_standings_key;
}


