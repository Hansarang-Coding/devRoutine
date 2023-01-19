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

import java.util.Map;

@Slf4j
@Getter
public class OAuth2Attributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;

    @Builder
    public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String pictrue) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = pictrue;
    }

    public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) throws JsonProcessingException {
        log.info("userNameAttributeName = {}", new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(userNameAttributeName));
        log.info("attributes = {}", new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(attributes));

        String registration = registrationId.toLowerCase();
        switch (registration) {
            case "google":
                return ofGoogle(userNameAttributeName, attributes);
            case "github":
                return ofGithub(userNameAttributeName, attributes);
            default:
                throw new OAuth2RegistrationException();
        }
    }

    private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return null;
    }

    private static OAuth2Attributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        String name = ObjectUtils.isEmpty(attributes.get("name")) ? "login" : "name";
        String nickName = String.valueOf(attributes.get(name));
        String email = String.valueOf(attributes.get("email"));
        String avatarUrl = String.valueOf(attributes.get("avatar_url"));
        return OAuth2Attributes.builder()
                .name(nickName)
                .email(email)
                .pictrue(avatarUrl)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(UserRole.USER)
                .build();
    }
}
