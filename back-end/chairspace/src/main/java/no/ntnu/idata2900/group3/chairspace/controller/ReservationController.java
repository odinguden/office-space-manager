package no.ntnu.idata2900.group3.chairspace.controller;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.service.ReservationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the reservation feature entity.
 *
 * @see Reservation
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController extends AbstractController<Reservation, UUID> {

	/**
	 * Creates a new reservation controller.
	 *
	 * @param reservationService autowired reservation service.
	 */
	public ReservationController(ReservationService reservationService) {
		super(reservationService);
	}
}
