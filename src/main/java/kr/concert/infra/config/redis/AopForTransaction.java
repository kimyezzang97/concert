package kr.concert.infra.config.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component // @Transactional 적용위해 별도로 분리
public class AopForTransaction {

    /**
     * 기존 트랜잭션이 있더라도 중단하고, 새로운 트랜잭션을 생성하여 해당 메서드를 실행
     * 이 메서드 안에서 발생하는 작업은 외부 트랜잭션과 독립적으로 커밋/롤백
     *
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed(); // ex) 실제로 chargePoint() 등 호출
    }
}
