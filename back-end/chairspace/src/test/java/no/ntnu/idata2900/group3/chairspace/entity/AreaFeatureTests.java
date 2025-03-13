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
 * Tests for area feature entity class
 *
 * @see AreaFeature
 * @author Odin Lyngsgård
 * @version 0.1
 * @since 0.1
 */
class AreaFeatureTests {

	private AreaFeature areaFeature;
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
		areaFeature = new AreaFeature("Test Feature", "Test Description");
	}

	/* ---- Test Constructor ---- */

	@DisplayName("Test that name is assigned")
	@Test
	void testName() {
		assertEquals("Test Feature", areaFeature.getName(), "Name is not assigned correctly");
	}

	@DisplayName("Test that description is assigned")
	@Test
	void testDescription() {
		assertEquals(
			"Test Description",
			areaFeature.getDescription(),
			"Description is not assigned correctly"
		);
	}

	@DisplayName("Test that constructor throws exception when name is null")
	@Test
	void testNullName() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new AreaFeature(null, "Test Description"),
			"Does not throw exception when name is null"
		);
	}

	@DisplayName("Test that constructor throws exception when name is blank")
	@Test
	void testBlankName() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new AreaFeature("","Test Description"),
			"Does not throw exception when name is blank"
		);
	}

	@DisplayName("Test that constructor throws exception when description is null")
	@Test
	void testNullDescription() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new AreaType("Test Type", null),
			"Does not throw exception when description is null"
		);
	}

	/* ---- Test Methods ---- */

	@DisplayName("Test that setDescription works")
	@Test
	void testUpdateDescription() {
		areaFeature.updateDescription("New Description");
		assertEquals("New Description", areaFeature.getDescription());
	}

	@DisplayName("Test that setDescription throws exception when null")
	@Test
	void testUpdateNullDescription() {
		assertThrows(
			IllegalArgumentException.class, () -> areaFeature.updateDescription(null),
			"Does not throw exception when description is null"
		);
	}

	@DisplayName("Test that setDescription throws exception when blank")
	@Test
	void testUpdateBlankDescription() {
		assertThrows(
			IllegalArgumentException.class, () -> areaFeature.updateDescription(""),
			"Does not throw exception when description is blank"
		);
	}

	@Test
	void testAddArea() {
		areaFeature.addArea(area);
		areaFeature.addArea(area2);
		assertTrue(areaFeature.getAreas().contains(area), "Area type does not contain area");
		assertTrue(areaFeature.getAreas().contains(area2), "Area type does not contain area2");
	}


	@Test
	void testAddAreas() {
		Set<Area> areas = new HashSet<>();
		areas.add(area);
		areas.add(area2);
		areaFeature.addAreas(areas);
		assertTrue(areaFeature.getAreas().contains(area), "Area type does not contain area");
		assertTrue(areaFeature.getAreas().contains(area2), "Area type does not contain area2");
	}

	@Test
	void testAddNullAreas() {
		assertThrows(
			IllegalArgumentException.class,
			() -> areaFeature.addAreas(null),
			"Area feature does not throw when trying to add null areas"
			);
	}

	@Test
	void testAddNullAreaThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> areaFeature.addArea(null)
		);
	}

	@Test
	void testRemoveArea() {
		areaFeature.addArea(area);
		areaFeature.addArea(area2);
		areaFeature.removeArea(area);
		assertFalse(areaFeature.getAreas().contains(area));
		areaFeature.removeArea(area2);
		assertFalse(areaFeature.getAreas().contains(area2));
	}

	@Test
	void testRemoveAreaThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> areaFeature.removeArea(null)
		);
	}
}