package no.ntnu.idata2900.group3.chairspace.controller;

import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.service.AreaFeatureService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the area feature entity.
 *
 * @see AreaFeature
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@RestController
@RequestMapping("/area-feature")
public class AreaFeatureController extends AbstractController<AreaFeature, String> {

	/**
	 * Creates a new area feature controller.
	 *
	 * @param areaFeatureService autowired area feature service.
	 */
	public AreaFeatureController(AreaFeatureService areaFeatureService) {
		super(areaFeatureService);
	}
}
