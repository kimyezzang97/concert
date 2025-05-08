package kr.concert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;


import static org.assertj.core.api.Assertions.assertThat;

public class RedisTest {

    // redis 연결 테스트
    @Test
    @DisplayName("redis 연결에 성공한다.")
    void masterSlaveReplicationTest() {
        // Master 연결
        try (Jedis master = new Jedis("localhost", 6377)) { // redis_master 가 6377 포트에 열려 있어야 함
            master.set("replication-test", "hello-world");
        }

        // Slave 연결
        try (Jedis slave = new Jedis("localhost", 6378)) { // redis_slave 가 6378 포트
            String value = slave.get("replication-test");
            assertThat(value).isEqualTo("hello-world");
        }
    }
}
