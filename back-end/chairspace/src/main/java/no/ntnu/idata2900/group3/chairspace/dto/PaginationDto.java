package no.ntnu.idata2900.group3.chairspace.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * This class works to paginate large amounts of data from the database.
 * This is done to avoid unnecessarily large data transfers to the frontend.
 */
public class PaginationDTO<EntityTypeT> {
	private int numberOfPages;
	private List<EntityTypeT> pageContent;

	/**
	 * Constructs a pagination.
	 *
	 * @param items Items to paginate
	 * @param itemsPerPage the amount of items per page
	 * @param currentPage the current page that is requested
	 */
	public PaginationDTO(List<EntityTypeT> items, int itemsPerPage, int currentPage) {
		setNumberOfPages(items.size());
		setPageContent(items, currentPage, itemsPerPage);


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
	 * Returns the page content.
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
	 * @param numberOfPages the total number of pages
	 */
	private void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
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
	 */
	private void setPageContent(List<EntityTypeT> content, int itemsPerPage, int currentPage) {
		int currentIndex = itemsPerPage * currentPage;
		int indexStop = currentIndex + itemsPerPage;

		pageContent = new ArrayList<>();

		while (currentIndex < content.size() && currentIndex < indexStop) {
			pageContent.add(content.get(currentIndex));
			currentIndex++;
		}
	}
}
