package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;

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
	private static String name = "Test Feature";
	private static String description = "Test Description";


	@BeforeEach
	void setUp() {
		try {
			areaFeature = new AreaFeature(name, description);
		} catch (Exception e) {
			fail("Failed to create AreaFeature" + e.getMessage(), e);
			return;
		}
	}

	/* ---- Test Constructor ---- */

	@DisplayName("Test that name is assigned")
	@Test
	void testName() {
		assertEquals(name, areaFeature.getName(), "Name is not assigned correctly");
	}

	@DisplayName("Test that description is assigned")
	@Test
	void testDescription() {
		assertEquals(
			description,
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
			InvalidArgumentCheckedException.class,
			() -> new AreaFeature("","Test Description"),
			"Does not throw exception when name is blank"
		);
	}

	/* ---- Test Methods ---- */

	@DisplayName("Test that setDescription works")
	@Test
	void testSetDescription() {
		String newDescription = "New Description";
		areaFeature.setDescription(newDescription);
		assertEquals(newDescription, areaFeature.getDescription());
	}

	@DisplayName("Test that setDescription throws exception when null")
	@Test
	void testSetNullDescription() {
		assertThrows(
			IllegalArgumentException.class, () -> areaFeature.setDescription(null),
			"Does not throw exception when description is null"
		);
	}
}