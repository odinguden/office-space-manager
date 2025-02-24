package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
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

	private static AreaType areaType;
	private static Area area;
	private static Area area2;

	@BeforeAll
	static void setUpAreas() {
		AreaType testType = new AreaType("Test Type", "Test Descripton");
		User tempUser = new User.Builder("Argh", "REah").build();
		area = new Area.Builder("Test Area", 123, testType).administrator(tempUser).build();
		area2 = new Area.Builder("Test Area 2", 23, testType).administrator(tempUser).build();
	}

	@BeforeEach
	void setUp() {
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
		assertEquals("John", user.getFirstName());
	}

	@DisplayName("Test that last name is assigned")
	@Test
	void testLastName() {
		assertEquals("Test", user.getLastName());
	}

	@DisplayName("Test that email is assigned")
	@Test
	void testEmail() {
		assertEquals("test@test.no", user.getEmail());
	}

	@DisplayName("Test that phone number is assigned")
	@Test
	void testPhoneNumber() {
		assertEquals(01234567, user.getPhoneNumber());
	}


	/* ---- Method Tests ---- */
	//TODO: Add tests for the following methods

	//Add reservation

	//Remove reservation

	//Add area

	//Remove area
}
