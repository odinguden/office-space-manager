package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the User entity.
 */
class UserTests {


	@DisplayName("Test Creation of user")
	@Test
	void testCreation() {
		User user = new User.Builder("John", "Test")
			.email("test@test.no")
			.build();
		assertNotNull(user);
	}

	@DisplayName("Test Creation of user with null first name")
	@Test
	void testCreationNullFirstName() {
		assertThrows(IllegalArgumentException.class, () -> {
			User.Builder testBuilder = new User.Builder(null, "Test");
		});
	}
}
