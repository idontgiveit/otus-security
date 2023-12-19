package ru.otus.library.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LibraryOidcUserService extends OidcUserService {
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser user =  super.loadUser(userRequest);
        Map<String, List<String>> realmAccess = (Map<String, List<String>>)user.getUserInfo().getClaims().get("realm_access");
        List<String> roles = realmAccess.get("roles");

        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        authorities.addAll(user.getAuthorities());

        return new LibraryOidcUser(authorities, user.getIdToken(), userRequest.getAccessToken());
    }
}
