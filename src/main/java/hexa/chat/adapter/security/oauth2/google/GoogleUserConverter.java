package hexa.chat.adapter.security.oauth2.google;

import hexa.chat.adapter.security.oauth2.strategy.SocialUserConverter;
import hexa.chat.adapter.security.oauth2.OAuth2Response;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GoogleUserConverter implements SocialUserConverter {

    private static final String GOOGLE = "google";

    @Override
    public boolean supports(String registrationId) {
        return GOOGLE.equals(registrationId);
    }

    @Override
    public OAuth2Response convert(Map<String, Object> attributes) {
        return new GoogleResponse(attributes);
    }
}
