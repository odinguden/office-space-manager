package no.ntnu.idata2900.group3.chairspace.controller;

import java.util.List;
import java.util.UUID;

import no.ntnu.idata2900.group3.chairspace.dto.SimplePlan;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
import no.ntnu.idata2900.group3.chairspace.service.PlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for plan entity.
 */
@RestController
@RequestMapping("/plans")
public class PlanController extends AbstractController<Plan, UUID> {
	private final PlanService planService;

	/**
	 * Constructor for plan controller.
	 *
	 * @param planService autowired plan service
	 */
	public PlanController(PlanService planService) {
		super(planService);
		this.planService = planService;
	}

	/**
	 * Gets all plans belonging to a single area.
	 *
	 * @param areaId id of the area
	 * @return plans belonging to a single area.
	 */
	@GetMapping()
	public ResponseEntity<List<SimplePlan>> getPlansByArea(@RequestParam UUID areaId) {
		hasPermissionToGet();
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
