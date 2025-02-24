package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
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
		nonAdminUser = new User.Builder("Stig", "Arne").build();
		adminUser = new User.Builder("Jon", "Kode").build();
		areaFeature = new AreaFeature("F1", "Feature 1");
		areaFeature1 = new AreaFeature("F2", "Confusing i know");
		areaType = new AreaType("T1", "Type 1");
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
		Area.Builder builder = new Area.Builder("Test", 1232, areaType);
		assertThrows(
			IllegalStateException.class,
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
		Area sub = new Area.Builder(name, capacity, areaType)
			.administrator(adminUser)
			.build();
		Area superArea = new Area.Builder(name, capacity, areaType)
			.administrator(adminUser)
			.build();
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusHours(4);

		Reservation reservation = new Reservation(superArea, adminUser, start, end, "BRuh");


		Area area = new Area.Builder(name, capacity, areaType)
			.administrator(adminUser)
			.description(description)
			.feature(areaFeature)
			.calendarLink(link)
			.reservation(reservation)
			.subArea(sub)
			.superArea(superArea)
			.build();

		assertEquals(name, area.getName(), "Name was not assigned correctly");
		assertEquals(capacity, area.getCapacity(), "Capacity was not assigned correctly");
		assertEquals(areaType, area.getAreaType(), "Area type was not assigned correctly");
		assertTrue(area.getAdministrators().contains(adminUser), "Admin was not added to area");
		assertEquals(description, area.getDescription(), "Description was not assigned correctly");
		assertTrue(area.getFeatures().contains(areaFeature), "Area feature was not added");
		assertEquals(link, area.getCalendarLink(), "Calendar link was not assigned correctly");
		assertTrue(area.getReservations().contains(reservation), "Reservation was not added");
		assertTrue(area.getSubAreas().contains(sub), "Sub area was not added");
		assertEquals(superArea, area.getSuperArea(), "Super area was not assigned correctly");
	}

	@Test
	void testThatBuilderThrowsIfNullAdminIsProvided() {
		Area.Builder builder = new Area.Builder("Test", 1232, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.administrator(null)
		);
	}

	@Test
	void testThatBuilderThrowsIfBlankDescriptionIsProvided() {
		Area.Builder builder = new Area.Builder("Test", 1232, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.description("")
		);
	}

	@Test
	void testThatBuilderThrowsIfNullDescriptionIsProvided() {
		Area.Builder builder = new Area.Builder("Test", 1232, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.description(null)
		);
	}

	@Test
	void testThatBuilderThrowsIfNullAreaFeatureIsProvided() {
		Area.Builder builder = new Area.Builder("Test", 1232, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.feature(null)
		);
	}

	@Test
	void testThatBuilderThrowsIfNullLinkIsProvided() {
		Area.Builder builder = new Area.Builder("Test", 1232, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.calendarLink(null)
		);
	}

	@Test
	void testThatBuilderThrowsIfNullReservationIsProvided() {
		Area.Builder builder = new Area.Builder("Test", 1232, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.reservation(null)
		);
	}

	@Test
	void testThatBuilderThrowsIfNullSubAreaIsProvided() {
		Area.Builder builder = new Area.Builder("Test", 1232, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.subArea(null)
		);
	}

	@Test
	void testThatBuilderThrowsIfNullSuperAreaIsProvided() {
		Area.Builder builder = new Area.Builder("Test", 1232, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.superArea(null)
		);
	}

	//16:31, I now have positive and negative tests for the builder
	// I have also considered removing reservations from the builder, as reservations should always
	// be created after the area.

	/* ---- Method tests ---- */


}
