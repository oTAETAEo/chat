package hexa.chat.adapter.security.oauth2.strategy;

import hexa.chat.adapter.security.oauth2.OAuth2Response;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SocialUserConverterComposite {

    private final List<SocialUserConverter> converters;

    // List 형태로 생성자 주입을 받으면 스프링이 해당 타입의 모든 빈을 주입 해 준다.
    public SocialUserConverterComposite(List<SocialUserConverter> converters) {
        this.converters = (converters != null) ? List.copyOf(converters) : List.of();
    }

    public OAuth2Response convert(String registrationId, Map<String, Object> attributes) {
        return converters.stream()
            .filter(converter -> converter.supports(registrationId))
            .findFirst()
            .map(converter -> converter.convert(attributes))
            .orElseThrow(() -> new IllegalArgumentException("Unsupported social login provider: " + registrationId));
    }
}
