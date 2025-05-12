package no.ntnu.idata2900.group3.chairspace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.assembler.PlanAssembler;
import no.ntnu.idata2900.group3.chairspace.dto.SimplePlan;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.Plan;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.service.AreaService;
import no.ntnu.idata2900.group3.chairspace.service.PlanService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


/**
 * Controller for plan entity.
 */
@RestController
@RequestMapping("/plan")
public class PlanController extends PermissionManager {

	private final AreaService areaService;
	private final PlanService planService;
	private final PlanAssembler planAssembler;

	/**
	 * Constructor for plan controller.
	 *
	 * @param planService autowired plan service
	 * @param planAssembler autowired plan assembler
	 * @param areaService autowired area service
	 */
	public PlanController(
		PlanService planService,
		PlanAssembler planAssembler,
		AreaService areaService) {
		this.planService = planService;
		this.planAssembler = planAssembler;
		this.areaService = areaService;
	}

	/**
	 * Gets all plans belonging to a single area.
	 *
	 * @param areaId id of the area
	 * @return plans belonging to a single area
	 */
	@GetMapping("/area/{areaId}")
	@Operation(
		summary = "Gets all plans for a area"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found plans of area"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users do not have access to read plans"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to read these plans"
			)
	})
	public ResponseEntity<List<SimplePlan>> getPlansByArea(
		@Parameter(description = "Id of the area to get plans for")
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
	@Operation(
		summary = "Creates a new plan",
		description = "Creates a new plan based on data from simple plan"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Plan was successfully created"
			),
		@ApiResponse(
			responseCode = "400",
			description = "If values within simple plan object are invalid to create new plan"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to create new plans"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to create a new plan"
			),
	})
	public ResponseEntity<UUID> postMethodName(
		@Parameter(description = "Data used to create new plan")
		@RequestBody SimplePlan plan
	) {
		hasPermissionToPost();
		Plan realPlan;
		try {
			realPlan = planAssembler.toDomain(plan);
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		UUID id = planService.create(realPlan);
		return new ResponseEntity<>(id, HttpStatus.CREATED);
	}

	/**
	 * Deletes a single plan based on id.
	 *
	 * @param planId the id of the plan to delete
	 * @return 200 ok if the plan was deleted
	 */
	@DeleteMapping("/{planId}")
	@Operation(
		summary = "Deletes a plan",
		description = "Deletes a plan using the given id"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Plan was deleted"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Could not find plan with given id"
			)
	})
	public ResponseEntity<String> deleteEntity(
		@Parameter(description = "The id of the plan to delete")
		@PathVariable UUID planId
	) {
		this.hasPermissionToDelete();
		planService.delete(planId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Gets all plans that belong to areas that belong to the given user.
	 *
	 * @param userId the user to get plans for
	 * @param page the page of the plans pagination
	 * @return a pagination of plans that belong to areas that the given user administrates
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<Page<SimplePlan>> getPlansForUser(
		@PathVariable UUID userId,
		@RequestParam(required = false) Integer page
	) {
		List<UUID> areaIds = areaService.getAreasByUserAsList(userId)
			.stream()
			.map(Area::getId)
			.toList();

		Page<SimplePlan> plans = planService.getPlansByAreas(areaIds, page)
			.map(planAssembler::toSimple);

		return new ResponseEntity<>(plans, HttpStatus.OK);
	}
}
