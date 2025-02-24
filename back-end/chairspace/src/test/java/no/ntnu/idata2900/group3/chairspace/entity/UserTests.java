package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the User entity.
 *
 * @see AreaFeature
 * @author Odin Lyngsgård
 * @version 0.1
 * @since 0.1
 */
class UserTests {
	private User user;
	private AreaType areaType;
	private Area area;

	@BeforeEach
	void setUp() {
		areaType = new AreaType("Test Type", "Test Description");
		area = new Area.Builder("Test Area", 20, areaType)
			.build();


		user = new User.Builder("John", "Test")
			.email("test@test.no")
			.phoneNumber(01234567)
			.build();
	}

	/* ---- Builder Tests ---- */

	@DisplayName("TODO")
	@Test
	void testCreation() {
		assertNotNull(user);
	}

	@DisplayName("Test that first name is assigned")
	@Test
	void testFirstName() {
		assertTrue(user.getFirstName().equals("John"));
	}

	@DisplayName("Test that last name is assigned")
	@Test
	void testLastName() {
		assertTrue(user.getLastName().equals("Test"));
	}

	@DisplayName("Test that email is assigned")
	@Test
	void testEmail() {
		assertTrue(user.getEmail().equals("test@test.no"));
	}

	@DisplayName("Test that phone number is assigned")
	@Test
	void testPhoneNumber() {
		assertTrue(user.getPhoneNumber() == 01234567);
	}


	/* ---- Method Tests ---- */
	//TODO: Add tests for the following methods

	//Add reservation

	//Remove reservation

	//Add area

	//Remove area
}
