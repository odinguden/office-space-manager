package no.ntnu.idata2900.group3.chairspace.entity;

/**
 * An interface which contains basic functionality required for entities that are going to be
 * handled by the abstract controller.
 */
public interface EntityInterface<IdTypeT> {

	/**
	 * Returns the id of the entity class.
	 *
	 * @return the id of the entity class.
	 */
	public IdTypeT getId();
}
