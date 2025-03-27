package no.ntnu.idata2900.group3.chairspace.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.PageNotFoundException;
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
	static void initialize() throws InvalidArgumentCheckedException {
		content = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			content.add(
				new AreaFeature(
					name + i,
					description + i
				)
			);
		}
	}

	@Test
	void testConstructor() {
		PaginationDto<AreaFeature> paginationDto;
		try {
			paginationDto = new PaginationDto<>(content, 20, 1);
		} catch (PageNotFoundException e) {
			fail("Failed to create pagination", e);
			return;
		}
		assertEquals(5, paginationDto.getNumberOfPages());
		List<AreaFeature> pageContent = paginationDto.getPageContent();
		for (int i = 20; i < 40; i++) {
			assertEquals(
				content.get(i),
				pageContent.get(i % 20),
				"Expected: " + content.get(i).getId()
				+ " but was: " + pageContent.get(i % 20).getId()
				+ " At index: " + i
			);
		}
	}

	@Test
	void testConstructorThrowsWhenNegativeCurrentPage() {
		assertThrows(
			PageNotFoundException.class,
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
	void testPageNumber() {
		PaginationDto<AreaFeature> paginationDto;
		try {
			paginationDto = new PaginationDto<>(content, 24, 1);
		} catch (PageNotFoundException e) {
			fail("Failed to create pagination", e);
			return;
		}
		assertEquals(5, paginationDto.getNumberOfPages());
	}

	@Test
	void testInvalidPageNumberThrows() {
		assertThrows(
			PageNotFoundException.class,
			() -> new PaginationDto<>(content, 20, 6),
			"Pagination constructor does not throw when a non extant page is requested"
		);
	}

	@Test
	void testNullContent() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new PaginationDto<AreaFeature>(null, 12, 3)
		);
	}
}
