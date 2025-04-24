package kr.concert.infra.config.scheduiling;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Profile("!test") // test 프로파일이 아닐 때만 작동
@EnableScheduling
public class SchedulingConfig {
}
