package no.ntnu.idata2900.group3.chairspace.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * A subset of items generated as though it was a page in a book.
 */
public class Pagination<EntityTypeT> {
	/** The default amount of items per page. Must be greater than 0. */
	public static final int DEFAULT_PAGE_SIZE = 12;

	private final int pageNum;
	private final int pages;
	private final int numItems;
	private final int pageSize;
	private final List<EntityTypeT> content;

	/**
	 * Creates a new pagination containing content for the provided page. Each page has 12 items.
	 *
	 * @param items the items to generate a pagination from
	 * @param page the page to select from the pagination. If the page is out of bounds, the content
	 *     will be empty.
	 */
	public Pagination(List<EntityTypeT> items, int page) {
		this.pageNum = page;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.numItems = items.size();
		this.pages = calcNumberOfPages();
		this.content = generateContent(items);
	}

	/**
	 * Creates a new pagination containing content for the provided page.
	 *
	 * @param items the items to generate a pagination from
	 * @param page the page to select from the pagination. If the page is out of bounds, the content
	 *     will be empty.
	 * @param pageSize the size of each page
	 * @throws IllegalArgumentException if {@code pageSize} is less than 1
	 */
	public Pagination(List<EntityTypeT> items, int page, int pageSize) {
		if (pageSize < 1) {
			throw new IllegalArgumentException();
		}

		this.pageNum = page;
		this.pageSize = pageSize;
		this.numItems = items.size();
		this.pages = calcNumberOfPages();
		this.content = generateContent(items);
	}

	/**
	 * Gets the content of this pagination's page.
	 *
	 * @return the content of this pagination's page
	 */
	public List<EntityTypeT> getContent() {
		return this.content;
	}

	private int calcNumberOfPages() {
		return (int) Math.ceil((double) this.numItems / (double) this.pageSize);
	}

	/**
	 * Gets the list of content for the page in this pagination. If the page is out of bounds,
	 * returns an empty list.
	 *
	 * @param items the items to generate the pagination from
	 * @return a list containing the contents of the page
	 */
	private List<EntityTypeT> generateContent(List<EntityTypeT> items) {
		if (this.pageNum < 0 || this.pageNum >= this.pages) {
			return new ArrayList<>();
		}

		int startIdx = this.pageSize * this.pageNum;
		int stopIdx = Math.min(startIdx + this.pageSize, numItems);

		return items.subList(startIdx, stopIdx);
	}

}
