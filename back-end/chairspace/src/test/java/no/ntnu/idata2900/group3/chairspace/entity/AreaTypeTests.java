package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the AreaType class.
 *
 * @see AreaType
 * @author Odin Lyngsgård
 * @version 0.1
 * @since 0.1
 */
class AreaTypeTests {

	private static AreaType areaType;
	private static Area area;
	private static Area area2;

	@BeforeAll
	static void setUpAreas() {
		AreaType testType = new AreaType("Test Type", "Test Descripton");
		User user = new User.Builder("Argh", "REah").build();
		area = new Area.Builder("Test Area", 123, testType).administrator(user).build();
		area2 = new Area.Builder("Test Area 2", 23, testType).administrator(user).build();
	}

	@BeforeEach
	void setUp() {
		areaType = new AreaType("Test Type", "Test Description");
	}

	/* ---- Test Constructor ---- */

	@DisplayName("Test that name is assigned")
	@Test
	void testName() {
		assertEquals("Test Type", areaType.getName(), "Name is not assigned correctly");
	}

	@DisplayName("Test that description is assigned")
	@Test
	void testDescription() {
		assertEquals(
			"Test Description",
			areaType.getDescription(),
			"Description is not assigned correctly"
		);
	}

	@DisplayName("Test that constructor throws exception when name is null")
	@Test
	void testNullName() {
		assertThrows(
			IllegalArgumentException.class, () -> new AreaType(null, "Test Description"),
			"Does not throw exception when name is null"
		);
	}

	@Test
	void testBlankName() {
		assertThrows(
			IllegalArgumentException.class, () -> new AreaType("", "Test Description"),
			"Does not throw exception when name is blank"
		);
	}

	@DisplayName("Test that constructor throws exception when description is null")
	@Test
	void testNullDescription() {
		assertThrows(
			IllegalArgumentException.class, () -> new AreaType("Test Type", null),
			"Does not throw exception when description is null"
		);
	}

	@DisplayName("Test that constructor throws exception when description is blank")
	@Test
	void testBlankDescription() {
		assertThrows(
			IllegalArgumentException.class, () -> new AreaType("Test Type", ""),
			"Does not throw exception when description is blank"
		);
	}

	/* ---- Test Methods ---- */

	@DisplayName("Test that setDescription works")
	@Test
	void testSetDescription() {
		areaType.setDescription("New Description");
		assertEquals("New Description", areaType.getDescription());
	}

	@DisplayName("Test that setDescription throws exception when null")
	@Test
	void testSetNullDescription() {
		assertThrows(
			IllegalArgumentException.class, () -> areaType.setDescription(null),
			"Does not throw exception when description is null"
		);
	}

	@Test
	void testAddArea() {
		areaType.addArea(area);
		areaType.addArea(area2);
		assertTrue(areaType.getAreas().contains(area), "Area type does not contain area");
		assertTrue(areaType.getAreas().contains(area2), "Area type does not contain area2");
	}

	@Test
	void testAddNullAreaThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> areaType.addArea(null)
		);
	}

	@Test
	void addAreas() {
		Set<Area> areas = new HashSet<>();
		areas.add(area);
		areas.add(area2);
		areaType.addAreas(areas);
		assertTrue(areaType.getAreas().contains(area), "Area type does not contain area");
		assertTrue(areaType.getAreas().contains(area2), "Area type does not contain area2");
	}

	@Test
	void addNullAreas() {
		assertThrows(
			IllegalArgumentException.class,
			() -> areaType.addAreas(null),
			"AreaType does not throw when trying to add null areas"
		);
	}

	@Test
	void testRemoveArea() {
		areaType.addArea(area);
		areaType.addArea(area2);
		areaType.removeArea(area);
		assertFalse(areaType.getAreas().contains(area));
		areaType.removeArea(area2);
		assertFalse(areaType.getAreas().contains(area2));
	}

	@Test
	void testRemoveAreaThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> areaType.removeArea(null)
		);
	}
}
