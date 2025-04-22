package no.ntnu.idata2900.group3.chairspace.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Immutable DTO for searching areas.
 */
public class SearchDto {
	private int capacity;
	private String areaType;
	private Set<String> areaFeatures;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;

	/* ---- Getters ---- */

	public int getCapacity() {
		return capacity;
	}
	public String getAreaType() {
		return areaType;
	}
	public Set<String> getAreaFeatures() {
		return areaFeatures;
	}
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}
	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}


	
}
