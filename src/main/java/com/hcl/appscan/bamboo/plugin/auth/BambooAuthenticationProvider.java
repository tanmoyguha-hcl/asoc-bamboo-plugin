package com.hcl.appscan.bamboo.plugin.auth;

import com.hcl.appscan.sdk.auth.AuthenticationHandler;
import com.hcl.appscan.sdk.auth.IAuthenticationProvider;
import com.hcl.appscan.sdk.auth.LoginType;

import java.io.IOException;
import java.io.Serializable;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class BambooAuthenticationProvider implements IAuthenticationProvider, Serializable {
    String DEFAULT_SERVER = "https://stage.cloud.appscan.com";
    String userName;
    String password;
    String token;

    public BambooAuthenticationProvider(String userName, String password) {
        this.userName = userName;
        this.password = password;
        token = null;
    }

    @Override
    public boolean isTokenExpired() {
        boolean isExpired = false;
        AuthenticationHandler handler = new AuthenticationHandler(this);

        try {
            isExpired = handler.isTokenExpired() && !handler.login(userName, password, true, LoginType.ASoC_Federated);
        } catch (Exception e) {
            isExpired = false;
        }
        return isExpired;
    }

    @Override
    public Map<String, String> getAuthorizationHeader(boolean persist) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+ getToken().trim()); //$NON-NLS-1$ //$NON-NLS-2$
        if(persist)
            headers.put("Connection", "Keep-Alive"); //$NON-NLS-1$ //$NON-NLS-2$
        return headers;
    }

    private String getToken() {
        return token == null ? "" : token;
    }

    @Override
    public String getServer() {
        return DEFAULT_SERVER;
    }

    @Override
    public void saveConnection(String s) {
        token = s;
    }

    @Override
    public Proxy getProxy() {
        return Proxy.NO_PROXY;
    }
}
