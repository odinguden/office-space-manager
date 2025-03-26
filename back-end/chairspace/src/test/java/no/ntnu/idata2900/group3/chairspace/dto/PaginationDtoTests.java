package no.ntnu.idata2900.group3.chairspace.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the pagination dto.
 *
 * @see PaginationDto
 */
public class PaginationDtoTests {
	private static ArrayList<AreaFeature> content;
	private static String name = "Feature: ";
	private static String description = "Description: ";

	@BeforeAll
	static void initialize() {
		content = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			try {
				content.add(
					new AreaFeature(
						name + i,
						description + i
						)
				);
			} catch (InvalidArgumentCheckedException e) {
				fail("Failed to create Area feature with index" + i, e);
			}
		}
	}

	@Test
	void testConstructor() {
		PaginationDto<AreaFeature> PaginationDto;
		try {
			PaginationDto = new PaginationDto<>(content, 20, 0);
		} catch (InvalidArgumentCheckedException e) {
			fail("Failed to create pagination", e);
			return;
		}
		assertEquals(20, PaginationDto.getPageContent().size(),
			"Page content has the wrong number of items"
		);
		assertEquals(5, PaginationDto.getNumberOfPages());
	}

	@Test
	void testConstructorThrowsWhenNegativeCurrentPage() {
		assertThrows(
			InvalidArgumentCheckedException.class,
			() -> new PaginationDto<>(content, 10, -1)
		);
	}

	@Test
	void testConstructorThrowsWhenNegativeItemsPerPage() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new PaginationDto<>(content, -10, 1)
		);
	}

	@Test
	void testPageNumberRoundUp() {
		PaginationDto<AreaFeature> PaginationDto;
		try {
			PaginationDto = new PaginationDto<>(content, 24, 1);
		} catch (InvalidArgumentCheckedException e) {
			fail("Failed to create pagination", e);
			return;
		}
		assertEquals(5, PaginationDto.getNumberOfPages());
	}

	@Test
	void testNullContent() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new PaginationDto<AreaFeature>(null, 12, 3)
		);
	}
}
