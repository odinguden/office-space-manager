package no.ntnu.idata2900.group3.chairspace.service;

import java.util.List;
import java.util.stream.StreamSupport;
import no.ntnu.idata2900.group3.chairspace.entity.EntityInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
	/** The default amount of items per page. Must be greater than 0. */
	public static final int DEFAULT_PAGE_SIZE = 12;

	protected final JpaRepository<EntityT, IdT> repository;

	protected EntityService(JpaRepository<EntityT, IdT> repository) {
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
	 * Gets a page of entities from the repository.
	 *
	 * @param page the page to get
	 * @return a page of 12 entities
	 */
	public Page<EntityT> getAllPaged(int page) {
		return this.getAllPaged(page, DEFAULT_PAGE_SIZE);
	}

	/**
	 * Gets a page of entities from the repository.
	 *
	 * @param page the page to get
	 * @param size the amount of entries per page
	 * @return a page of entities
	 */
	public Page<EntityT> getAllPaged(int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		return repository.findAll(paging);
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
	 * @return The ID of the created entity, or null if it already exists
	 */
	public IdT create(EntityT entity) {
		if (entity.getId() != null && this.exists(entity.getId())) {
			return null;
		}

		return this.save(entity);
	}

	/**
	 * A wrapper for {@link CrudRepository#save}.
	 *
	 * @param entity entity to save.
	 */
	protected IdT save(EntityT entity) {
		EntityT savedEntity = this.repository.save(entity);
		return savedEntity.getId();
	}

	/**
	 * Attempts to delete an entity.
	 *
	 * @param id the id of the entity to be deleted
	 * @return true if the entity was deleted, false if it does not exist
	 */
	public boolean delete(IdT id) {
		if (!this.exists(id)) {
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
