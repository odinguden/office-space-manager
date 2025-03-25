package no.ntnu.idata2900.group3.chairspace.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * TODO:
 */
public class PaginationDto<EntityTypeT> {
	private int numberOfPages;
	private List<EntityTypeT> pageContent;

	/**
	 * TODO make constuctor comment.
	 *
	 * @param items
	 */
	public PaginationDto(List<EntityTypeT> items, int itemsPerPage, int currentPage) {
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
	 * TODO: Propper imlementation
	 * @return
	 */
	public List<EntityTypeT> getPageContent() {
		return pageContent;
	}

	/* ---- Setters ---- */

	/**
	 * 
	 * @param numberOfPages
	 */
	private void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	/**
	 * 
	 * @param pageContent
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
