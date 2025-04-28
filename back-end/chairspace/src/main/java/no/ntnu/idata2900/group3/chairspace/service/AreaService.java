package no.ntnu.idata2900.group3.chairspace.service;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.repository.AreaRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link Area}s.
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@Service
public class AreaService extends EntityService<Area, UUID> {

	/**
	 * Creates a new area service.
	 *
	 * @param repository autowired AreaRepository
	 */
	public AreaService(AreaRepository repository) {
		super(repository);
	}
}
