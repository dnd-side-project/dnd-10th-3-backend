package dnd.donworry.config;

import dnd.donworry.core.factory.YamlPropertySourceFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource(value = {"classpath:application-jwt.yml"}, factory = YamlPropertySourceFactory.class)
@Getter
@Setter
@ToString
@Component
public class JwtConfig {
    @Value("${jwt.token.secretKey}")
    private String secretKey;
    @Value("${jwt.access.token.expiration.seconds}")
    private Long accessExpirationTime;
    @Value("${jwt.refresh.token.expiration.seconds}")
    private Long refreshExpirationTime;
}