package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

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
		HashSet<Area> areas = new HashSet<>();
		String email = "Jon@test.no";
		String firstName = "Jon";
		String lastName = "Test";
		int phoneNumber = 12345678;
		areas.add(area);
		areas.add(area2);
		User newUser = new User.Builder(firstName, lastName)
			.email(email)
			.phoneNumber(phoneNumber)
			.areas(areas)
			.build();

		assertEquals(firstName, newUser.getFirstName());
		assertEquals(lastName, newUser.getLastName());
		assertEquals(email, newUser.getEmail());
		assertEquals(phoneNumber, newUser.getPhoneNumber());
		assertEquals(areas, newUser.getAreas());

	}

	@Test
	void testNullFirstNameThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new User.Builder(null, "Test")
			);
	}

	@Test
	void testBlankFirstNameThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new User.Builder("", "Test")
			);
	}

	@Test
	void testNullLastNameThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new User.Builder("Hello", null)
			);
	}

	@Test
	void testBlankLastNameThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new User.Builder("Test", "")
			);
	}

	@Test
	void testAddSingleAreaThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";
		User newUser = new User.Builder(firstName, lastName)
			.area(area)
			.build();

		assertTrue(newUser.getAreas().contains(area));
	}

	@Test
	void testAddSingleNullAreaThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";

		assertThrows(
			IllegalArgumentException.class,
			() -> new User.Builder(firstName, lastName).area(null)
			);
	}

	@Test
	void testAddNullEmailThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";

		assertThrows(
			IllegalArgumentException.class,
			() -> new User.Builder(firstName, lastName).email(null)
			);
	}

	@Test
	void testAddBlankEmailThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";

		assertThrows(
			IllegalArgumentException.class,
			() -> new User.Builder(firstName, lastName).email("")
			);
	}

	@Test
	void testAddNullPhoneNumberThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";

		assertThrows(
			IllegalArgumentException.class,
			() -> new User.Builder(firstName, lastName).phoneNumber(0)
			);
	}

	@Test
	void testAddNullAreasThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";

		assertThrows(
			IllegalArgumentException.class,
			() -> new User.Builder(firstName, lastName).areas(null)
			);
	}

	/* ---- Method Tests ---- */
	//TODO: Add tests for the following methods

	//Add reservation

	//Remove reservation

	//Add area

	//Remove area
}
