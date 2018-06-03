package com.example.jiraIntegration.dao;

import com.example.jiraIntegration.auth.JiraOAuthClient;
import com.example.jiraIntegration.auth.OAuthContext;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Scanner;

@Repository
public class OAuthDao {

    @Value("${jira.oauth.consumerKey}")
    private String consumerKey;
    @Value("${jira.oauth.privateKey}")
    private String privateKey;

    @Value("${jira.url.createMeta}")
    private String createMetaUrl;


    @Autowired
    JiraOAuthClient jiraOAuthClient;
    @Autowired
    HttpRequestFactory httpRequestFactory;

    public String getRequestToken() {
        try {
            OAuthContext.requestToken = jiraOAuthClient.getAndAuthorizeTemporaryToken(consumerKey, privateKey);
            return "Request Token Loaded : " + OAuthContext.requestToken;
        } catch (Exception e) {
            e.printStackTrace();
            return "Operation Failed";
        }
    }

    public String getAccessToken(String secret) {
        try {
                String accessToken = jiraOAuthClient.getAccessToken(OAuthContext.requestToken, secret, consumerKey,privateKey);
                OAuthContext.secret = secret;
                OAuthContext.accessToken = accessToken;
                return "Access Token Loaded " + OAuthContext.accessToken;
        } catch (Exception e) {
            e.printStackTrace();
            return "Operation Failed";
        }
    }


    public void getCreateMeta() {

        try {
            HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(createMetaUrl));
            HttpResponse response = request.execute();
            parseResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parseResponse(HttpResponse response) throws IOException {
        Scanner s = new Scanner(response.getContent()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        try {
            JSONObject jsonObj = new JSONObject(result);
            System.out.println(jsonObj.toString(2));
        } catch (Exception e) {
            System.out.println(result);
        }
    }
}
