package no.ntnu.idata2900.group3.chairspace.controllers;

import java.security.AuthProvider;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	private ReservationRepository reservationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AreaRepository areaRepository;


	@GetMapping("")
	public ResponseEntity<List<ReservationDto>> getAllReservation() {
		Iterator<Reservation> it = reservationRepository.findAll().iterator();
		List<ReservationDto> reservations = new ArrayList<>();
		while (it.hasNext()) {
			reservations.add(
				new ReservationDto(
					it.next()
				)
			);
		}
		return new ResponseEntity<>(reservations, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public String getMethodName(@PathVariable UUID id) {
		return new String();
	}
	
	@PostMapping("")
	public ResponseEntity<String> createNewReservation(@RequestBody ReservationCreationDto reservationCreationDto) {
		Reservation reservation = createReservationFromDto(reservationCreationDto);
		reservationRepository.save(reservation);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("path/{id}")
	public String putMethodName(@PathVariable String id, @RequestBody String entity) {
		//TODO: process PUT request

		return entity;
	}

	@DeleteMapping
	public ResponseEntity<String> deleteReservation() {
		return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
	}

	// ---- Create Reservation ----

	private Reservation createReservationFromDto(ReservationCreationDto reservationCreationDto) {
		String comment = reservationCreationDto.getComment();
		LocalDateTime startTime = reservationCreationDto.getStartTime();
		LocalDateTime endTime = reservationCreationDto.getEndTime();
		Area area = getArea(reservationCreationDto.getArea());
		User user = getUser(reservationCreationDto.getUser());
		Reservation reservation;
		try {
			reservation = new Reservation(area, user, startTime, endTime, comment);
		} catch (InvalidArgumentCheckedException e) {
			throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);
		} catch (ReservedException e) {
			throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);
		} catch (NotReservableException e) {
			throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);
		}
		return reservation;
	}

	private User getUser(UUID id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Could not find user with id: " + id.toString()
				+ ". Unable to create reservation with this user."
			);
		}
		return optionalUser.get();
	}

	private Area getArea(UUID id) {
		Optional<Area> optionalArea = areaRepository.findById(id);
		if (!optionalArea.isPresent()) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Could not find area with id: " + id.toString()
				+ ". Unable to create reservation with this area."
			);
		}
		return optionalArea.get();
	}
}
