package com.likelion.devroutine.auth.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.devroutine.auth.domain.User;
import com.likelion.devroutine.auth.domain.UserRole;
import com.likelion.devroutine.auth.exception.OAuth2RegistrationException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
@Getter
public class OAuth2Attributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;
    private final String oauthId;

    @Builder
    public OAuth2Attributes(
            Map<String, Object> attributes, String nameAttributeKey,
            String name, String email,
            String pictrue, String oauthId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = pictrue;
        this.oauthId = oauthId;
    }

    public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) throws JsonProcessingException {
        log.info("registrationId = {}", registrationId);
        log.info("userNameAttributeName = {}", new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(userNameAttributeName));
        log.info("attributes = {}", new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(attributes));

        if (StringUtils.hasText(registrationId))
            return ofGithub(userNameAttributeName, attributes);
        else
            throw new OAuth2RegistrationException();
    }

    private static OAuth2Attributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        String nickName = ObjectUtils.isEmpty(attributes.get("name")) ? "login" : "name";
        String userName = String.valueOf(attributes.get(nickName));
        String email = String.valueOf(attributes.get("email"));
        String picture = String.valueOf(attributes.get("avatar_url"));
        String oauthId = String.valueOf(attributes.get("id"));
        return OAuth2Attributes.builder()
                .name(userName)
                .email(email)
                .pictrue(picture)
                .oauthId(oauthId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .oauthId(oauthId)
                .role(UserRole.USER)
                .build();
    }
}
