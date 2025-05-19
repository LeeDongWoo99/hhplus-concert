package io.hhplus.concert.infrastructure.lock.common.user;

import io.hhplus.concert.infrastructure.lock.reservation.SpELParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisSimpleLockAspect {
    private final StringRedisTemplate redisTemplate;
    private static final String LOCK_PREFIX = "Lock:";

    @Around("@annotation(redisSimpleLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisSimpleLock redisSimpleLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String lockKey = getLockKey(signature, joinPoint, redisSimpleLock);
        String lockValue = UUID.randomUUID().toString();

        acquireLock(lockKey, lockValue, redisSimpleLock);
        try {
            return joinPoint.proceed();
        } finally {
            releaseLock(lockKey, lockValue);
        }
    }

    private String getLockKey(MethodSignature signature, ProceedingJoinPoint joinPoint, RedisSimpleLock redisSimpleLock) {
        String dynamicKey = SpELParser.getDynamicKey(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                redisSimpleLock.key()
        );

        return LOCK_PREFIX + dynamicKey;
    }

    private void acquireLock(String lockKey, String lockValue, RedisSimpleLock redisSimpleLock) {
        boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, Duration.ofSeconds(5));
        if (!acquired) {
            log.error("❌ 락 획득 실패: {}", lockKey);
            throw new IllegalStateException("Redis Simple Lock acquisition failed: " + lockKey);
        }
        log.debug("✅ 락 획득 성공: {}", lockKey);
    }

    private void releaseLock(String lockKey, String lockValue) {
        String currentValue = redisTemplate.opsForValue().get(lockKey);
        if (lockValue.equals(currentValue)) {
            redisTemplate.delete(lockKey);
        }
    }
}


