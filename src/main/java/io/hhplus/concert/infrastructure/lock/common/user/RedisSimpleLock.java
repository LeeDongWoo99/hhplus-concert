package io.hhplus.concert.infrastructure.lock.common.user;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisSimpleLock {
    String key(); // SpEL 표현식
    long timeout() default 5000; // 락 획득 대기 시간 (ms)
}

