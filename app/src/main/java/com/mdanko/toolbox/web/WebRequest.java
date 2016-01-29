package com.mdanko.toolbox.web;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebRequest<T> {
    Gson gson;
    String url;
    byte[] postBytes;
    Object cancelTag;
    String contentType;
    Map<String, String> httpHeaders;
    Class<T> responseType;
    String wrapper;
    Map<String, String> params = new LinkedHashMap<>(); // ordered map to stablize url (caching efficiency)

    // TODO volley dependencies
    int http = Request.Method.DEPRECATED_GET_OR_POST;
    Response.Listener<T> onSuccess;
    Response.ErrorListener onError;

    // MUST be from volleys Request.Method, shame volley didn't provide enum :-(
    public WebRequest<T> http(int httpVerb) {
        this.http = httpVerb;
        return this;
    }

    public WebRequest<T> url(String url) {
        this.url = url;
        return this;
    }

    public WebRequest<T> onSuccess(Response.Listener<T> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public WebRequest<T> onError(Response.ErrorListener onError) {
        this.onError = onError;
        return this;
    }

    public WebRequest<T> httpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
        return this;
    }

    public WebRequest<T> withParam(String key, String val) {
        params.put(key, val);
        return this;
    }

    public WebRequest<T> contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public WebRequest<T> parser(Gson gson) {
        this.gson = gson;
        return this;
    }

    public WebRequest<T> wrapper(String wrapper) {
        this.wrapper = wrapper;
        return this;
    }

    public WebRequest<T> postBytes(byte[] postBytes) {
        this.postBytes = postBytes;
        return this;
    }

    public WebRequest<T> model(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    public WebRequest<T> cancelTag(Object cancelTag) {
        this.cancelTag = cancelTag;
        return this;
    }

    public void execute() {
        WebService.call(this);
    }
}