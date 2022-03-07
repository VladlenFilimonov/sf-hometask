package com.sunfinance.hometask.verification.core.cache;

import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import io.lettuce.core.RedisException;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class RedisCacheImpl implements CacheService {

    private final ValueOperations<String, VerificationCreateEvent> createEventCache;

    @Override
    public boolean saveIfAbsent(String key, VerificationCreateEvent event, Duration ttl) {
        var isCached = createEventCache.setIfAbsent(key, event, ttl);
        return getResult(isCached);
    }

    private boolean getResult(Boolean isCached) {
        if (isCached == null) {
            throw new RedisException("Redis transaction can't be applied");
        }

        return isCached;
    }
}
