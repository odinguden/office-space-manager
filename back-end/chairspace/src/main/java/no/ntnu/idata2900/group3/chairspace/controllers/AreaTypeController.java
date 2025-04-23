package no.ntnu.idata2900.group3.chairspace.controllers;

import no.ntnu.idata2900.group3.chairspace.entity.AreaType;
import no.ntnu.idata2900.group3.chairspace.service.AreaTypeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for area type.
 */
@CrossOrigin(origins = "${frontend.url}")
@RestController
@RequestMapping("/areatype")
public class AreaTypeController extends AbstractController<AreaType, String> {
	protected AreaTypeController(AreaTypeService service) {
		super(service);
	}
}