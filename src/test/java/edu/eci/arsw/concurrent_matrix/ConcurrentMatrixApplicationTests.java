package edu.eci.arsw.concurrent_matrix;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration tests for the Concurrent Matrix Application.
 */
@SpringBootTest(args = "--test")
@TestPropertySource(properties = {"spring.main.allow-circular-references=true"})
class ConcurrentMatrixApplicationTests {

	@Test
	void contextLoads() {
		// Test that the Spring Boot context loads successfully
	}

	@Test
	void testApplicationCanStart() {
		// Test that the main application class exists and can be instantiated
		ConcurrentMatrixApplication app = new ConcurrentMatrixApplication();
		assertNotNull(app);
	}

	private void assertNotNull(Object obj) {
		if (obj == null) {
			throw new AssertionError("Object should not be null");
		}
	}
}
