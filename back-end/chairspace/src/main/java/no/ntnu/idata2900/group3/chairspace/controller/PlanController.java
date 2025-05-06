package no.ntnu.idata2900.group3.chairspace.controller;

import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.assembler.PlanAssembler;
import no.ntnu.idata2900.group3.chairspace.dto.SimplePlan;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
import no.ntnu.idata2900.group3.chairspace.service.PlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for plan entity.
 */
@RestController
@RequestMapping("/plans")
public class PlanController extends AbstractController<Plan, UUID> {
	private final PlanService planService;
	private final PlanAssembler planAssembler;

	/**
	 * Constructor for plan controller.
	 *
	 * @param planService autowired plan service
	 * @param planAssembler autowired plan assembler
	 */
	public PlanController(PlanService planService, PlanAssembler planAssembler) {
		super(planService);
		this.planService = planService;
		this.planAssembler = planAssembler;
	}

	/**
	 * Gets all plans belonging to a single area.
	 *
	 * @param areaId id of the area
	 * @return plans belonging to a single area
	 */
	@GetMapping("/area/{areaId}")
	public ResponseEntity<List<SimplePlan>> getPlansByArea(
		@PathVariable UUID areaId
	) {
		hasPermissionToGet();
		List<Plan> plans = planService.getPlansByArea(areaId);
		List<SimplePlan> simplePlans = plans
			.stream()
			.map(planAssembler::toSimple)
			.toList();
		return new ResponseEntity<>(simplePlans, HttpStatus.OK);
	}
}
