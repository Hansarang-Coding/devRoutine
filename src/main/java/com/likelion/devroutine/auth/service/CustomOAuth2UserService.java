package com.likelion.devroutine.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.likelion.devroutine.auth.domain.User;
import com.likelion.devroutine.auth.dto.OAuth2Attributes;
import com.likelion.devroutine.auth.dto.SessionUser;
import com.likelion.devroutine.auth.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String SESSION_USER_NAME = "user";
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attributes attributes;
        try {
            attributes = OAuth2Attributes.of(registrationId,
                    userNameAttributeName, oAuth2User.getAttributes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        User user = saveOrUpdate(attributes);
        Set<SimpleGrantedAuthority> role = Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleKey()));
        httpSession.setAttribute(SESSION_USER_NAME, new SessionUser(user));

        return new DefaultOAuth2User(role, attributes.getAttributes(), attributes.getNameAttributeKey()); //OAuth2AuthenticationToken으로 변환
    }

    private User saveOrUpdate(OAuth2Attributes attributes) {
        String name = attributes.getName();
        String picture = attributes.getPicture();
        User user = userRepository.findByName(name)
                .map(entity -> entity.update(name, picture)) //유저를 불러 오는데, 이미 존재하면, 바뀐 이미지와 이름 반영
                .orElse(attributes.toEntity()); //유저를 불러 오는데, 존재 하지 않으면 새로 생성
        return userRepository.save(user);
    }
}
