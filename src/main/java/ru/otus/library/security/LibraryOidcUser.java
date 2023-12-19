package ru.otus.library.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;

public class LibraryOidcUser extends DefaultOidcUser {
    private OAuth2AccessToken accessToken;

    public LibraryOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken,
                           OAuth2AccessToken accessToken) {
        super(authorities, idToken);
        this.accessToken = accessToken;
    }

    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }
}
