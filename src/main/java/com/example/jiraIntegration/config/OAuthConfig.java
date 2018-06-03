package com.example.jiraIntegration.config;

import com.example.jiraIntegration.auth.JiraOAuthGetAccessToken;
import com.example.jiraIntegration.auth.JiraOAuthTokenFactory;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.example.jiraIntegration.auth.OAuthContext.secret;

@Configuration
public class OAuthConfig {

    @Autowired
    OAuthContextHolder contextHolder;

    @Autowired
    JiraOAuthTokenFactory jiraOAuthTokenFactory;

    @Bean
    public OAuthParameters getOAuthParameters() throws InvalidKeySpecException, NoSuchAlgorithmException {
        JiraOAuthGetAccessToken oAuthAccessToken = jiraOAuthTokenFactory.getJiraOAuthGetAccessToken(
                contextHolder.getAccessToken(), contextHolder.getSecret(), contextHolder.getConsumerKey(), contextHolder.getPrivateKey());
        oAuthAccessToken.verifier = secret;
        return oAuthAccessToken.createParameters();
    }

    @Bean
    public HttpRequestFactory getHttpRequestFactory(OAuthParameters parameters){
        return new NetHttpTransport().createRequestFactory(parameters);
    }
}
