package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
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
	private Area area2;

	@BeforeEach
	void setUp() {
		user = new User.Builder("John", "Test")
			.email("test@test.no")
			.build();
		areaType = new AreaType("Test Type", "Test Descripton");
		area = new Area.Builder("Test Area", 123, areaType).administrator(user).build();
		area2 = new Area.Builder("Test Area 2", 23, areaType).administrator(user).build();
		user.addArea(area);
		user.addArea(area2);
	}

	/* ---- Builder Tests ---- */

	@DisplayName("TODO")
	@Test
	void testCreation() {
		HashSet<Area> areas = new HashSet<>();
		String email = "Jon@test.no";
		String firstName = "Jon";
		String lastName = "Test";
		areas.add(area);
		areas.add(area2);
		User newUser = new User.Builder(firstName, lastName)
			.email(email)
			.areas(areas)
			.build();

		assertEquals(firstName, newUser.getFirstName());
		assertEquals(lastName, newUser.getLastName());
		assertEquals(email, newUser.getEmail());
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
		User.Builder builder = new User.Builder(firstName, lastName);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.area(null)
		);
	}

	@Test
	void testAddNullEmailThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";
		User.Builder builder = new User.Builder(firstName, lastName);

		assertThrows(
			IllegalArgumentException.class,
			() -> builder.email(null)
		);
	}

	@Test
	void testAddBlankEmailThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";
		User.Builder builder = new User.Builder(firstName, lastName);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.email("")
		);
	}

	@Test
	void testAddNullAreasThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";
		User.Builder builder = new User.Builder(firstName, lastName);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.areas(null)
		);
	}

	/* ---- Method Tests ---- */

	@Test
	void testAddReservations() {
		HashSet<Reservation> reservations = new HashSet<>();
		reservations.add(
			new Reservation(
				area,
				user,
				LocalDateTime.now().plusHours(1),
				LocalDateTime.now().plusHours(4),
				"Hello"
			)
		);
		reservations.add(
			new Reservation(
				area,
				user,
				LocalDateTime.now().plusHours(5),
				LocalDateTime.now().plusHours(7),
				"Hello"
			)
		);

		user.addReservations(reservations);

		assertEquals(user.getReservations(), reservations);
	}

	@Test
	void testAddNullReservation() {
		assertThrows(
			IllegalArgumentException.class,
			() -> user.addReservation(null)
		);
	}

	@Test
	void addNullReservations() {
		assertThrows(
			IllegalArgumentException.class,
			() -> user.addReservations(null)
		);
	}

	@Test
	void removeReservation() {
		HashSet<Reservation> reservations = new HashSet<>();
		Reservation reservation = new Reservation(
			area,
			user,
			LocalDateTime.now().plusHours(1),
			LocalDateTime.now().plusHours(4),
			"Hello"
		);
		Reservation reservation2 = new Reservation(
			area,
			user,
			LocalDateTime.now().plusHours(5),
			LocalDateTime.now().plusHours(7),
			"Hello"
		);
		reservations.add(reservation);
		reservations.add(reservation2);
		user.addReservations(reservations);

		assertDoesNotThrow(
			() -> user.removeReservation(reservation2)
		);
		assertFalse(user.getReservations().contains(reservation2));
	}

	@Test
	void testAddArea() {
		Area newArea = new Area.Builder("Name", 234, areaType).administrator(user).build();
		user.addArea(newArea);
		assertTrue(user.getAreas().contains(newArea));
	}

	@Test
	void testAddNullArea() {
		assertThrows(
			IllegalArgumentException.class,
			() -> user.addArea(null)
		);
	}

	@Test
	void testRemoveArea() {
		user.removeArea(area);
		assertFalse(user.getAreas().contains(area));
	}

	@Test
	void testRemoveNullArea() {
		assertThrows(
			IllegalArgumentException.class,
			() -> user.removeArea(null)
		);
	}

	@Test
	void testRemoveAllAndOneAreas() {
		user.removeArea(area);
		user.removeArea(area2);
		assertThrows(
			IllegalStateException.class,
			() -> user.removeArea(area)
		);
	}
}

