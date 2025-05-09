package io.hhplus.concert.domain.user;

import io.hhplus.concert.domain.reservation.SpELParser;
import lombok.RequiredArgsConstructor;
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
public class RedisSimpleLockAspect {

    private final StringRedisTemplate redisTemplate;
    private static final String LOCK_PREFIX = "Lock:";

    @Around("@annotation(redisSimpleLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisSimpleLock redisSimpleLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String dynamicKey = SpELParser.getDynamicKey(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                redisSimpleLock.key()
        );
        String lockKey = LOCK_PREFIX + dynamicKey;

        String lockValue = UUID.randomUUID().toString();
        boolean acquired = false;
        long endTime = System.currentTimeMillis() + redisSimpleLock.timeout();

        try {
            acquired = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, lockValue, Duration.ofSeconds(5));

            if (!acquired) {
                System.out.println("❌ 락 획득 실패: " + lockKey);
                throw new IllegalStateException("Redis Simple Lock 획득 실패: " + lockKey);
            }

            System.out.println("✅ 락 획득 성공: " + lockKey);  // 디버그

            return joinPoint.proceed();

        } finally {
            String currentValue = redisTemplate.opsForValue().get(lockKey);
            if (lockValue.equals(currentValue)) {
                redisTemplate.delete(lockKey);
            }
        }
    }
}


