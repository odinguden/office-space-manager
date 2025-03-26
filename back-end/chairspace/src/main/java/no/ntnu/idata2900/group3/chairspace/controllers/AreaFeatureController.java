package no.ntnu.idata2900.group3.chairspace.controllers;

import no.ntnu.idata2900.group3.chairspace.entity.AreaFeature;
import no.ntnu.idata2900.group3.chairspace.repository.AreaFeatureRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for area features.
 * Extends Abstract Controller
 */
@CrossOrigin(origins = "${frontend.url}")
@RestController
@RequestMapping("/areafeature")
public class AreaFeatureController extends AbstractController<AreaFeature, String> {

	protected AreaFeatureController(AreaFeatureRepository repository) {
		super(repository);
	}
}
