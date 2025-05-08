package io.hhplus.concert.domain.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Order;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisDistributedLockAspect {

    private static final String LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient; // RedissonConfig 에서 만든 RedissonClient 를 주입

    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String dynamicKey = SpELParser.getDynamicKey(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        String lockName = LOCK_PREFIX + dynamicKey;

        RLock rLock = redissonClient.getLock(lockName);

        boolean available = false;

        try {
            available = rLock.tryLock(
                    distributedLock.waitTime(),
                    distributedLock.leaseTime(),
                    distributedLock.timeUnit()
            );

            if (!available) {
                throw new IllegalStateException("락 획득 실패: " + lockName);
            }

            return joinPoint.proceed();

        } finally {
            if (available && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }
}
