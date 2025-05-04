package no.ntnu.idata2900.group3.chairspace.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.assembler.ReservationAssembler;
import no.ntnu.idata2900.group3.chairspace.dto.SimpleReservation;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.NotReservableException;
import no.ntnu.idata2900.group3.chairspace.service.ReservationService;
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
	private final ReservationAssembler reservationAssembler;

	/**
	 * Creates a new reservation controller.
	 *
	 * @param reservationService autowired reservation service
	 * @param reservationAssembler autowired reservation assembler
	 */
	public ReservationController(
		ReservationService reservationService,
		ReservationAssembler reservationAssembler
	) {
		this.reservationService = reservationService;
		this.reservationAssembler = reservationAssembler;
	}

	/**
	 * Gets a single reservation by ID.
	 *
	 * @param id the id of the reservation to get
	 * @return 200 OK with the found reservation, 404 if not found
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SimpleReservation> get(@PathVariable UUID id) {
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
	public ResponseEntity<Page<SimpleReservation>> getAll(
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
	public ResponseEntity<List<SimpleReservation>> getForAreaInTime(
		@PathVariable UUID id,
		@RequestParam LocalDateTime start,
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
	public ResponseEntity<Integer> getReservationFrequency(
		@PathVariable UUID id,
		@RequestParam(required = false) LocalDate date,
		@RequestParam(required = false) Integer year,
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
	public ResponseEntity<List<Integer>> getReservationFrequencyForFullMonth(
		@PathVariable UUID id,
		@RequestParam int year,
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
	public ResponseEntity<UUID> post(@RequestBody SimpleReservation simpleReservation) {
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
	 * Updates an existing reservation.
	 *
	 * @param simpleReservation the simple reservation to use for updating
	 * @return 204 NO CONTENT
	 */
	@PutMapping("")
	public ResponseEntity<String> put(@RequestBody SimpleReservation simpleReservation) {
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
	public ResponseEntity<String> delete(@PathVariable UUID id) {
		this.hasPermissionToDelete();
		reservationService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Gets all reservations belonging to a given user.
	 *
	 * @param userId the id of the user to get the reservations of
	 * @param page the page of the pagination to retrieve
	 * @param size the size of the page to retrieve
	 * @return reservations belonging to the user in a paginated format
	 * @throws ResponseStatusException 400 BAD_REQUEST if the userId is null or the page is negative
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<Page<SimpleReservation>> getReservationsByUser(
		@PathVariable UUID userId,
		@RequestParam(required = false) Integer page,
		@RequestParam(required = false) Integer size
	) {
		this.hasPermissionToGet();
		Page<Reservation> reservations = reservationService.getReservationsByUserPaged(
			userId,
			page,
			size
		);
		Page<SimpleReservation> simpleReservations = reservations.map(
			reservationAssembler::toSimple
		);
		return new ResponseEntity<>(simpleReservations, HttpStatus.OK);
	}
}
