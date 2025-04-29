package no.ntnu.idata2900.group3.chairspace.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A container for a list of {@link SimpleReservation}s. Defines the time scope of such a list.
 */
public record SimpleReservationList(
	LocalDateTime scopeStart,
	LocalDateTime scopeEnd,
	List<SimpleReservation> reservations
) {
	
}
