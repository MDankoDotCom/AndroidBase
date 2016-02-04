package com.mdanko.example.web;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.Map;

class VolleyGsonRequest <T> extends Request<T> {
    WebRequest builder;

    VolleyGsonRequest(WebRequest<T> builder) {
        super(builder.http, builder.url, builder.onError);
        this.builder = builder;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return builder.httpHeaders != null ? builder.httpHeaders : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        builder.onSuccess.onResponse(response);
    }

    public void addParam(String key, String value) {
        builder.params.put(key, value);
    }

    @Override
    protected Map<String, String> getParams() {
        return builder.params;
    }

    protected String getParamString() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> keys = builder.params.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            sb.append(String.format("&%s=%s", key, builder.params.get(key)));
        }
        String paramString = sb.toString();
        if (!paramString.isEmpty())
            paramString = sb.replace(0,1, "?").toString();
        return paramString;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String json = "";
        try {
            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.i("volley", " response: HTTP "
                    + response.statusCode + (response.notModified ? " (data in cache)" : "")
                    + " size: " + response.data.length);

            Object gsonObj = null;
            if (response.statusCode != HttpURLConnection.HTTP_NO_CONTENT
                    && builder.responseType != Void.class // request doesn't want anything
                    && response.statusCode != HttpURLConnection.HTTP_ACCEPTED) { // reports that DELETE can return this also

                Gson gson = builder.gson != null ? builder.gson : new Gson();
                if (builder.wrapper != null) {
                    JsonElement root = new JsonParser().parse(json);
                    JsonElement model = root.getAsJsonObject().get(builder.wrapper);
                    gsonObj = gson.fromJson(model, builder.responseType); // parse response object
                } else {
                    gsonObj = gson.fromJson(json, builder.responseType); // parse response object
                }
                if (gsonObj == null) // fail if entity is null ? debatable?
                    return Response.error(new ParseError(new Exception("NULL MODEL DATA")));
            }
            Log.i("model", "json size " + json.length());
            return Response.success((T) gsonObj, HttpHeaderParser.parseCacheHeaders(response));

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("volley", "failed to parse network response: " + builder.responseType);
            Log.e("volley", "response headers: " + response.headers.toString());
            Log.e("volley", json);
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return builder.postBytes;
    }

    @Override
    public String getBodyContentType() {
        return builder.contentType != null ? builder.contentType : super.getBodyContentType();
    }
}