package no.ntnu.idata2900.group3.chairspace.service;

import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.repository.AreaFeatureRepository;

/**
 * Service containing basic methods for area feature service.
 */
public class AreaFeatureService extends AbstractEntityService<AreaFeature, String> {

	/**
	 * Creates new instance of AreaFeatureService.
	 *
	 * @param controller AreaFeatureController
	 */
	public AreaFeatureService(AreaFeatureRepository controller) {
		super(controller);
	}
}
