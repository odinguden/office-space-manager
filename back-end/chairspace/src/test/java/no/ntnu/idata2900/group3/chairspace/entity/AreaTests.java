package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.translation.messages_bg;

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
		nonAdminUser = new User.Builder("Stig", "Arne").build();
		adminUser = new User.Builder("Jon", "Kode").build();
		adminUser2 = new User.Builder("Jon", "Kode").build();
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

		Area area = new Area.Builder(name, capacity, areaType)
			.administrator(adminUser)
			.description(description)
			.feature(areaFeature)
			.calendarLink(link)
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
		assertTrue(area.isCalendarControlled(), "isCalendar controlled was not set to true");
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
	void testThatBuilderThrowsIfBlankLinkIsProvided() {
		Area.Builder builder = new Area.Builder("Test", 1232, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.calendarLink("")
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

	@Test
	void testSetFeatures() {
		HashSet<AreaFeature> features = new HashSet<>();
		features.add(areaFeature);
		features.add(areaFeature1);

		Area area = new Area.Builder("Test", 124, areaType)
			.administrator(adminUser)
			.features(features)
			.build();
		assertTrue(area.getFeatures().contains(areaFeature));
		assertTrue(area.getFeatures().contains(areaFeature1));

	}

	@Test
	void testNullFeatures() {
		Area.Builder builder = new Area.Builder("Test", 123, areaType);
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

		Area area = new Area.Builder("Test", 5, areaType)
			.administrators(administrators)
			.build();

		assertTrue(area.getAdministrators().contains(adminUser), "User was not added to admins");
		assertTrue(area.getAdministrators().contains(nonAdminUser), "User was not added to admins");
		assertEquals(2, area.getAdminCount());
	}

	@Test
	void addNullAdministrators() {
		Area.Builder builder = new Area.Builder("Test", 124, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.administrators(null),
			"Added null administrators"
		);
	}

	@Test
	void addSubAreas() {
		Area sub = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.build();
		Area sub1 = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.build();
		HashSet<Area> subs = new HashSet<>();
		subs.add(sub1);
		subs.add(sub);

		Area area = new Area.Builder("test", 432, areaType)
			.administrator(adminUser)
			.subAreas(subs)
			.build();

		assertTrue(area.getSubAreas().contains(sub1));
		assertTrue(area.getSubAreas().contains(sub));
	}

	@Test
	void addSubAreaWithSuperArea() {
		Area superArea = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.build();

		Area subWithSuper = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.superArea(superArea)
			.build();


		Area.Builder builder = new Area.Builder("Test", 124, areaType);

		assertThrows(
			IllegalStateException.class,
			() -> builder.subArea(subWithSuper),
			"Area lets you add sub area with existing super area"
		);
	}

	@Test
	void addNullSubArea() {
		Area.Builder builder = new Area.Builder("This is a area", 123, areaType);
		assertThrows(
			IllegalArgumentException.class,
			() -> builder.subArea(null),
			"Area lets you add null area"
		);
	}

	@Test
	void addNullSubAreas() {
		Area.Builder builder = new Area.Builder("This is a area", 123, areaType);
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
		Area superArea = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.build();

		Area areaWithSuper = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.administrator(nonAdminUser)
			.superArea(superArea)
			.build();

		assertEquals(2, areaWithSuper.getAdminCount());

	}

	@Test
	void testRemoveAdmin() {
		Area area = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.administrator(nonAdminUser)
			.build();

		area.removeAdministrator(nonAdminUser, adminUser);

		assertFalse(area.getAdministrators().contains(nonAdminUser));
	}

	@Test
	void testRemoveLastAdmin() {
		Area area = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.build();
		assertThrows(
			IllegalStateException.class,
			() -> area.removeAdministrator(adminUser, adminUser),
			"Area lets you remove last user"
		);
	}

	@Test
	void testRemoveAdminWithNonAdminUser() {
		Area area = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.administrator(adminUser2)
			.build();
		assertThrows(
			IllegalStateException.class,
			() -> area.removeAdministrator(adminUser, nonAdminUser),
			"Area lets you remove user without being admin"
		);
	}

	@Test
	void testRemoveAdminWithNullUser() {
		Area area = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.administrator(adminUser2)
			.build();
		assertThrows(
			IllegalArgumentException.class,
			() -> area.removeAdministrator(adminUser, null),
			"Area lets you remove user with null admin"
		);
	}

	@Test
	void testRemoveNullAdmin() {
		Area area = new Area.Builder("Test", 1234, areaType)
			.administrator(adminUser)
			.administrator(adminUser2)
			.build();
		assertThrows(
			IllegalArgumentException.class,
			() -> area.removeAdministrator(null, adminUser),
			"Area lets you remove null admin"
		);
	}

	@Test
	void testIsAdminWithNullUser() {
		Area area = new Area.Builder("Name", 12, areaType)
			.administrator(adminUser)
			.build();
		assertThrows(
			IllegalArgumentException.class,
			() -> area.isAdmin(null),
			"Did not throw when provided with null user"
		);
	}

	@Test
	void testAdminFromSuperArea() {
		Area superArea = new Area.Builder("Name", 12, areaType)
			.administrator(adminUser)
			.build();

		Area area = new Area.Builder("Test", 123, areaType)
			.superArea(superArea)
			.build();

		assertTrue(area.isAdmin(adminUser));
	}

	@Test
	void testAddSubArea() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Area sub = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		area.addSubArea(sub, adminUser);
		assertTrue(area.getSubAreas().contains(sub), "Sub area was not added");
	}

	@Test
	void testAddNullSubArea() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();

		assertThrows(
			IllegalArgumentException.class,
			() -> area.addSubArea(null, adminUser),
			"Adding null sub area does not throw exception"
		);
	}

	@Test
	void testAddSubAreaWithNullUser() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Area sub = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		assertThrows(
			IllegalArgumentException.class,
			() -> area.addSubArea(sub, null),
			"Adding sub area with no user not throw exception"
		);
	}

	@Test
	void testAddSubAreaWithNonAdminUser() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Area sub = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		assertThrows(
			IllegalStateException.class,
			() -> area.addSubArea(sub, nonAdminUser),
			"Adding sub area with non admin user not throw exception"
		);
	}

	@Test
	void testAddSubAreaWithSuperArea() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Area superArea = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Area subWithSuper = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.superArea(superArea)
			.build();
		assertThrows(
			IllegalStateException.class,
			() -> area.addSubArea(subWithSuper, adminUser),
			"Adding sub area with superArea does not throw exception"
		);
	}

	@Test
	void testUpdateCapacity() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		area.updateCapacity(420, adminUser);
		assertEquals(420, area.getCapacity());
	}

	@Test
	void testUpdateCapacityInvalidNumber() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		assertThrows(
			IllegalArgumentException.class,
			() -> area.updateCapacity(-3, adminUser),
			"Does not throw exception when invalid capacity is given"
		);
	}

	@Test
	void testUpdateCapacityNullUser() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		assertThrows(
			IllegalArgumentException.class,
			() -> area.updateCapacity(3, null),
			"Does not throw exception when invalid capacity is given"
		);
	}

	@Test
	void testUpdateCapacityNonAdminUser() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		assertThrows(
			IllegalStateException.class,
			() -> area.updateCapacity(4, nonAdminUser),
			"Does not throw exception when invalid capacity is given"
		);
	}

	@Test
	void testUpdateDescription() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.description("Coding is fun")
			.build();
		String newText = "Testing is fun";

		area.updateDescription(newText, adminUser);
		assertEquals(newText, area.getDescription(), "New description is not assigned");
	}

	@Test
	void testUpdateDescriptionNonAdminUser() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.description("Coding is fun")
			.build();
		String newText = "Testing is fun";
		assertThrows(
			IllegalStateException.class,
			() -> area.updateDescription(newText, nonAdminUser),
			"Does not throw exception when non admin user is given"
		);
	}

	@Test
	void testUpdateDescriptionNullUser() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.description("Coding is fun")
			.build();
		String newText = "Testing is fun";
		assertThrows(
			IllegalArgumentException.class,
			() -> area.updateDescription(newText, null),
			"Does not throw exception when null user is given"
		);
	}

	@Test
	void testUpdateDescriptionNullText() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.description("Coding is fun")
			.build();
		assertThrows(
			IllegalArgumentException.class,
			() -> area.updateDescription(null, nonAdminUser),
			"Does not throw exception when null text is given"
		);
	}

	@Test
	void testUpdateDescriptionBlankTest() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.description("Coding is fun")
			.build();
		assertThrows(
			IllegalArgumentException.class,
			() -> area.updateDescription("", nonAdminUser),
			"Does not throw exception when blank test is given"
		);
	}

	@Test
	void testAddAreaFeature() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.description("Coding is fun")
			.build();

		area.addAreaFeature(areaFeature, adminUser);

		assertTrue(area.getFeatures().contains(areaFeature), "AreaFeature was not added");
	}

	@Test
	void testAddAreaFeatureWithNonAdminUser() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.description("Coding is fun")
			.build();

		assertThrows(
			IllegalStateException.class,
			() -> area.addAreaFeature(areaFeature, nonAdminUser),
			"Area did not throw when feature was added by non admin user"
		);
	}

	@Test
	void testAddAreaFeatureWithNullUser() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.description("Coding is fun")
			.build();

		assertThrows(
			IllegalArgumentException.class,
			() -> area.addAreaFeature(areaFeature, null),
			"Area did not throw when feature was added by null user"
		);
	}

	@Test
	void testAddNullAreaFeature() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.description("Coding is fun")
			.build();

		assertThrows(
			IllegalArgumentException.class,
			() -> area.addAreaFeature(null, adminUser),
			"Area did not throw when adding null feature"
		);
	}

	@Test
	void testAddExistingAreaFeature() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.feature(areaFeature)
			.description("Coding is fun")
			.build();

		assertDoesNotThrow(
			() -> area.addAreaFeature(areaFeature, adminUser),
			"Area did throw when adding existing feature"
		);
		assertEquals(1, area.getFeatures().size(), "Multiple of same area feature exist");
	}

	@Test
	void testReplaceSuperArea() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Area superArea = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		area.replaceSuperArea(superArea, adminUser);
		assertEquals(superArea, area.getSuperArea());
	}

	@Test
	void testReplaceSuperAreaWithNonAdminUser() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Area superArea = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		assertThrows(
			IllegalStateException.class,
			() -> area.replaceSuperArea(superArea, nonAdminUser),
			"Area did not throw when trying to set superArea using non admin user"
		);
	}

	@Test
	void testReplaceSuperAreaWithNull() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.description("Coding is fun")
			.build();
		assertThrows(
			IllegalArgumentException.class,
			() -> area.replaceSuperArea(null, adminUser),
			"Area did not throw when trying to set superArea to null"
		);
	}

	@Test
	void testSetSuperAreaWithAreaWithSuper() {
		Area superArea = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Area areaWithSuper = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.superArea(superArea)
			.build();
		Area newArea = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		assertThrows(
			IllegalStateException.class,
			() -> areaWithSuper.setSuperArea(newArea, adminUser),
			"Area does not throw when setting sub area with existing super area"
		);
	}

	@Test
	void testSetSuperAreaWithNonAdminUser() {
		Area superArea = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();

		assertThrows(
			IllegalStateException.class,
			() -> area.setSuperArea(superArea, nonAdminUser),
			"Area does not throw when setting sub area with non admin user"
		);
	}

	@Test
	void testRemoveSuperAreaWhenAreaHasNoAdminsOfItsOwn() {
		Area superArea = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Area area = new Area.Builder("Test", 123, areaType)
			.superArea(superArea)
			.build();

		assertThrows(
			IllegalStateException.class,
			() -> area.removeSuperArea(adminUser),
			"Area does not throw when removing super area with only admins"
		);
	}

	@Test
	void testRemoveReservation() {
		Area area = new Area.Builder("Test", 123, areaType)
			.administrator(adminUser)
			.build();
		Reservation reservation = new Reservation(
			area,
			adminUser,
			LocalDateTime.now().plusHours(4),
			LocalDateTime.now().plusHours(6),
			"Meeting with mom"
		);

		Reservation reservation2 = new Reservation(
			area,
			adminUser,
			LocalDateTime.now().plusHours(4),
			LocalDateTime.now().plusHours(6),
			"Meeting with mom"
		);
	}
}
