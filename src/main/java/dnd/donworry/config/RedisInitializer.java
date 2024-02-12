package dnd.donworry.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisInitializer implements CommandLineRunner {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(String... args) {
        clearRedisData();
    }

    private void clearRedisData() {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.flushAll();
            return null;
        });
    }


}
