package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
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
	private AreaType areaType;
	private static String name = "Test Type";
	private static String description = "Test Description";


	@BeforeEach
	void setUp() {
		try {
			areaType = new AreaType(name, description);
		} catch (Exception e) {
			fail("Failed to create AreaType" + e.getMessage(), e);
			return;
		}
	}

	/* ---- Test Constructor ---- */

	@Test
	void testSingleArgConstructor() {
		AreaType singleArgAreaType;
		try {
			singleArgAreaType = new AreaType(name);
		} catch (InvalidArgumentCheckedException e) {
			fail("Failed to create area type", e);
			return;
		}
		assertEquals(name, singleArgAreaType.getId());
		assertEquals("", singleArgAreaType.getDescription());
	}

	@DisplayName("Test that name is assigned")
	@Test
	void testName() {
		assertEquals(name, areaType.getId(), "Name is not assigned correctly");
	}

	@DisplayName("Test that description is assigned")
	@Test
	void testDescription() {
		assertEquals(
			description,
			areaType.getDescription(),
			"Description is not assigned correctly"
		);
	}

	@DisplayName("Test that constructor throws exception when name is null")
	@Test
	void testNullName() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new AreaType(null, "Test Description"),
			"Does not throw exception when name is null"
		);
	}

	@DisplayName("Test that constructor throws exception when name is blank")
	@Test
	void testBlankName() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new AreaType("", "Test Description"),
			"Does not throw exception when name is blank"
		);
	}

	/* ---- Test Methods ---- */

	@DisplayName("Test that setDescription works")
	@Test
	void testSetDescription() {
		String newDescription = "New Description";
		areaType.setDescription(newDescription);
		assertEquals(newDescription, areaType.getDescription());
	}

	@DisplayName("Test that setDescription throws exception when null")
	@Test
	void testSetNullDescription() {
		assertThrows(
			IllegalArgumentException.class, () -> areaType.setDescription(null),
			"Does not throw exception when description is null"
		);
	}
}
