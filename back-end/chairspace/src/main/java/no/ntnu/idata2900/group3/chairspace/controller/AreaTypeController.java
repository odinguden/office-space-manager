package no.ntnu.idata2900.group3.chairspace.controller;

import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.service.AreaTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the area type entity.
 *
 * @see AreaType
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@RestController
@RequestMapping("/area-type")
public class AreaTypeController extends AbstractController<AreaType, String> {

	/**
	 * Creates a new area type controller.
	 *
	 * @param areaTypeService autowired area type service.
	 */
	public AreaTypeController(AreaTypeService areaTypeService) {
		super(areaTypeService);
	}
}
