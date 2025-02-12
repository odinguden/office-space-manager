package no.ntnu.idata2900.group3.chairspace.repository;

import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Reservation entity.
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, UUID> {}