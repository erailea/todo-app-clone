package com.erailea.todoappclone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "RUN_FULL_TESTS", matches = "true")
class TodoAppCloneApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertTrue(true, "Context loads successfully");
	}

}
