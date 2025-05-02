package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Iterator;
import no.ntnu.idata2900.group3.chairspace.exceptions.AdminCountException;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
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
			nonAdminUser = new User("Stig " + "Arne", "Arne@email.no", "TestUser");
			adminUser = new User("Jon " + "Kode", "Jon@Kode.no", "TestUser");
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
		Area.Builder areaBuilder = new Area.Builder(null, 1232, areaType);
		areaBuilder.administrator(adminUser);
		assertThrows(
			IllegalArgumentException.class,
			areaBuilder::build
		);
	}

	@Test
	void testThatBuilderThrowsIfBlank() {
		Area.Builder areaBuilder = new Area.Builder("", 1232, areaType);
		areaBuilder.administrator(adminUser);
		assertThrows(
			InvalidArgumentCheckedException.class,
			areaBuilder::build
		);
	}

	@Test
	void testThatBuilderThrowsIfNegativeCapacity() {
		Area.Builder areaBuilder = new Area.Builder("Test", -3, areaType);
		areaBuilder.administrator(adminUser);
		assertThrows(
			InvalidArgumentCheckedException.class,
			areaBuilder::build
		);
	}

	@Test
	void testThatBuilderThrowsIfNoAreaType() {
		Area.Builder areaBuilder = new Area.Builder("Test", 1232, null);
		areaBuilder.administrator(adminUser);
		assertThrows(
			IllegalArgumentException.class,
			areaBuilder::build
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
		Iterator<AreaFeature> itFeature = area.getFeatures().iterator();

		while (itFeature.hasNext() && !containsFeature) {
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
		builder = new Area.Builder("Test", 1232, areaType);
		builder.administrator(null);
		assertThrows(
			IllegalArgumentException.class,
			builder::build
		);
	}

	@Test
	void testThatBuilderThrowsIfNullAreaFeatureIsProvided() {
		Area.Builder builder;
		builder = new Area.Builder("Test", 1232, areaType);
		builder.feature(null);
		builder.administrator(adminUser);
		assertThrows(
			IllegalArgumentException.class,
			builder::build
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
		Iterator<AreaFeature> it = area.getFeatures().iterator();
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
}
