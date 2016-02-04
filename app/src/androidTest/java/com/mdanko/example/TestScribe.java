package com.mdanko.example;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mdanko.example.web.WebRequest;

import java.util.ArrayList;
import java.util.List;

public class TestScribe {
//    WebService.init(getApplicationContext());
    WebRequest<YelpModel> req = new WebRequest<YelpModel>()
            .http(Request.Method.GET)
            .model(YelpModel.class)
            .url("https://api.yelp.com/v2/search")
            .withParam("term", "ethiopian")
            .withParam("location", "Toronto,On")
            .withParam("limit", "10")
            .onSuccess(new Response.Listener<YelpModel>() {
                @Override
                public void onResponse(YelpModel response) {
                    Log.i("MDanko", response.toString());
                }
            })
            .onError(new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
//    WebService.callOAuth(req);
}

class YelpModel {
    public Region region;
    public int total;
    public List<Business> businesses = new ArrayList<>();

    static class Region {
        public Span span;
        public Center center;
    }

    static class Center {
        public float latitude;
        public float longitude;
    }

    static class Span {
        public float latitudeDelta;
        public float longitudeDelta;
    }
}

class Business {
    public boolean is_claimed;
    public double rating;
    public String mobile_url; // full website
    public String rating_img_url;
    public int review_count;
    public String name;
    public String rating_img_url_small;
    public String url;
    public List<List<String>> categories = new ArrayList<>();
    public String phone;
    public String snippet_text;
    public String image_url;
    public String snippet_image_url;
    public String display_phone;
    public String rating_img_url_large;
    public String id;
    public boolean is_closed;
    public Location location;

    static class Location {
        public String city;
        public List<String> displayAddress = new ArrayList<>();
        public float geoAccuracy;
        public List<String> neighborhoods = new ArrayList<>();
        public String postalCode;
        public String countryCode;
        public List<String> address = new ArrayList<>();
        public Coordinate coordinate;
        public String stateCode;
        public String mobile_url;
    }

    static class Coordinate {
        public float latitude;
        public float longitude;
    }
}