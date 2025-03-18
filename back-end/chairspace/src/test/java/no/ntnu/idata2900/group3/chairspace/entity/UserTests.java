package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.HashSet;

import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ReservedException;
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
		try {
			user = new User.Builder("John", "Test")
				.email("test@test.no")
				.build();
			areaType = new AreaType("Test Type", "Test Descripton");
			area = new Area.Builder("Test Area", 123, areaType).administrator(user).build();
			area2 = new Area.Builder("Test Area 2", 23, areaType).administrator(user).build();
			user.addArea(area);
			user.addArea(area2);
		} catch (Exception e) {
			fail("Failed to set up variables: " + e.getMessage(), e);
			return;
		}
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
		User newUser;
		try {
			newUser = new User.Builder(firstName, lastName)
				.email(email)
				.areas(areas)
				.build();
		} catch (Exception e) {
			fail("Failed to create user" + e.getMessage(), e);
			return;
		}

		assertEquals(firstName, newUser.getFirstName());
		assertEquals(lastName, newUser.getLastName());
		assertEquals(email, newUser.getEmail());
		assertEquals(areas, newUser.getAreas());

	}

	@Test
	void testNullFirstNameThrows() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new User.Builder(null, "Test")
		);
	}

	@Test
	void testBlankFirstNameThrows() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new User.Builder("", "Test")
		);
	}

	@Test
	void testNullLastNameThrows() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new User.Builder("Hello", null)
		);
	}

	@Test
	void testBlankLastNameThrows() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new User.Builder("Test", "")
		);
	}

	@Test
	void testAddSingleAreaThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";
		User newUser;
		try {
			newUser = new User.Builder(firstName, lastName)
				.area(area)
				.build();
		} catch (Exception e) {
			fail("Failed to create user" + e.getMessage(), e);
			return;
		}

		assertTrue(newUser.getAreas().contains(area));
	}

	@Test
	void testAddSingleNullAreaThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";
		User.Builder builder;
		try {
			builder = new User.Builder(firstName, lastName);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> builder.area(null)
		);
	}

	@Test
	void testAddNullEmailThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";
		User.Builder builder;
		try {
			builder = new User.Builder(firstName, lastName);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> builder.email(null)
		);
	}

	@Test
	void testAddBlankEmailThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";
		User.Builder builder;
		try {
			builder = new User.Builder(firstName, lastName);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> builder.email("")
		);
	}

	@Test
	void testAddNullAreasThroughBuilder() {
		String firstName = "Jon";
		String lastName = "Test";
		User.Builder builder;
		try {
			builder = new User.Builder(firstName, lastName);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> builder.areas(null)
		);
	}

	/* ---- Method Tests ---- */

	@Test
	void testAddReservations() {
		HashSet<Reservation> reservations = new HashSet<>();
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			user.addReservations(reservations);
		} catch (Exception e) {
			fail("Failed to add reservations" + e.getMessage(), e);
			return;
		}

		assertEquals(user.getReservations(), reservations);
	}

	@Test
	void testAddNullReservation() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> user.addReservation(null)
		);
	}

	@Test
	void addNullReservations() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> user.addReservations(null)
		);
	}

	@Test
	void removeReservation() {
		final Reservation reservation;
		final Reservation reservation2;
		try {
			reservation = new Reservation(
				area,
				user,
				LocalDateTime.now().plusHours(1),
				LocalDateTime.now().plusHours(4),
				"Hello"
			);
			reservation2 = new Reservation(
				area,
				user,
				LocalDateTime.now().plusHours(5),
				LocalDateTime.now().plusHours(7),
				"Hello"
				);
		} catch (ReservedException e) {
			fail("Failed to create reservation" + e.getMessage());
			return;
		} catch (InvalidArgumentCheckedException e) {
			fail("Failed to create reservation" + e.getMessage());
			return;
		}

		assertNotNull(reservation2);
		assertNotNull(reservation);

		HashSet<Reservation> reservations = new HashSet<>();
		reservations.add(reservation);
		reservations.add(reservation2);
		try {
			user.addReservations(reservations);
		} catch (Exception e) {
			fail("Failed to add reservations" + e.getMessage(), e);
			return;
		}

		assertDoesNotThrow(
			() -> user.removeReservation(reservation2)
		);
		assertFalse(user.getReservations().contains(reservation2));
	}

	@Test
	void testAddArea() {
		Area newArea;
		try {
			newArea = new Area.Builder("Name", 234, areaType).administrator(user).build();
			user.addArea(newArea);
		} catch (Exception e) {
			fail("Failed to add area" + e.getMessage(), e);
			return;
		}
		assertTrue(user.getAreas().contains(newArea));
	}

	@Test
	void testAddNullArea() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> user.addArea(null)
		);
	}

	@Test
	void testRemoveArea() {
		try {
			user.removeArea(area);
		} catch (Exception e) {
			fail("Failed to remove area" + e.getMessage(), e);
			return;
		}
		assertFalse(user.getAreas().contains(area));
	}

	@Test
	void testRemoveNullArea() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> user.removeArea(null)
		);
	}

	@Test
	void testRemoveAllAndOneAreas() {
		try {
			user.removeArea(area);
			user.removeArea(area2);
		} catch (Exception e) {
			fail("Failed to remove areas: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalStateException.class,
			() -> user.removeArea(area)
		);
	}
}

