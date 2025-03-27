package no.ntnu.idata2900.group3.chairspace.dto;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2900.group3.chairspace.exceptions.PageNotFoundException;

/**
 * This class works to paginate large amounts of data from the database.
 * This is done to avoid unnecessarily large data transfers to the frontend.
 */
public class PaginationDto<EntityTypeT> {
	private int numberOfPages;
	private List<EntityTypeT> pageContent;

	/**
	 * Constructs a pagination.
	 * The pagination content will contain the items from the index itemsPerPage * current page
	 * to the index ( itemsPerPage * current page ) + itemsPerPage.
	 *
	 * @param items Items to paginate
	 * @param itemsPerPage the amount of items per page
	 * @param currentPage the current page that is requested
	 * @throws PageNotFoundException if the current page is negative
	 */
	public PaginationDto(List<EntityTypeT> items, int itemsPerPage, int currentPage)
		throws PageNotFoundException {
		if (items == null) {
			throw new IllegalArgumentException("Items are null where value was expected");
		}
		setPageContent(items, itemsPerPage, currentPage);
	}

	/* ---- Getters ---- */

	/**
	 * Returns the total number of pages.
	 *
	 * @return the total number possible pages.
	 */
	public int getNumberOfPages() {
		return numberOfPages;
	}

	/**
	 * Returns the page content in a list.
	 *
	 * @return page content
	 */
	public List<EntityTypeT> getPageContent() {
		return pageContent;
	}

	/* ---- Setters ---- */

	/**
	 * Sets the total number of pages in the pagination.
	 *
	 * @param itemsPerPage the number of items per page
	 * @param numberOfItems the total number of items
	 */
	private void setNumberOfPages(int itemsPerPage, int numberOfItems) {
		if (itemsPerPage <= 0) {
			throw new IllegalArgumentException(
				"Number of items on a page cannot be equal or less than zero"
			);
		}
		if (numberOfItems < 0) {
			// Something has gone horribly wrong if an array has less than zero entries.
			// At least it wouldn't be something wrong with our code.
			throw new IllegalArgumentException(
				"The content on a page cannot be less than zero"
			);
		}
		numberOfPages = (int) Math.ceil((double) numberOfItems / (double) itemsPerPage);
	}

	/**
	 * Sets the page content.
	 * The items for the page will be the items starting on content list with
	 * the index currentPage * itemsPerPage and stopping when the page content is filled or when
	 * there are no more items left in the content list.
	 *
	 * @param content the content that needs o be paginated
	 * @param itemsPerPage the number of items on a page
	 * @param currentPage the current page
	 * @throws IllegalArgumentException if the number of items per page is zero
	 * @throws IllegalArgumentException if the index of the requested page is less than zero
	 * @throws PageNotFoundException if a non extant page is requested
	 */
	private void setPageContent(List<EntityTypeT> content, int itemsPerPage, int currentPage)
		throws PageNotFoundException {
		if (itemsPerPage < 0) {
			throw new IllegalArgumentException(
				"The number of items per page cannot be less than zero"
			);
		}
		setNumberOfPages(itemsPerPage, content.size());
		if (currentPage < 0) {
			throw new IllegalArgumentException("Page index cannot be a negative number");
		}
		if (currentPage > numberOfPages) {
			throw PageNotFoundException.nonExistentPageException(currentPage, numberOfPages);
		}

		pageContent = new ArrayList<>();
		int currentIndex = itemsPerPage * currentPage;
		int indexStop = currentIndex + itemsPerPage;


		while (currentIndex < content.size() && currentIndex < indexStop) {
			pageContent.add(content.get(currentIndex));
			currentIndex++;
		}
	}
}
