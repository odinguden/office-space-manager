package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
			IllegalArgumentException.class, () -> new AreaType(null, "Test Description"),
			"Does not throw exception when name is null"
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

	/* ---- Test Methods ---- */

	@DisplayName("Test that setDescription works")
	@Test
	void testSetDescription() {
		areaFeature.setDescription("New Description");
		assertEquals("New Description", areaFeature.getDescription());
	}

	@DisplayName("Test that setDescription throws exception when null")
	@Test
	void testSetNullDescription() {
		assertThrows(
			IllegalArgumentException.class, () -> areaFeature.setDescription(null),
			"Does not throw exception when description is null"
		);
	}

	@DisplayName("❓")
	@Test
	void testAddArea() {
		AreaType testType = new AreaType("Test Type", "Test Descripton");
		Area area = new Area.Builder("Test Area", 123, testType).build();
		Area area2 = new Area.Builder("Test Area 2", 23, testType).build();
		areaFeature.addArea(area);
		areaFeature.addArea(area2);
		assertTrue(areaFeature.getAreas().contains(area), "Area type does not contain area");
		assertTrue(areaFeature.getAreas().contains(area2), "Area type does not contain area2");
	}

	@DisplayName("❓")
	@Test
	void testAddNullAreaThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> areaFeature.addArea(null)
		);
	}

	@DisplayName("❓")
	@Test
	void testRemoveArea() {
		AreaType testType = new AreaType("Test Type", "Test Descripton");
		Area area = new Area.Builder("Test Area", 123, testType).build();
		Area area2 = new Area.Builder("Test Area 2", 23, testType).build();
		areaFeature.addArea(area);
		areaFeature.addArea(area2);
		areaFeature.removeArea(area);
		assertFalse(areaFeature.getAreas().contains(area));
		areaFeature.removeArea(area2);
		assertFalse(areaFeature.getAreas().contains(area2));
	}

	@DisplayName("❓")
	@Test
	void testRemoveAreaThrows() {
		assertThrows(
			IllegalArgumentException.class,
			() -> areaFeature.removeArea(null)
		);
	}
}