package com.example.jiraIntegration.api;

import com.example.jiraIntegration.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/1")
public class OAuthApi {
    @Autowired
    OAuthService oAuthService;

    @GetMapping("/getRequestToken")
    public String getRequestToken(){
        return oAuthService.getRequestToken();
    }

    @GetMapping("/getAccessToken/{secret}")
    public String getAccessToken(@PathVariable("secret") String secret){
        return oAuthService.getAccessToken(secret);
    }

    @GetMapping(value = "/getCreateMeta",produces = "application/json; charset=UTF-8")
    public void getCreateMeta(){
        oAuthService.getCreateMeta();
    }
}
