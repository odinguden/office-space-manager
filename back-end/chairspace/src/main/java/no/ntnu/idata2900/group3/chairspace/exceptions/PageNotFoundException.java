package no.ntnu.idata2900.group3.chairspace.exceptions;

/**
 * Exception thrown when a requested page index is invalid.
 * This can happen either when the page index is negative or when it exceeds
 * the number of available pages in a paginated data set.
 */
public final class PageNotFoundException extends Exception {

	/**
	 * Constructs a new PageNotFoundException with the specified detail message.
	 *
	 * @param message message explaining the cause of the exception
	 */
	private PageNotFoundException(String message) {
		super(message);
	}

	/**
	 * Creates a PageNotFoundException indicating that a page was
	 * requested beyond the total number of pages.
	 *
	 * @param pageId the index of the page that was requested
	 * @param numberOfPages the total number of available pages
	 * @return a PageNotFoundException with a message about the non-existent page
	 */
	public static PageNotFoundException nonExistentPageException(int pageId, int numberOfPages) {
		return new PageNotFoundException(
			"There is no page with the index: " + pageId
			+ " when the number of pages is: " + numberOfPages
		);
	}
}
