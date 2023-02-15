package com.likelion.devroutine.auth.config;

import com.likelion.devroutine.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String[] PERMIT_URL = {
            "/assets/**", "/js/**", "/css/**", "/webjars/**"
    };

    private final CustomOAuth2UserService customOAuth2UserService;
    private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
    private final CustomAuthenticationEntryPoint sessionAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .httpBasic().disable()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/challenges/**", "/").permitAll()
                        .requestMatchers(PERMIT_URL).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/challenges/**").permitAll()
                        .anyRequest().authenticated())

                .oauth2Login()
                .defaultSuccessUrl("/")
                .successHandler(oauth2AuthenticationSuccessHandler)
                .failureHandler(oauth2AuthenticationFailureHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService)

                .and()
                .and().logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and().exceptionHandling()
                .accessDeniedPage("/")
                .authenticationEntryPoint(sessionAuthenticationEntryPoint);
        return httpSecurity.build();
    }
}
