package no.ntnu.idata2900.group3.chairspace.repository;

import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the AreaFeature entity.
 */
@Repository
public interface AreaFeatureRepository extends CrudRepository<AreaFeature, String> {}
