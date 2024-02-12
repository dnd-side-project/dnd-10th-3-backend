package dnd.donworry.domain.entity;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Slf4j
@RedisHash(value = "RefreshToken")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String key;
    @Indexed
    private String value;
    @TimeToLive
    private Long expiration;

    public void validateRefreshTokenRotate(String refreshToken) {
        if (!this.key.equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
