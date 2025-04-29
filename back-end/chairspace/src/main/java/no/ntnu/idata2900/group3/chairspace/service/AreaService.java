package no.ntnu.idata2900.group3.chairspace.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.repository.AreaRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link Area}s.
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@Service
public class AreaService extends EntityService<Area, UUID> {
	AreaRepository areaRepository;
	ReservationService reservationService;

	/**
	 * Creates a new area service.
	 *
	 * @param repository autowired AreaRepository
	 * @param reservationService autowired reservationService
	 */
	public AreaService(AreaRepository repository, ReservationService reservationService) {
		super(repository);
		this.areaRepository = repository;
		this.reservationService = reservationService;
	}

	@Override
	// Override to ensure areas cannot be saved with themselves as their super area
	protected boolean save(Area area) {
		if (area.isSuperArea(area)) {
			return false;
		}

		return super.save(area);
	}

	/**
	 * Retrieves all areas that have a gap like or longer than the input duration.
	 *
	 * @param searchStart the time to start the search from
	 * @param searchEnd the time to end the search at
	 * @param minGapSize the minimum size of a gap for it to be counted
	 * @return a list of all areas that meet the gap requirements
	 */
	public List<Area> getAreasWithFreeGapLike(
		LocalDateTime searchStart,
		LocalDateTime searchEnd,
		Duration minGapSize
	) {
		Iterable<Area> reservableAreas = this.areaRepository.findAllByReservable(true);
		Set<Area> areasWithGap = new HashSet<>();
		reservableAreas.iterator().forEachRemaining(areasWithGap::add);
		Iterator<Area> reservableAreasIterator = reservableAreas.iterator();

		while (reservableAreasIterator.hasNext()) {
			Area area = reservableAreasIterator.next();
			boolean hasGap = reservationService.doesAreaHaveFreeGapLike(
				area.getId(),
				searchStart,
				searchEnd,
				minGapSize
			);

			if (!hasGap) {
				// Assume areas have gap until proven otherwise
				areasWithGap.remove(area);
			}
		}

		return List.copyOf(areasWithGap);
	}
}
