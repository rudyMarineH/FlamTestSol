package org.taf.api.base;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.taf.api.config.ApiTestConfig;

@SpringBootTest(classes = ApiTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseApiTest {
}
