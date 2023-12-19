package ru.otus.library.approve.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(registry -> {
                    registry.anyRequest().authenticated();
                })
               .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));

//                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
