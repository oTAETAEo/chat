package hexa.chat.adapter.security.oauth2.strategy;

import hexa.chat.adapter.security.oauth2.OAuth2Response;

import java.util.Map;

public interface SocialUserConverter {

    boolean supports(String registrationId);

    OAuth2Response convert(Map<String, Object> attributes);
}
