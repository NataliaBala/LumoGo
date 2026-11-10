package pl.lumogo.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "security.jwt.secret=test-only-jwt-secret-that-is-at-least-32-bytes-long")
class LumoGoBackendApplicationTests {

    @Test
    void contextLoads() {
    }
}
