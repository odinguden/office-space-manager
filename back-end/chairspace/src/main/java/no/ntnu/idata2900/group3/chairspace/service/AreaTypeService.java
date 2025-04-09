package no.ntnu.idata2900.group3.chairspace.service;

import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.repository.AreaTypeRepository;

/**
 * Service for AreaType entity.
 */
public class AreaTypeService extends AbstractEntityService<AreaType, String> {

	/**
	 * Creates new instance of areaTypeService.
	 *
	 * @param areaRepository area repository
	 */
	public AreaTypeService(AreaTypeRepository areaRepository) {
		super(areaRepository);
	}
}
