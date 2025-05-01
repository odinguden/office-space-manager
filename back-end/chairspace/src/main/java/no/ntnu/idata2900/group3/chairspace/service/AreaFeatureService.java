package no.ntnu.idata2900.group3.chairspace.service;

import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.repository.AreaFeatureRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link AreaFeature}s.
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@Service
public class AreaFeatureService extends EntityService<AreaFeature, String> {

	/**
	 * Creates a new area feature service.
	 *
	 * @param repository autowired AreaFeatureRepository
	 */
	public AreaFeatureService(AreaFeatureRepository repository) {
		super(repository);
	}
}
