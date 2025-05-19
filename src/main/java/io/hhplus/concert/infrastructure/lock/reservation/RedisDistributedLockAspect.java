package io.hhplus.concert.infrastructure.lock.reservation;

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
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisDistributedLockAspect {

    private static final String LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String dynamicKey = SpELParser.getDynamicKey(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        String lockName = LOCK_PREFIX + dynamicKey;

        RLock rLock = redissonClient.getLock(lockName);
        boolean available;

        try {
            available = rLock.tryLock(
                    distributedLock.waitTime(),
                    distributedLock.leaseTime(),
                    distributedLock.timeUnit()
            );

            if (!available) {
                throw new IllegalStateException("락 획득 실패: " + lockName);
            }

            // 트랜잭션 동기화 후 unlock 등록
            registerLockReleaseAfterTransaction(rLock);

            return joinPoint.proceed();

        } catch (Exception e) {
            log.error("분산 락 처리 중 예외 발생", e);
            throw e;
        }
    }


    /**
     * 트랜잭션 커밋 후 또는 트랜잭션이 활성화되지 않았을 때 락을 해제하는 트랜잭션 핸들러
     */
    private class LockReleaseTransactionSynchronization implements TransactionSynchronization {
        private final RLock rLock;

        public LockReleaseTransactionSynchronization(RLock rLock) {
            this.rLock = rLock;
        }

        /**
         * 트랜잭션의 커밋 이후에 락을 해제
         */
        @Override
        public void afterCommit() {
            releaseLock(rLock);
        }

        /**
         * 트랜잭션의 상태가 롤백일 때 락을 해제
         */
        @Override
        public void afterCompletion(int status) {
            if (status != STATUS_COMMITTED) {
                releaseLock(rLock);
            }
        }
    }

    /**
     * 트랜잭션이 활성화된 경우, 트랜잭션 종료 시 락을 해제하도록 콜백을 등록,
     * 그렇지 않은 경우 즉시 락을 해제.
     */
    private void registerLockReleaseAfterTransaction(RLock rLock) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new LockReleaseTransactionSynchronization(rLock)
            );
        } else {
            releaseLock(rLock);
        }
    }

    /**
     * 현재 스레드가 갖고 있는 락을 해제
     */
    private void releaseLock(RLock rLock) {
        try {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                log.debug("락 해제 완료");
            }
        } catch (Exception e) {
            log.error("락 해제 중 예외 발생", e);
        }
    }
}