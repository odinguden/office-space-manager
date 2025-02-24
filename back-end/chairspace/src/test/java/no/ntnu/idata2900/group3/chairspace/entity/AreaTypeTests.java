package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the AreaType class.
 *
 * @see AreaType
 * @author Odin Lyngsgård
 * @version 0.1
 * @since 1.0
 */
public class AreaTypeTests {

	private AreaType areaType;

	@BeforeEach
	void setUp() {
		areaType = new AreaType("Test Type", "Test Description");
	}

	/* ---- Test Constructor ---- */

	@DisplayName("Test that name is assigned")
	@Test
	void testName() {
		assertEquals(areaType.getName(), "Test Type", "Name is not assigned correctly");
	}

	@DisplayName("Test that description is assigned")
	@Test
	void testDescription() {
		assertEquals(areaType.getDescription(), "Test Description", "Description is not assigned correctly");
	}

	@DisplayName("Test that constructor throws exception when name is null")
	@Test
	void testNullName() {
		assertThrows(IllegalArgumentException.class, () -> new AreaType(null, "Test Description"), "Does not throw exception when name is null");
	}

	@DisplayName("Test that constructor throws exception when description is null")
	@Test
	void testNullDescription() {
		assertThrows(IllegalArgumentException.class, () -> new AreaType("Test Type", null), "Does not throw exception when description is null");
	}

	/* ---- Test Methods ---- */

	@DisplayName("Test that setDescription works")
	@Test
	void testSetDescription() {
		areaType.setDescription("New Description");
		assertEquals(areaType.getDescription(), "New Description");
	}

	@DisplayName("Test that setDescription throws exception when null")
	@Test
	void testSetNullDescription() {
		assertThrows(IllegalArgumentException.class, () -> areaType.setDescription(null), "Does not throw exception when description is null");
	}
}
