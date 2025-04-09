package no.ntnu.idata2900.group3.chairspace.service;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2900.group3.chairspace.entity.EntityInterface;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO.
 */
public abstract class AbstractEntityService<EntityT extends EntityInterface<IdTypeT>, IdTypeT> {
	private CrudRepository<EntityT, IdTypeT> repository;

	protected AbstractEntityService(CrudRepository<EntityT, IdTypeT> repository) {
		this.repository = repository;
	}

	/* ---- Get from database ---- */

	/**
	 * Gets a single entity from the database, or null if none is found with id.
	 *
	 * @param id the id of the entity
	 * @return entity with matching id from database
	 */
	public EntityT getEntity(IdTypeT id) {
		return repository.findById(id).orElse(null);

	}

	/**
	 * Gets all entities from the database.
	 *
	 * @return list containing all entities
	 */
	public List<EntityT> getAll() {

		Iterable<EntityT> entities = repository.findAll();
		List<EntityT> entityList = new ArrayList<>();

		entities.forEach(entityList::add);
		return entityList;
	}

	/* ---- Change database ---- */

	/**
	 * Saves a single entity to the database.
	 *
	 * @param entity the entity to save
	 * @return true if the entity was saved
	 */
	public boolean saveEntity(EntityT entity) {
		boolean saved = true;
		if (entity.getId() != null && repository.findById(entity.getId()).isPresent()) {
			saved = false;
		}
		repository.save(entity);
		return saved;
	}

	/**
	 * Updates a entity in the database.
	 *
	 * @param entity new entity
	 * @return success of update
	 */
	public boolean putEntity(EntityT entity) {
		boolean modified = true;

		if (!repository.existsById(entity.getId())) {
			modified = false;
		} else {
			repository.save(entity);
		}

		return modified;
	}

	/**
	 * Remove an entity based on id.
	 * Returns true if the entity was removed successfully, false otherwise.
	 *
	 * @param id id of the entity to delete
	 * @return boolean indicating success of removal
	 */
	public boolean deleteEntity(IdTypeT id) {
		boolean exists = repository.existsById(id);
		repository.deleteById(id);
		return exists;
	}
}
