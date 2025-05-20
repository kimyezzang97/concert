package kr.concert.infra.config.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncEventConfig {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 기본 설정값 (적절히 조정 가능)
        executor.setCorePoolSize(4);       // 기본 쓰레드 수
        executor.setMaxPoolSize(10);       // 최대 쓰레드 수
        executor.setQueueCapacity(100);    // 큐 용량
        executor.setThreadNamePrefix("async-thread-");

        executor.initialize();
        return executor;
    }
}
