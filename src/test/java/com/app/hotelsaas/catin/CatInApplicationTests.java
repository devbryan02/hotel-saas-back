package com.app.hotelsaas.catin;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires configured database/Flyway to load full Spring context")
class CatInApplicationTests {

    @Test
    void contextLoads() {
    }

}
