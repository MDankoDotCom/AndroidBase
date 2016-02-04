package com.mdanko.example.web;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.Map;

public class VolleyOAuthRequest<T> extends VolleyGsonRequest<T> {
    // OAuth credentials below from the Yelp Developers API site:
    // http://www.yelp.com/developers/getting_started/api_access
    private static final String CONSUMER_KEY = "gWdQetirxGIF6dDkVjOgRg";
    private static final String CONSUMER_SECRET = "MtiEl-RkCwg7HVzXXuzNcGdzUfk";
    private static final String TOKEN = "iSq6bY3v1eLFr7LjPUsvoUylRYNuWhsb";
    private static final String TOKEN_SECRET = "geYpq0kwqGcM8VA33u3uZGzWeps";

    private static Token accessToken = new Token(TOKEN, TOKEN_SECRET);
    private static OAuthService service = new ServiceBuilder()
            .provider(TwoStepOAuth.class)
            .apiKey(CONSUMER_KEY)
            .apiSecret(CONSUMER_SECRET)
            .build();

    private OAuthRequest oAuthRequest;

    VolleyOAuthRequest(WebRequest<T> builder) {
        super(builder);
    }

    @Override
    public String getUrl() {
        if (oAuthRequest == null) {
            buildOAuthRequest();

            for (Map.Entry<String, String> entry : oAuthRequest.getOauthParameters().entrySet()) {
                addParam(entry.getKey(), entry.getValue());
            }
        }
        return super.getUrl() + getParamString();
    }

    private void buildOAuthRequest() {
        oAuthRequest = new OAuthRequest(getVerb(), super.getUrl());
        for (Map.Entry<String, String> entry : getParams().entrySet()) {
            oAuthRequest.addQuerystringParameter(entry.getKey(), entry.getValue());
        }
        service.signRequest(accessToken, oAuthRequest);
    }

    private Verb getVerb() {
        switch (getMethod()) {
            case Method.GET:
                return Verb.GET;
            case Method.DELETE:
                return Verb.DELETE;
            case Method.POST:
                return Verb.POST;
            case Method.PUT:
                return Verb.PUT;
            default:
                return Verb.GET;
        }
    }

    /**
     * Generic service provider for two-step OAuth10a.
     */
    public static class TwoStepOAuth extends DefaultApi10a {
        @Override
        public String getAccessTokenEndpoint() {
            return null;
        }

        @Override
        public String getAuthorizationUrl(Token arg0) {
            return null;
        }

        @Override
        public String getRequestTokenEndpoint() {
            return null;
        }
    }
}