package com.niuzhendong.frame.core.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JwtToken
 * @author niuzhendong
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
