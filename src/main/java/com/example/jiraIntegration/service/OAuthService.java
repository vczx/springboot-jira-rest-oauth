package com.example.jiraIntegration.service;

import com.example.jiraIntegration.dao.OAuthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    @Autowired
    OAuthDao oAuthDao;

    public String getRequestToken() {
        return oAuthDao.getRequestToken();
    }

    public String getAccessToken(String secret) {
        return oAuthDao.getAccessToken(secret);
    }

    public void getCreateMeta() {
        oAuthDao.getCreateMeta();
    }
}
