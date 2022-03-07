package com.sunfinance.hometask.verification.core.cache;

import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;

import java.time.Duration;

public interface CacheService {

    boolean saveIfAbsent(String key, VerificationCreateEvent event, Duration ttl);

}
