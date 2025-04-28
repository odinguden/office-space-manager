package no.ntnu.idata2900.group3.chairspace.service;

import java.util.List;
import java.util.stream.StreamSupport;
import no.ntnu.idata2900.group3.chairspace.entity.EntityInterface;
import org.springframework.data.repository.CrudRepository;

/**
 * A base class for services that need to implement basic CRUD operations.
 *
 * <p>
 * Allows services to perform all the basic CRUD operations, including create, retrieve, update,
 * and delete.
 * </p>
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 * @see #get
 * @see #getAll
 * @see #create
 * @see #update
 * @see #delete
 */
public abstract class EntityService<EntityT extends EntityInterface<IdT>, IdT> {
	protected final CrudRepository<EntityT, IdT> repository;

	protected EntityService(CrudRepository<EntityT, IdT> repository) {
		this.repository = repository;
	}

	/**
	 * Checks if an entity with the given ID exists.
	 *
	 * @param id the id to check
	 * @return true if entity exists
	 */
	public boolean exists(IdT id) {
		return this.repository.existsById(id);
	}

	/**
	 * Gets the entity with the given id.
	 *
	 * @param id the id of the entity to get
	 * @return the found entity, or null if it does not exist
	 */
	public EntityT get(IdT id) {
		return this.repository.findById(id).orElse(null);
	}

	/**
	 * Gets all entities as a list.
	 *
	 * @return all entities as a list
	 */
	public List<EntityT> getAll() {
		Iterable<EntityT> iterableItems = this.repository.findAll();
		return iterableToList(iterableItems);
	}

	/**
	 * Attempts to update an existing entity.
	 *
	 * @param entity the updated entity
	 * @return true if the entity was updated, false if it does not exist
	 */
	public boolean update(EntityT entity) {
		if (entity.getId() == null || !this.exists(entity.getId())) {
			return false;
		}

		this.save(entity);
		return true;
	}

	/**
	 * Attempts to create a new entity.
	 *
	 * @param entity the entity to save
	 * @return true if the entity was created, false if it already exists
	 */
	public boolean create(EntityT entity) {
		if (entity.getId() != null && this.exists(entity.getId())) {
			return false;
		}

		this.save(entity);
		return true;
	}

	/**
	 * A wrapper for {@link CrudRepository#save}.
	 *
	 * @param entity entity to save.
	 */
	protected void save(EntityT entity) {
		this.repository.save(entity);
	}

	/**
	 * Attempts to delete an entity.
	 *
	 * @param id the id of the entity to be deleted
	 * @return true if the entity was deleted, false if it does not exist
	 */
	public boolean delete(IdT id) {
		if (this.exists(id)) {
			return false;
		}

		repository.deleteById(id);
		return true;
	}

	/**
	 * Efficiently converts an iterable to a list.
	 *
	 * @param iterable the iterable to convert
	 * @return the iterable as a list
	 */
	protected <T> List<T> iterableToList(Iterable<T> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false).toList();
	}
}
