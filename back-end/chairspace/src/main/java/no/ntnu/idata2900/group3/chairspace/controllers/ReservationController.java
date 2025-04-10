package no.ntnu.idata2900.group3.chairspace.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.dto.reservation.ReservationCreationDto;
import no.ntnu.idata2900.group3.chairspace.dto.reservation.ReservationDto;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.entity.User;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.NotReservableException;
import no.ntnu.idata2900.group3.chairspace.exceptions.ReservedException;
import no.ntnu.idata2900.group3.chairspace.repository.AreaRepository;
import no.ntnu.idata2900.group3.chairspace.repository.ReservationRepository;
import no.ntnu.idata2900.group3.chairspace.repository.UserRepository;
import no.ntnu.idata2900.group3.chairspace.service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestParam;





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
	public ResponseEntity<ReservationDto> getReservation(@PathVariable UUID id) {
		super.hasPermissionToGet();
		ReservationDto reservationDto = reservationService.getReservationById(id);
		return new ResponseEntity<>(reservationDto, HttpStatus.OK);
	}

	@PostMapping("")
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
}
