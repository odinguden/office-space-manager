package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
	private static String firstName = "John";
	private static String lastName = "Test";
	private static String email = "JohTes@Test.no";
	private User user;

	@Test
	void testCreation() {
		try {
			user = new User(firstName, lastName, email);
		} catch (InvalidArgumentCheckedException e) {
			e.printStackTrace();
		}

		assertEquals(firstName, user.getFirstName());
		assertEquals(lastName, user.getLastName());
		assertEquals(email, user.getEmail());
	}

	@Test
	void testNullFirstNameThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new User(null, lastName, email),
			"Null first name does not throw"
		);
	}

	@Test
	void testBlankFirstNameThrows() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new User("", lastName, email),
			"Blank first name does not throw"
		);
	}

	@Test
	void testNullLastNameThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new User(firstName, null, email),
			"Null last name does not throw"
		);
	}

	@Test
	void testBlankLastNameThrows() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new User(firstName, "", email),
			"Null last name does not throw"
		);
	}

	@Test
	void testNullEmailThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new User(firstName, lastName, null),
			"Null email does not throw"
		);
	}

	@Test
	void testBlankEmailThrows() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new User(firstName, lastName, ""),
			"Blank email does not throw"
		);
	}

	@Test
	void testNonValidEmailThrows() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new User(firstName, lastName, "Email?"),
			"Invalid email does not throw"
		);
	}
}

