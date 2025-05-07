package no.ntnu.idata2900.group3.chairspace.controller;

import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.assembler.PlanAssembler;
import no.ntnu.idata2900.group3.chairspace.dto.SimplePlan;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.repository.PlanRepository;
import no.ntnu.idata2900.group3.chairspace.service.PlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



/**
 * Controller for plan entity.
 */
@RestController
@RequestMapping("/plans")
public class PlanController extends PermissionManager {

	private final PlanRepository planRepository;
	private final PlanService planService;
	private final PlanAssembler planAssembler;

	/**
	 * Constructor for plan controller.
	 *
	 * @param planService autowired plan service
	 * @param planAssembler autowired plan assembler
	 */
	public PlanController(
		PlanService planService,
		PlanAssembler planAssembler,
		PlanRepository planRepository
	) {
		this.planService = planService;
		this.planAssembler = planAssembler;
		this.planRepository = planRepository;
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

	/**
	 * Creates a new plan based on a simple plan.
	 *
	 * @param plan the simple plan to use as base
	 * @return 200 OK if new plan is created
	 * @throws ResponseStatusException 400 bad request if values for simple plan is invalid
	 */
	@PostMapping("")
	public ResponseEntity<String> postMethodName(@RequestBody SimplePlan plan) {
		Plan realPlan;
		try {
			realPlan = planAssembler.toDomain(plan);
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		planService.create(realPlan);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Deletes a single plan based on id.
	 *
	 * @param planId the id of the plan to delete
	 * @return 200 ok if the plan was deleted
	 */
	@DeleteMapping("/{planId}")
	public ResponseEntity<String> deleteEntity(@PathVariable UUID planId) {
		if (!planRepository.existsById(planId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		planRepository.deleteById(planId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
