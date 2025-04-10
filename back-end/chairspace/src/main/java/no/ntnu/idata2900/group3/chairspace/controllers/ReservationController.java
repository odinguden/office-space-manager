package no.ntnu.idata2900.group3.chairspace.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.reservation.ReservationCreationDto;
import no.ntnu.idata2900.group3.chairspace.dto.reservation.ReservationDto;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.NotReservableException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ReservedException;
import no.ntnu.idata2900.group3.chairspace.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 * Reservation controller.
 */
@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/reservation")
public class ReservationController extends AbstractPermissionManager {
	@Autowired
	private ReservationService reservationService;

	/**
	 * Gets the reservation with the corresponding ID.
	 *
	 * @param id the id of the reservation to fetch
	 * @return 200 OK with the found reservation
	 */
	@GetMapping("/{id}")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Found a reservation with the given id"
			)
	})
	public ResponseEntity<ReservationDto> getReservation(@PathVariable UUID id) {
		super.hasPermissionToGet();
		ReservationDto reservationDto = reservationService.getReservationById(id);
		return new ResponseEntity<>(reservationDto, HttpStatus.OK);
	}

	/**
	 * Gets a list of all reservations for an area within the given timeframe.
	 *
	 * @param areaId the id of the area for which to get reservations
	 * @param start the start of the time slot to check, defaults to the current time
	 * @param end the end of the time slot to check, defaults to 12 hours from start
	 * @return 200 OK with a list of reservations for the given time period
	 */
	@GetMapping("/area/{areaId}")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Collected a list of all reservations within the given timeframe"
			)
	})
	public ResponseEntity<List<ReservationDto>> getReservationsInTimeFrame(
		@PathVariable UUID areaId,
		@RequestParam LocalDateTime start,
		@RequestParam LocalDateTime end) {
		if (start == null) {
			start = LocalDateTime.now();
		}
		if (end == null) {
			end = start.plusHours(12);
		}

		return new ResponseEntity<>(
			reservationService.getReservationsForAreaInTimePeriod(areaId, start, end),
			HttpStatus.OK
		);
	}

	/**
	 * Attempts to create a new reservation based on a DTO.
	 *
	 * @param creationDto a simple DTO containing information about the reservation to be created
	 * @return 201 CREATED if the reservation was created
	 * @throws ResponseStatusException 400 BAD REQUEST if the creation DTO contains invalid values
	 * @throws ResponseStatusException 400 BAD REQUEST if the reservation to be created is for an
	 *     area that cannot be reserved
	 * @throws ResponseStatusException 409 CONFLICT if the reservation to be created overlaps
	 *     with an existing reservation
	 */
	@PostMapping("")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Succesfully created the reservation"
			),
		@ApiResponse(
			responseCode = "400",
			description = "If the creation parameters contain invalid values"
			),
		@ApiResponse(
			responseCode = "400",
			description = "If the area to be reserved does not allow reservations"
			),
		@ApiResponse(
			responseCode = "409",
			description = "If the reservation would overlap with an existing one"
			)
	})
	public ResponseEntity<String> postMethodName(@RequestBody ReservationCreationDto creationDto) {
		super.hasPermissionToPost();

		try {
			reservationService.createReservationByCreationDto(creationDto);
		} catch (InvalidArgumentCheckedException | NotReservableException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (ReservedException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Attempts to delete the reservation with the given ID.
	 *
	 * @param id the id of the reservation to be deleted
	 * @return 204 NO CONTENT if the reservation was deleted
	 * @throws ResponseStatusException 404 NOT FOUND if the id does not exist in the database
	 */
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Successfully deleted the reservation"
			),
		@ApiResponse(
			responseCode = "404",
			description = "If the reservation to be deleted does not exist"
			)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteReservation(@PathVariable UUID id) {
		// FIXME: Ensure that reservations can only be deleted by owning or administrating users
		super.hasPermissionToDelete();

		if (!reservationService.deleteReservation(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
