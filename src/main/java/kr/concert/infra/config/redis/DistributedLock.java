package kr.concert.infra.config.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD) // 메서드에만 붙일 수 있는 어노테이션
@Retention(RetentionPolicy.RUNTIME) // 런타임 시에도 어노테이션 정보를 사용할 수 있어, AOP에서 동적으로 처리가능
public @interface DistributedLock {

    /**
     * 락의 이름
     */
    String key();

    /**
     * 락의 시간 단위
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 락을 기다리는 시간 (default - 5s)
     * 락 획득을 위해 waitTime 만큼 대기한다
     */
    long waitTime() default 5L;

    /**
     * 락 임대 시간 (default - 3s)
     * 락을 획득한 이후 leaseTime 이 지나면 락을 해제한다
     */
    long leaseTime() default 3L;

}
