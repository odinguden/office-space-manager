package no.ntnu.idata2900.group3.chairspace.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A container for a list of {@link SimpleReservation}s. Defines the time scope of such a list.
 */
public record SimpleReservationList(
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd'T'HH:mm:ss")
	LocalDateTime scopeStart,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd'T'HH:mm:ss")
	LocalDateTime scopeEnd,
	List<SimpleReservation> reservations
) {}
