package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
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
			nonAdminUser = new User.Builder("Stig", "Arne").build();
			adminUser = new User.Builder("Jon", "Kode").build();
			adminUser2 = new User.Builder("Jon", "Kode").build();
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
			IllegalStateException.class,
			builder::build
		);
	}

	@Test
	void testThatBuilderThrowsIfNoName() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new Area.Builder(null, 1232, areaType)
		);
	}

	@Test
	void testThatBuilderThrowsIfBlank() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new Area.Builder("", 1232, areaType)
		);
	}

	@Test
	void testThatBuilderThrowsIfNoCapacity() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new Area.Builder("Test", 0, areaType)
		);
	}

	@Test
	void testThatBuilderThrowsIfNegativeCapacity() {
		assertThrows(
			IllegalArgumentException.class,
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
		Area sub;
		Area superArea;
		Area area;
		try {
			sub = new Area.Builder(name, capacity, areaType)
				.administrator(adminUser)
				.build();
			superArea = new Area.Builder(name, capacity, areaType)
				.administrator(adminUser)
				.build();
			area = new Area.Builder(name, capacity, areaType)
				.administrator(adminUser)
				.description(description)
				.feature(areaFeature)
				.calendarLink(link)
				.subArea(sub)
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
		assertTrue(area.getFeatures().contains(areaFeature), "Area feature was not added");
		assertEquals(link, area.getCalendarLink(), "Calendar link was not assigned correctly");
		assertTrue(area.isCalendarControlled(), "isCalendar controlled was not set to true");
		assertTrue(area.getSubAreas().contains(sub), "Sub area was not added");
		assertEquals(superArea, area.getSuperArea(), "Super area was not assigned correctly");
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
	void testThatBuilderThrowsIfBlankDescriptionIsProvided() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 1232, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.description("")
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
			IllegalArgumentException.class,
			() -> builder.calendarLink("")
		);
	}

	@Test
	void testThatBuilderThrowsIfNullSubAreaIsProvided() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("Test", 1232, areaType);
		} catch (Exception e) {
			fail("Failed to create builder: " + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.subArea(null)
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
		assertTrue(area.getFeatures().contains(areaFeature));
		assertTrue(area.getFeatures().contains(areaFeature1));

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

	@Test
	void addSubAreas() {
		Area sub;
		Area sub1;
		try {
			sub = new Area.Builder("Test", 1234, areaType)
				.administrator(adminUser)
				.build();
			sub1 = new Area.Builder("Test", 1234, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas: " + e.getMessage(), e);
			return;
		}
		HashSet<Area> subs = new HashSet<>();
		subs.add(sub1);
		subs.add(sub);

		Area area;
		try {
			area = new Area.Builder("test", 432, areaType)
				.administrator(adminUser)
				.subAreas(subs)
				.build();
		} catch (Exception e) {
			fail("Failed to create area" + e.getMessage(), e);
			return;
		}

		assertTrue(area.getSubAreas().contains(sub1));
		assertTrue(area.getSubAreas().contains(sub));
	}

	@Test
	void addSubAreaWithSuperArea() {
		Area.Builder builder;
		Area superArea;
		Area subWithSuper;

		try {
			superArea = new Area.Builder("Test", 1234, areaType)
				.administrator(adminUser)
				.build();

			subWithSuper = new Area.Builder("Test", 1234, areaType)
				.administrator(adminUser)
				.superArea(superArea)
				.build();

			builder = new Area.Builder("Test", 124, areaType);

		} catch (Exception e) {
			fail();
			return;
		}

		assertThrows(
			IllegalStateException.class,
			() -> builder.subArea(subWithSuper),
			"Area lets you add sub area with existing super area"
		);
	}

	@Test
	void addNullSubArea() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("This is a area", 123, areaType);
		} catch (Exception e) {
			fail("Failed to create builder" + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.subArea(null),
			"Area lets you add null area"
		);
	}

	@Test
	void addNullSubAreas() {
		Area.Builder builder;
		try {
			builder = new Area.Builder("This is a area", 123, areaType);
		} catch (Exception e) {
			fail("Failed to create builder" + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.subAreas(null),
			"Area lets you add null area"
		);
	}



	//16:31, I now have positive and negative tests for the builder
	// I have also considered removing reservations from the builder, as reservations should always
	// be created after the area.

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
			IllegalStateException.class,
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
	void testAddSubArea() {
		Area area;
		Area sub;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			sub = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			area.addSubArea(sub, adminUser);
		} catch (Exception e) {
			fail(e.getMessage(), e);
			return;
		}
		assertTrue(area.getSubAreas().contains(sub), "Sub area was not added");
	}

	@Test
	void testAddNullSubArea() {
		Area area;

		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}

		assertThrows(
			IllegalArgumentException.class,
			() -> area.addSubArea(null, adminUser),
			"Adding null sub area does not throw exception"
		);
	}

	@Test
	void testAddSubAreaWithNullUser() {
		Area area;
		Area sub;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			sub = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas", e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> area.addSubArea(sub, null),
			"Adding sub area with no user not throw exception"
		);
	}

	@Test
	void testAddSubAreaWithNonAdminUser() {
		Area area;
		Area sub;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			sub = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertThrows(
			IllegalStateException.class,
			() -> area.addSubArea(sub, nonAdminUser),
			"Adding sub area with non admin user not throw exception"
		);
	}

	@Test
	void testAddSubAreaWithSuperArea() {
		Area area;
		Area superArea;
		Area subWithSuper;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			superArea = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			subWithSuper = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.superArea(superArea)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas", e);
			return;
		}
		assertThrows(
			IllegalStateException.class,
			() -> area.addSubArea(subWithSuper, adminUser),
			"Adding sub area with superArea does not throw exception"
		);
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
	void testUpdateDescriptionBlankTest() {
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
			() -> area.updateDescription(""),
			"Does not throw exception when blank test is given"
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

		assertTrue(area.getFeatures().contains(areaFeature), "AreaFeature was not added");
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
		assertEquals(1, area.getFeatures().size(), "Multiple of same area feature exist");
	}

	@Test
	void testReplaceSuperArea() {
		Area area;
		Area superArea;
		try {
			area = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			superArea = new Area.Builder("Test", 123, areaType)
				.administrator(adminUser)
				.build();
			area.replaceSuperArea(superArea);
		} catch (Exception e) {
			fail("Failed to create area", e);
			return;
		}
		assertEquals(superArea, area.getSuperArea());
	}

	@Test
	void testReplaceSuperAreaWithNull() {
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
			() -> area.replaceSuperArea(null),
			"Area did not throw when trying to set superArea to null"
		);
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
			IllegalStateException.class,
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
	void testRemoveSubArea() {
		Area supArea;
		Area subArea;
		try {
			supArea = new Area.Builder("Test", 23, areaType)
				.administrator(adminUser)
				.build();
			subArea = new Area.Builder("Test", 23, areaType)
				.administrator(adminUser)
				.superArea(supArea)
				.build();
			supArea.removeSubArea(subArea);
		} catch (Exception e) {
			fail();
			return;
		}
		assertTrue(supArea.getSubAreas().isEmpty());
	}

	@Test
	void testRemoveNullSubArea() {
		Area supArea;
		try {
			supArea = new Area.Builder("Test", 23, areaType)
				.administrator(adminUser)
				.build();
			new Area.Builder("Test", 23, areaType)
				.administrator(adminUser)
				.superArea(supArea)
				.build();
		} catch (Exception e) {
			fail("Failed to create areas:" + e.getMessage(), e);
			return;
		}
		assertThrows(
			IllegalArgumentException.class,
			() -> supArea.removeSubArea(null)
		);
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
	void removeReservationNotAdmin() {
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
	void removeReservationNotOwner() {
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
		} catch (Exception e) {
			fail("Failed to create reservation" + e.getMessage());
			return;
		}
		try {
			area.removeReservation(reservation2);
		} catch (Exception e) {
			fail("Failed to remove reservation: " + e.getMessage(), e);
			return;
		}

		boolean contains = false;
		Iterator<Reservation> iterator = area.getReservations();
		while (iterator.hasNext() && !contains) {
			if (iterator.next().equals(reservation2)) {
				contains = true;
			}
		}
		assertFalse(contains);
	}
}
