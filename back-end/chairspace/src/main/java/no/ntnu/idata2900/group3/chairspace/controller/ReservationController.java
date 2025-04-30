package no.ntnu.idata2900.group3.chairspace.controller;

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

}
