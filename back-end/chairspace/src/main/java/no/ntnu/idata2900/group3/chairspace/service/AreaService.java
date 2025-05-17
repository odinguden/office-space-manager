package no.ntnu.idata2900.group3.chairspace.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;
import no.ntnu.idata2900.group3.chairspace.repository.AreaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with and managing {@link Area}s.
 *
 * @author Odin Lyngsgård
 * @author Sigve Bjørkedal
 */
@Service
public class AreaService extends EntityService<Area, UUID> {
	private final AreaRepository areaRepository;
	private final ReservationService reservationService;
	private final PlanService planService;

	/**
	 * Creates a new area service.
	 *
	 * @param repository autowired AreaRepository
	 * @param reservationService autowired reservationService
	 * @param planService autowired planService
	 */
	public AreaService(
		AreaRepository repository,
		ReservationService reservationService,
		PlanService planService
	) {
		super(repository);
		this.areaRepository = repository;
		this.reservationService = reservationService;
		this.planService = planService;
	}

	@Override
	// Override to ensure areas cannot be saved with themselves as their super area
	protected UUID save(Area area) {
		if (area.isSuperArea(area)) {
			return null;
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
		List<Area> reservableAreas =
				this.areaRepository.findAllByPlanControlledOrReservable(true, true);

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
			) && (
				!area.isPlanControlled()
				|| planService.isFree(area.getId(), searchStart, searchEnd)
			);

			if (!hasGap) {
				// Assume areas have gap until proven otherwise
				areasWithGap.remove(area);
			}
		}

		return List.copyOf(areasWithGap);
	}

	/**
	 * Searches for areas based on the given parameters.
	 * If any of the parameters are null, they will be ignored in the search.
	 *
	 * <p>
	 * Note that this method does not check for the validity of the parameters.
	 * If any of the parameters are invalid, or does otherwise not exist in the database,
	 * the method will return nothing.
	 *
	 * @param page the page to get
	 * @param capacity the minimum capacity of the area
	 * @param superAreaId the id of the super area to search in
	 * @param areaTypeId the id of the area type to search for
	 * @param areaFeatureIds the ids of the area features to search for
	 * @param freeAreaIds the ids of the free areas to search for
	 * @return an iterable of areas that match the search criteria
	 */
	public Page<Area> searchWithOptionalParams(
		int page,
		Integer capacity,
		UUID superAreaId,
		String areaTypeId,
		List<String> areaFeatureIds,
		List<UUID> freeAreaIds
	) {
		Pageable paging = PageRequest.of(page, EntityService.DEFAULT_PAGE_SIZE);
		List<UUID> subAreaIds = null;
		if (superAreaId != null) {
			subAreaIds = getSubAreas(superAreaId);
		}
		return areaRepository.searchWithOptionalParams(
			paging,
			capacity,
			subAreaIds,
			areaTypeId,
			areaFeatureIds,
			freeAreaIds,
			areaFeatureIds != null ? areaFeatureIds.size() : 0
		);
	}

	/**
	 * Gets all sub areas of a given area.
	 * Will return an empty list if the given id is null, or if the area does not exist.
	 *
	 * @param superAreaId the id of the area to get sub areas from
	 * @return a list of sub areas of the given area
	 */
	public List<UUID> getSubAreas(UUID superAreaId) {
		if (superAreaId == null) {
			return List.of();
		}
		List<UUID> subAreaIds = new ArrayList<>();
		Deque<UUID> areaStack = new ArrayDeque<>();
		Set<UUID> visited = new HashSet<>();
		areaStack.add(superAreaId);

		while (!areaStack.isEmpty()) {
			UUID node = areaStack.pop();
			if (visited.add(node)) {
				List<UUID> children = areaRepository.findIdBySuperAreaId(node);
				areaStack.addAll(children);
				subAreaIds.addAll(children);
			}
		}

		return subAreaIds;
	}

	/**
	 * Gets all areas that have this user as an admin.
	 *
	 * @param userId the user to find areas for
	 * @param page the page to get
	 * @param size the amount of entries per page
	 * @return a page of areas that have this user as an admin
	 */
	public Page<Area> getAreasByUser(UUID userId, Integer page, Integer size) {
		if (page == null || page < 0) {
			page = 0;
		}
		if (size == null || size < 0) {
			size = DEFAULT_PAGE_SIZE;
		}
		Pageable paging = PageRequest.of(page, size);
		return areaRepository.findByAdministrators_Id(userId, paging);
	}

	/**
	 * Gets all areas that are administrated by a user.
	 *
	 * @param userId the user to get areas for
	 * @return a list of all areas administrated by a user
	 */
	public List<Area> getAreasByUserAsList(UUID userId) {
		return areaRepository.findByAdministrators_Id(userId);
	}

	public List<Area> getSuperAreasByName(String name) {
		Pageable limit20 = PageRequest.of(0, 20);
		return areaRepository.findSuperAreasByName(name, limit20).getContent();
	}
}
