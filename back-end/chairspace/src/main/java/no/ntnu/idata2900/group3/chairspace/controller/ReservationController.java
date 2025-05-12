package no.ntnu.idata2900.group3.chairspace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import no.ntnu.idata2900.group3.chairspace.assembler.ReservationAssembler;
import no.ntnu.idata2900.group3.chairspace.dto.MakeReservationDto;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleReservation;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleReservationList;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.NotReservableException;
import no.ntnu.idata2900.group3.chairspace.service.AreaService;
import no.ntnu.idata2900.group3.chairspace.service.ReservationService;
import no.ntnu.idata2900.group3.chairspace.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


/**
 * Controller for the reservation feature entity.
 *
 * @see Reservation
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController extends PermissionManager {
	private final ReservationService reservationService;
	private final UserService userService;
	private final AreaService areaService;
	private final ReservationAssembler reservationAssembler;

	/**
	 * Creates a new reservation controller.
	 *
	 * @param reservationService autowired reservation service
	 * @param areaService autowired area service
	 * @param userService autowired user service
	 * @param reservationAssembler autowired reservation assembler
	 */
	public ReservationController(
		ReservationService reservationService,
		UserService userService,
		AreaService areaService,
		ReservationAssembler reservationAssembler
	) {
		this.reservationService = reservationService;
		this.areaService = areaService;
		this.userService = userService;
		this.reservationAssembler = reservationAssembler;
	}

	/**
	 * Gets a single reservation by ID.
	 *
	 * @param id the id of the reservation to get
	 * @return 200 OK with the found reservation, 404 if not found
	 */
	@GetMapping("/{id}")
	@Operation(
		summary = "Gets a single reservation based on id",
		description = "Gets a reservation based on the provided id"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found a reservation with the given id"
			),
		@ApiResponse(
			responseCode = "404",
			description = "If a reservation cannot be found with the given id"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users do not have access to read reservations"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to read reservations"
			)
	})
	public ResponseEntity<SimpleReservation> get(
		@Parameter(description = "The id of the reservation to get")
		@PathVariable UUID id
	) {
		this.hasPermissionToGet();
		Reservation reservation = this.reservationService.get(id);

		if (reservation == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(reservationAssembler.toSimple(reservation), HttpStatus.OK);
	}

	/**
	 * Gets all reservations paginated.
	 *
	 * @param page the page of the pagination to retrieve
	 * @return a page of the pagination of reservations
	 */
	@GetMapping("")
	@Operation(
		summary = "Gets all reservations",
		description = "Returns all reservations paginated"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found all reservations"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users do not have access to read reservations"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to read reservations"
			)
	})
	public ResponseEntity<Page<SimpleReservation>> getAll(
		@Parameter(description = "The requested page, Will be 0 if not provided")
		@RequestParam(required = false) Integer page
	) {
		if (page == null || page < 0) {
			page = 0;
		}

		this.hasPermissionToGetAll();

		Page<Reservation> reservations = this.reservationService.getAllPaged(page);
		Page<SimpleReservation> simpleReservations = reservations.map(
			reservationAssembler::toSimple
		);

		return new ResponseEntity<>(simpleReservations, HttpStatus.OK);
	}

	/**
	 * Gets reservations belonging to an area between two timestamps.
	 *
	 * @param id the id of the area to retrieve reservations for
	 * @param start the starting timestamp
	 * @param end the ending timestamp
	 * @return a list of reservations for the area within a timespan
	 */
	@GetMapping("/area/{id}")
	@Operation(
		summary = "Gets reservations belonging to an area between two timestamps.",
		description = "Gets a list of all the reservations of a area between timestamps"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found all reservations"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users do not have access to read reservations"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to read reservations"
			)
	})
	public ResponseEntity<List<SimpleReservation>> getForAreaInTime(
		@Parameter(description = "The id of the area to retrieve reservations for")
		@PathVariable UUID id,
		@Parameter(description = "The starting timestamp")
		@RequestParam LocalDateTime start,
		@Parameter(description = "The ending timestamp")
		@RequestParam LocalDateTime end
	) {
		this.hasPermissionToGet();
		List<Reservation> reservations =
			this.reservationService.getReservationsForAreaBetween(id, start, end);

		return new ResponseEntity<>(
			reservations.stream().map(reservationAssembler::toSimple).toList(),
			HttpStatus.OK
		);
	}

	/**
	 * Gets the reservation frequency for either a day or a month. If day is null and month/year
	 * are defined, gets the aggregated frequency of the month. If month/year is null and day is
	 * defined, gets the frequency of the day. Otherwise returns 404.
	 *
	 * @param id the id of the area to retrieve the frequency for
	 * @param date the day to get the frequency of
	 * @param year the year to get the frequency of
	 * @param month the month to get the frequency of
	 * @return the reservation frequency of the room, as an integer percentage.
	 */
	@GetMapping("/area/{id}/frequency")
	@Operation(
		summary = "Gets the reservation frequency for either a day or a month",
		description = "Gets the reservation frequency for either a day or a month."
		+ " If day is null and month/year are defined, gets the aggregated frequency of the month."
		+ " If month/year is null and day is defined, gets the frequency of the day."
		+ " Otherwise returns 404."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Calculated frequency"
			),
		@ApiResponse(
			responseCode = "400",
			description = "Missing necessary data"
			)
	})
	public ResponseEntity<Integer> getReservationFrequency(
		@Parameter(description = "the id of the area to retrieve the frequency for")
		@PathVariable UUID id,
		@Parameter(description = "the day to get the frequency of")
		@RequestParam(required = false) LocalDate date,
		@Parameter(description = "the year to get the frequency of")
		@RequestParam(required = false) Integer year,
		@Parameter(description = "the month to get the frequency of")
		@RequestParam(required = false) Integer month
	) {
		float frequency;
		if (date == null && month != null && year != null) {
			frequency = reservationService.getReservationFrequencyForMonth(id, year, month);
		} else if (date != null && month == null && year == null) {
			frequency = reservationService.getReservationFrequencyForDay(id, date);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>((int) (frequency * 100), HttpStatus.OK);
	}

	/**
	 * Gets the reservation frequency for a month as a list of frequencies for each day.
	 *
	 * @param id the id of the area to get the frequencies of
	 * @param year the year to get the frequency of
	 * @param month the month to get the frequency of
	 * @return a list of frequencies for the provided month for the area
	 */
	@GetMapping("/area/{id}/frequency/list")
	@Operation(
		summary = "Gets the reservation frequency for a month",
		description = "Gets the reservation frequency for a month as a list of"
			+ " frequencies for each day"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found frequencies for month"
			),
	})
	public ResponseEntity<List<Integer>> getReservationFrequencyForFullMonth(
		@Parameter(description = "The id of the area to get the frequencies of ")
		@PathVariable UUID id,
		@Parameter(description = "The year to get the frequency of")
		@RequestParam int year,
		@Parameter(description = "The month to get the frequency of")
		@RequestParam int month
	) {
		List<Float> frequencies =
			reservationService.getReservationFrequencyForDaysInMonth(id, year, month);

		List<Integer> frequencyInts = frequencies.stream()
			.map(frequency -> (int) (frequency * 100))
			.toList();

		return new ResponseEntity<>(
			frequencyInts,
			HttpStatus.OK
		);
	}

	/**
	 * Creates a new reservation based on a simple reservation.
	 *
	 * @param simpleReservation the simple reservation to create from
	 * @return 201 CREATED
	 */
	@PostMapping("")
	@Operation(
		summary = "Creates a new reservation",
		description = "Creates a new reservation based on simple reservation"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Reservation was created"
			),
		@ApiResponse(
			responseCode = "400",
			description = "If simple reservation contains invalid data"
			)
	})
	public ResponseEntity<UUID> post(
		@Parameter(description = "The simple reservation to create from")
		@RequestBody SimpleReservation simpleReservation
	) {
		this.hasPermissionToPost();
		Reservation reservation;

		try {
			reservation = reservationAssembler.toDomain(simpleReservation);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		reservationService.create(reservation);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Creates a new reservation for the currently logged in user.
	 *
	 * @param reservationMakeRequest DTO object containing data to make a reservation
	 * @return 204 NO CONTENT
	 */
	@PostMapping("/make")
	@Operation(
		summary = "Creates new reservation for the current user"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Reservation was created"
			),
		@ApiResponse(
			responseCode = "400",
			description = "If simple reservation contains invalid data"
			)	})
	public ResponseEntity<UUID> bookRoomForMe(
		@Parameter(description = "The simple reservation to create from")
		@RequestBody MakeReservationDto reservationMakeRequest
	) {
		User user = userService.getSessionUser();
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		Area area = areaService.get(reservationMakeRequest.roomId());
		if (area == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		Reservation reservation;
		try {
			reservation = new Reservation(
				area,
				user,
				reservationMakeRequest.startTime(),
				reservationMakeRequest.endTime(),
				reservationMakeRequest.comment());
		} catch (InvalidArgumentCheckedException | NotReservableException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		UUID id = reservationService.create(reservation);
		if (id == null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
	}

	/**
	 * Updates an existing reservation.
	 *
	 * @param simpleReservation the simple reservation to use for updating
	 * @return 204 NO CONTENT
	 */
	@PutMapping("")
	@Operation(
		summary = "Updates a reservation",
		description = "Updates an existing reservation based on data from a simple reservation"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Successfully updated reservation"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to update reservations"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to update reservations"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Failed to update the entity as it doesn't exist"
			)	})
	public ResponseEntity<String> put(
		@Parameter(description = "The simple reservation to update from")
		@RequestBody SimpleReservation simpleReservation
	) {
		this.hasPermissionToPut();
		Reservation reservation;
		try {
			reservation = reservationAssembler.toDomain(simpleReservation);
		} catch (InvalidArgumentCheckedException | NotReservableException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		reservationService.update(reservation);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Attempts to delete the reservation with the given ID.
	 *
	 * @param id the id of the reservation to delete
	 * @return 204 NO CONTENT
	 */
	@DeleteMapping("/{id}")
	@Operation(
		summary = "Deletes a reservation",
		description = "Attempts to delete a reservation with the given ID"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Successfully deleted the reservation"
			),
		@ApiResponse(
			responseCode = "401",
			description = "Unauthorized users are not permitted to delete reservations"
			),
		@ApiResponse(
			responseCode = "403",
			description = "User has insufficient permissions to delete reservations"
			),
		@ApiResponse(
			responseCode = "404",
			description = "Failed to delete the reservation as it doesn't exist"
			)
	})
	public ResponseEntity<String> delete(
		@Parameter(description = "The id of the reservation to delete")
		@PathVariable UUID id
	) {
		this.hasPermissionToDelete();
		reservationService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Gets all reservations belonging to a given user.
	 *
	 * @param userId the id of the user to get the reservations of
	 * @return reservations belonging to the user
	 */
	@GetMapping("/user/{userId}")
	@Operation(
		summary = "Gets all the reservations belonging to a user",
		description = "Gets all the reservations belonging to a user in a list"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found the reservations belonging to the user"
			),
	})
	public ResponseEntity<List<SimpleReservation>> getReservationsByUser(
		@Parameter(description = "The id of the user")
		@PathVariable UUID userId
	) {
		this.hasPermissionToGet();
		List<Reservation> reservations = reservationService.getReservationsByUser(userId);
		List<SimpleReservation> simpleReservations = reservations.stream()
			.map(reservationAssembler::toSimple)
			.toList();
		return new ResponseEntity<>(simpleReservations, HttpStatus.OK);
	}

	/**
	 * Gets a map of all areas in which the current user has a booking, mapped to all bookings for
	 *     those areas, from the start of the first booking to the end of the last booking made
	 *     by the user.
	 *
	 * @return a map of all areas the user has booked connected to simple reservation lists
	 *     for the scope of the user's reservations.
	 */
	@GetMapping("/user/me")
	@Operation(
		summary = "Gets a map of all the areas where the current user has made a reservation",
		description = "Gets a map of all areas in which the current user has a booking,"
			+ "mapped to all bookings for those areas, "
			+ "from the start of the first booking to the end of the last booking made by the user."
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found the areas where the current user has made a reservation"
			)
	})
	public ResponseEntity<Map<UUID, SimpleReservationList>> getMyReservationData() {
		this.hasPermissionToGet();
		User sessionUser = userService.getSessionUser();
		if (sessionUser == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		List<Reservation> myReservations = reservationService
			.getReservationsByUser(sessionUser.getId());

		if (myReservations.isEmpty()) {
			return new ResponseEntity<>(
				new HashMap<>(),
				HttpStatus.OK
			);
		}

		LocalDateTime scopeStart = myReservations.stream()
			.map(Reservation::getStart)
			.min(Comparator.naturalOrder())
			.orElseThrow();

		LocalDateTime scopeEnd = myReservations.stream()
			.map(Reservation::getEnd)
			.max(Comparator.naturalOrder())
			.orElseThrow();

		Set<UUID> areaIds = myReservations.stream()
			.map(Reservation::getArea)
			.map(Area::getId)
			.collect(Collectors.toSet());

		Map<UUID, SimpleReservationList> allReservationsMap = new HashMap<>();

		for (UUID areaId : areaIds) {
			List<SimpleReservation> areaReservations = reservationService
				.getReservationsForAreaBetween(areaId, scopeStart, scopeEnd)
				.stream()
				.map(reservationAssembler::toSimple)
				.toList();

			SimpleReservationList simpleReservationList = new SimpleReservationList(
				scopeStart,
				scopeEnd,
				areaReservations
			);

			allReservationsMap.put(areaId, simpleReservationList);
		}

		return new ResponseEntity<>(allReservationsMap, HttpStatus.OK);
	}
}
