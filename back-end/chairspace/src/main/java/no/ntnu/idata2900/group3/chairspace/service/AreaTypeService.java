package no.ntnu.idata2900.group3.chairspace.service;

import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.repository.AreaTypeRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link AreaType}s.
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@Service
public class AreaTypeService extends EntityService<AreaType, String> {
	/**
	 * Creates a new area type service.
	 *
	 * @param repository autowired AreaTypeRepository
	 */
	public AreaTypeService(AreaTypeRepository repository) {
		super(repository);
	}
}
