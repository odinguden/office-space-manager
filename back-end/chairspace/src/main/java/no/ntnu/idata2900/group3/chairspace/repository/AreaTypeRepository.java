package no.ntnu.idata2900.group3.chairspace.repository;

import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the AreaType entity.
 */
@Repository
public interface AreaTypeRepository extends CrudRepository<AreaType, String> {}