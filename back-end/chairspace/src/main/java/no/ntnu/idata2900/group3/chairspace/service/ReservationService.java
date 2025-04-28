package no.ntnu.idata2900.group3.chairspace.service;

import java.util.List;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import no.ntnu.idata2900.group3.chairspace.repository.ReservationRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link Reservation}s.
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@Service
public class ReservationService extends EntityService<Reservation, UUID> {
	/**
	 * Creates a new user service.
	 *
	 * @param repository autowired UserRepository
	 */
	public ReservationService(ReservationRepository repository) {
		super(repository);
	}

	/**
	 * Gets this service's repository as a reservation repository.
	 *
	 * @return this service's repository as a reservation repository
	 */
	public ReservationRepository getRepository() {
		return (ReservationRepository) this.repository;
	}

	/**
	 * Gets all reservations belonging to a given area.
	 *
	 * @param areaId the id to get reservations from
	 * @return all reservations belonging to a given area
	 */
	public List<Reservation> getReservationsForArea(UUID areaId) {
		ReservationRepository reservationRepo = getRepository();

		return reservationRepo.findAllByAreaIdOrderByStartDateTimeAsc(areaId);
	}
}
