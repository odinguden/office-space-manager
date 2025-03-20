package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import no.ntnu.idata2900.group3.chairspace.exceptions.AdminCountException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ReservedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the area class.
 * Contains tests for builder, and methods.
 *
 * @author Odin Lyngsgård
 * @version 0.1
 * @since 0.1
 * @see Area
 */
class AreaTests {
	// 24.02.2025, 14:55
	// I have been in the coding zone, however you might also call it coding delusion.
	// I worked so much on the area class not realizing that i need to write tests.
	// So now i have to reap what i sowed.
	// Im also writing all the tests without AI.
	// This might go without saying for some but its not a given anymore.
	// It just means that I have a lot of boilerplate to write.

	private static User nonAdminUser;
	private static User adminUser;
	private static User adminUser2;
	private static AreaFeature areaFeature;
	private static AreaFeature areaFeature1;
	private static AreaType areaType;

	/* ---- Setup ---- */

	/**
	 * Sets up the private variables for the class.
	 */
	@BeforeAll
	static void setup() {
		try {
			nonAdminUser = new User("Stig", "Arne", "Arne@email.no");
			adminUser = new User("Jon", "Kode", "Jon@Kode.no");
			adminUser2 = new User("Stig", "Kode", "Stig@Kode.no");
			areaFeature = new AreaFeature("F1", "Feature 1");
			areaFeature1 = new AreaFeature("F2", "Confusing i know");
			areaType = new AreaType("T1", "Type 1");
		} catch (Exception e) {
			fail("Failed to set up variables: " + e.getMessage(), e);
			return;
		}
	}

	/* ---- Builder tests ---- */

	@Test
	void testThatBuilderDoesNotThrow() {
		assertDoesNotThrow(() -> {
			new Area.Builder("Test", 123, areaType).administrator(adminUser).build();
		});
	}

	@Test
	void testThatBuilderThrowsIfBuildsWithoutAdmin() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 1232, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			AdminCountException.class,
			builder::build
		);
	}

	@Test
	void testThatBuilderThrowsIfNoName() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new Area.Builder(null, 1232, areaType)
		);
	}

	@Test
	void testThatBuilderThrowsIfBlank() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new Area.Builder("", 1232, areaType)
		);
	}

	@Test
	void testThatBuilderThrowsIfNoCapacity() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new Area.Builder("Test", 0, areaType)
		);
	}

	@Test
	void testThatBuilderThrowsIfNegativeCapacity() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new Area.Builder("Test", -1, areaType)
		);
	}

	@Test
	void testThatBuilderThrowsIfNoAreaType() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new Area.Builder("Test", 1232, null)
		);
	}

	@Test
	void testThatValuesAreAssignedCorrectly() {
		String name = "test";
		int capacity = 24;
		String description = "This is a test";
		String link = "test.no";
		Area superArea;
		Area area;
		try {
			superArea = new Area.Builder(name, capacity, areaType)
				.administrator(adminUser)
				.build();
			area = new Area.Builder(name, capacity, areaType)
				.administrator(adminUser)
				.description(description)
				.feature(areaFeature)
				.calendarLink(link)
				.superArea(superArea)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas: " + e.getMessage(), e);
			return;
		}


		assertEquals(name, area.getName(), "Name was not assigned correctly");
		assertEquals(capacity, area.getCapacity(), "Capacity was not assigned correctly");
		assertEquals(areaType, area.getAreaType(), "Area type was not assigned correctly");
		assertTrue(area.getAdministrators().contains(adminUser), "Admin was not added to area");
		assertEquals(description, area.getDescription(), "Description was not assigned correctly");
		assertEquals(link, area.getCalendarLink(), "Calendar link was not assigned correctly");
		assertTrue(area.isCalendarControlled(), "isCalendar controlled was not set to true");
		assertEquals(superArea, area.getSuperArea(), "Super area was not assigned correctly");

		boolean containsFeature = false;
		Iterator<AreaFeature> itFeature = area.getFeatures();

		while (itFeature.hasNext()) {
			AreaFeature feature = itFeature.next();
			if (feature == areaFeature) {
				containsFeature = true;
			}
		}
		assertTrue(containsFeature, "Area feature was not added");
	}

	@Test
	void testThatBuilderThrowsIfNullAdminIsProvided() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 1232, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.administrator(null)
		);
	}

	@Test
	void testThatBuilderThrowsIfNullDescriptionIsProvided() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 1232, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.description(null)
		);
	}

	@Test
	void testThatBuilderThrowsIfNullAreaFeatureIsProvided() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 1232, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.feature(null)
		);
	}

	@Test
	void testThatBuilderThrowsIfNullLinkIsProvided() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 1232, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.calendarLink(null)
		);
	}

	@Test
	void testThatBuilderThrowsIfBlankLinkIsProvided() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 1232, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> builder.calendarLink("")
		);
	}

	@Test
	void testThatBuilderThrowsIfNullSuperAreaIsProvided() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 1232, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.superArea(null)
		);
	}

	@Test
	void testSetFeatures() {
		HashSet<AreaFeature> features = new HashSet<>();
		features.add(areaFeature);
		features.add(areaFeature1);
		Area area;
		try {
			area = new Area.Builder("Test", 124, areaType)
				.administrator(adminUser)
				.features(features)
				.build();
		} catch (Exception e) {
			fail("Failed to create area: " + e.getMessage(), e);
			return;
		}
		Iterator<AreaFeature> it = area.getFeatures();
		boolean contains1 = false;
		boolean contains2 = false;

		while (it.hasNext()) {
			AreaFeature feature = it.next();
			if (feature == areaFeature) {
				contains1 = true;
			}
			if (feature == areaFeature1) {
				contains2 = true;
			}
		}

		assertTrue(contains1);
		assertTrue(contains2);
	}

	@Test
	void testNullFeatures() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 1232, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.features(null),
			"Does not throw when trying to set null features"
		);
	}

	@Test
	void addAdministrators() {
		HashSet<User> administrators = new HashSet<>();
		administrators.add(adminUser);
		administrators.add(nonAdminUser);

		Area area;
		try {
			area = new Area.Builder("Test", 5, areaType)
				.administrators(administrators)
				.build();
		} catch (Exception e) {
			fail("Failed to create area: " + e.getMessage(), e);
			return;
		}

		assertTrue(area.getAdministrators().contains(adminUser), "User was not added to admins");
		assertTrue(area.getAdministrators().contains(nonAdminUser), "User was not added to admins");
		assertEquals(2, area.getAdminCount());
	}

	@Test
	void addNullAdministrators() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 124, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.administrators(null),
			"Added null administrators"
		);
	}

	/* ---- Method tests ---- */

	@Test
	void testGetAdminCountWithSuperArea() {
		Area superArea;
		Area areaWithSuper;
		try {
			superArea = new Area.Builder("Test", 1234, areaType)
				.administrator(adminUser)
				.build();

			areaWithSuper = new Area.Builder("Test", 1234, areaType)
				.administrator(adminUser)
				.administrator(nonAdminUser)
				.superArea(superArea)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas", e);
			return;
		}

		assertEquals(2, areaWithSuper.getAdminCount());

	}

	@Test
	void testRemoveAdmin() {
		Area area;
		try {
			area = new Area.Builder("Test", 1234, areaType)
				.administrator(adminUser)
				.administrator(nonAdminUser)
				.build();
			area.removeAdministrator(nonAdminUser);
		} catch (Exception e) {
			fail(e.getMessage(), e);
			return;
		}

		assertFalse(area.getAdministrators().contains(nonAdminUser));
	}

	@Test
	void testRemoveLastAdmin() {
		Area area;
		try {
			area = new Area.Builder("Test", 1234, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area" + e.getMessage(), e);
			return;
		}
		assertThrows(
			AdminCountException.class,
			() -> area.removeAdministrator(adminUser),
			"Area lets you remove last user"
		);
	}

	@Test
	void testRemoveNullAdmin() {
		Area area;
		try {
			area = new Area.Builder("Test", 1234, areaType)
				.administrator(adminUser)
				.administrator(adminUser2)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> area.removeAdministrator(null),
			"Area lets you remove null admin"
		);
	}

	@Test
	void testIsAdminWithNullUser() {
		Area area;
		try {
			area = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> area.isAdmin(null),
			"Did not throw when provided with null user"
		);
	}

	@Test
	void testAdminFromSuperArea() {
		Area area;
		Area superArea;
		try {
			superArea = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
			area = new Area.Builder("Test", 123, areaType)
				.superArea(superArea)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas" + e.getMessage(), e);
			return;
		}

		assertTrue(area.isAdmin(adminUser));
	}

	@Test
	void testUpdateCapacity() {
		Area area;
		try {
			area = new Area.Builder("Test", 2, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertDoesNotThrow(
			() -> area.updateCapacity(420)
		);
		assertEquals(420, area.getCapacity());
	}

	@Test
	void testUpdateCapacityInvalidNumber() {
		Area area;
		try {
			area = new Area.Builder("Test", 2, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> area.updateCapacity(-3),
			"Does not throw exception when invalid capacity is given"
		);
	}

	@Test
	void testUpdateDescription() {
		String newText = "Testing is fun";
		Area area;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.description("Coding is fun")
				.build();
			area.updateDescription(newText);
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}

		assertEquals(newText, area.getDescription(), "New description is not assigned");
	}

	@Test
	void testUpdateDescriptionNullText() {
		Area area;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.description("Coding is fun")
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> area.updateDescription(null),
			"Does not throw exception when null text is given"
		);
	}

	@Test
	void testAddAreaFeature() {
		Area area;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.description("Coding is fun")
				.build();
			area.addAreaFeature(areaFeature);
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}

		boolean containsFeature = false;
		Iterator<AreaFeature> itFeature = area.getFeatures();

		while (itFeature.hasNext()) {
			AreaFeature feature = itFeature.next();
			if (feature == areaFeature) {
				containsFeature = true;
			}
		}
		assertTrue(containsFeature, "Area feature was not added");
	}

	@Test
	void testAddNullAreaFeature() {
		Area area;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.description("Coding is fun")
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}

		assertThrows(
			IllegalArgumentException.class,
			() -> area.addAreaFeature(null),
			"Area did not throw when adding null feature"
		);
	}

	@Test
	void testAddExistingAreaFeature() {
		Area area;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.feature(areaFeature)
				.description("Coding is fun")
				// ^^ This guy doesn't know what i know
				.build();
		} catch (Exception e) {
			fail();
			return;
		}

		assertDoesNotThrow(
			() -> area.addAreaFeature(areaFeature),
			"Area did throw when adding existing feature"
		);
		int size = 0;
		Iterator<AreaFeature> featuresIterator = area.getFeatures();
		while (featuresIterator.hasNext()) {
			featuresIterator.next();
			size++;
		}

		assertEquals(1, size, "Multiple of same area feature exist");
	}

	@Test
	void testSetSuperAreaWithAreaWithSuper() {
		Area superArea;
		Area areaWithSuper;
		Area newArea;
		try {
			superArea = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			areaWithSuper = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.superArea(superArea)
				.build();
			newArea = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas", e);
			return;
		}
		assertThrows(
			IllegalStateException.class,
			() -> areaWithSuper.setSuperArea(newArea),
			"Area does not throw when setting sub area with existing super area"
		);
	}

	@Test
	void testRemoveSuperAreaWhenAreaHasNoAdminsOfItsOwn() {
		Area area;
		Area superArea;
		try {
			superArea = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			area = new Area.Builder("Test", 123, areaType)
				.superArea(superArea)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas", e);
			return;
		}

		assertThrows(
			AdminCountException.class,
			area::removeSuperArea,
			"Area does not throw when removing super area with only admins"
		);
	}

	@Test
	void testRemoveReservation() {
		Area area;
		final Reservation reservation;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			reservation = new Reservation(
				area,
				adminUser,
				LocalDateTime.now().plusHours(4),
				LocalDateTime.now().plusHours(6),
				"Meeting with mom"
			);
			new Reservation(
					area,
					adminUser,
					LocalDateTime.now().plusHours(7),
					LocalDateTime.now().plusHours(8),
					"Meeting with mom"
			);
			area.removeReservation(reservation);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
			return;
		}
		Iterator<Reservation> iterator = area.getReservations();
		boolean contains = false;
		while (iterator.hasNext() && !contains) {
			if (iterator.next().equals(reservation)) {
				contains = true;
			}
		}
		assertFalse(contains, "Reservation was not removed");
	}

	@Test
	void testIsFreeReturnsTrue() {
		LocalDateTime start = LocalDateTime.now().plusMinutes(30);
		Area area;
		try {
			area = new Area.Builder("Test", 2, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertDoesNotThrow(
			() -> new Reservation(
				area,
				adminUser,
				start,
				start.plusHours(3),
				"This is for testing"
			)
		);
		assertTrue(
			area.isFree(start.minusHours(1)),
			"Area is not free 1 hour before the only reservation"
		);
		assertTrue(
			area.isFree(start.minusHours(4)),
			"Area is not free 1 hour after the only reservation"
		);
	}

	@Test
	void testIsFreeReturnsFalse() {
		LocalDateTime start = LocalDateTime.now().plusMinutes(30);
		LocalDateTime end = start.plusHours(3);
		Area area;
		try {
			area = new Area.Builder("Test", 2, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertDoesNotThrow(
			() -> new Reservation(area, adminUser, start, end, "This is a test")
		);
		assertFalse(area.isFree(start.plusHours(2)));
	}

	@Test
	void testGetReservations() {
		LocalDateTime start = LocalDateTime.now().plusMinutes(30);
		LocalDateTime start2 = start.plusHours(3);
		Area area;
		try {
			area = new Area.Builder("Test", 2, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}

		Reservation reservation = null;
		Reservation reservation2 = null;
		try {
			reservation = new Reservation(
				area,
				adminUser,
				start,
				start.plusHours(2),
				"This is a test");
			reservation2 = new Reservation(
				area,
				adminUser,
				start2,
				start2.plusHours(3),
				"This is a test");
			area.removeReservation(reservation2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Iterator<Reservation> iterator = area.getReservations();
		boolean contains = false;
		boolean contain2 = false;
		while (iterator.hasNext() && !contains && !contain2) {
			Reservation next = iterator.next();
			if (next == reservation) {
				contains = true;
			}
			if (next == reservation2) {
				contain2 = true;
			}
		}
		assertTrue(contains, "Reservation was not added");
		assertFalse(
			contain2,
			"Returns true for non existing reservation"
		);
	}

	@Test
	void testAddingNullReservationThrows() {
		Area area;
		try {
			area = new Area.Builder("Test", 2, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> area.addReservation(null)
		);
	}

	@Test
	void addingReservationForInvalidTimeSpanThrows() {
		LocalDateTime start = LocalDateTime.now().plusMinutes(30);
		Area area;
		try {
			area = new Area.Builder("Test", 2, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		Reservation reservation;
		try {
			reservation = new Reservation(
				area,
				adminUser,
				start.plusHours(1),
				start.plusHours(2),
				"This is a test"
			);
			area.removeReservation(reservation);
		} catch (Exception e) {
			fail("Failed to create reservation " + e.getMessage());
			return;
		}

		assertDoesNotThrow(
			() -> new Reservation(area, adminUser, start, start.plusHours(3), "This is a test")
		);
		assertThrows(
			ReservedException.class,
			() -> area.addReservation(reservation)
		);
	}

	@Test
	void testAddAdmin() {
		Area area;
		try {
			area = new Area.Builder("Test", 2, areaType)
				.administrator(adminUser)
				.build();
			area.addAdministrator(adminUser2);
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}

		assertTrue(area.getAdministrators().contains(adminUser2), "Admin user was not added");
	}

	@Test
	void testAddNullAdmin() {
		Area area;
		try {
			area = new Area.Builder("Test", 2, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> area.addAdministrator(null)
		);
	}

	@Test
	void isFreeBetweenFalseTests() {
		LocalDateTime start = LocalDateTime.now().plusHours(1);
		Area area;
		try {
			area = new Area.Builder("Test", 2, areaType)
				.administrator(adminUser)
				.build();
			new Reservation(area, adminUser, start, start.plusHours(4), "This is for testing");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			assertFalse(
				area.isFreeBetween(start.plusHours(1),
				start.plusHours(3)),
				"Area is free during a reservation"
			);
			assertFalse(
				area.isFreeBetween(start.plusHours(1),
				start.plusHours(5)),
				"Area is free starting in a reservation"
			);
			assertFalse(
				area.isFreeBetween(start.minusMinutes(15),
				start.plusHours(2)),
				"Area is free ending in a reservation"
			);
			assertFalse(
				area.isFreeBetween(start.minusMinutes(15),
				start.plusHours(5)),
				"Area is free encompassing a reservation"
			);
		} catch (Exception e) {
			fail(e.getMessage(), e);
		}
	}

	@Test
	void removeNullReservationTest() {
		LocalDateTime start = LocalDateTime.now().plusMinutes(30);
		LocalDateTime end = start.plusHours(3);
		Area area;
		try {
			area = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area: " + e.getMessage(), e);
			return;
		}

		assertDoesNotThrow(
			() -> new Reservation(area, adminUser, start, end, "More testing")
		);
		assertThrows(
			IllegalArgumentException.class,
			() -> area.removeReservation(null)
		);
	}

	@Test
	void removeReservation() {
		LocalDateTime start = LocalDateTime.now().plusMinutes(30);
		LocalDateTime end = start.plusHours(3);
		Area area;
		try {
			area = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area: " + e.getMessage(), e);
			return;
		}
		assertDoesNotThrow(
			() -> new Reservation(area, adminUser, start, end, "More testing")
		);
		Reservation reservation2;
		try {
			reservation2 = new Reservation(area, adminUser2, start, end, "More testing");
			area.removeReservation(reservation2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
			return;
		}

		Iterator<Reservation> iterator = area.getReservations();
		boolean contains = false;
		while (iterator.hasNext() && !contains) {
			if (iterator.next().equals(reservation2)) {
				contains = true;
			}
		}
		assertFalse(contains);
	}

	@Test
	void removeSuperArea() {
		Area area;
		Area superArea;
		try {
			superArea = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
			area = new Area.Builder("Name", 12, areaType)
				.superArea(superArea)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas: " + e.getMessage(), e);
			return;
		}
		assertDoesNotThrow(
			area::removeSuperArea
		);
		assertNull(area.getSuperArea());
	}

	@Test
	void addSuperArea() {
		Area area;
		Area superArea;
		try {
			superArea = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
			area = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas: " + e.getMessage(), e);
			return;
		}
		assertDoesNotThrow(
			() -> area.setSuperArea(superArea)
		);
		assertEquals(superArea, area.getSuperArea());
	}

	@Test
	void addNullSuperArea() {
		Area area;
		try {
			area = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> area.setSuperArea(null)
		);
	}

	@Test
	void addSuperAreaToBeSuperOfItself() {
		Area area;
		Area superArea;
		Area superArea2;
		try {
			superArea2 = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
			superArea = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.superArea(superArea2)
				.build();
			area = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.superArea(superArea)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> superArea2.setSuperArea(area)
		);
	}

	@Test
	void testIsFreeBetweenNullArgs() {
		Area area;
		try {
			area = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas: " + e.getMessage(), e);
			return;
		}
		LocalDateTime time = LocalDateTime.now().plusDays(1);
		assertThrows(
			IllegalArgumentException.class,
			() -> area.isFreeBetween(null, time)
		);
		assertThrows(
			IllegalArgumentException.class,
			() -> area.isFreeBetween(time, null)
		);
	}

	@Test
	void testIsFreeNullArgs() {
		Area area;
		try {
			area = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> area.isFree(null)
		);
	}

	@Test
	void testNotAbleToAssignSelfAsSuperArea() {
		Area area;
		try {
			area = new Area.Builder("Name", 12, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> area.setSuperArea(area)
		);
	}
}
