package com.example.demo.util.converter;

import com.example.demo.util.KeycloakSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class KeycloakJwtAuthorityConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final KeycloakSecurityUtil keycloakSecurityUtil;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.keycloakSecurityUtil.extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

}
