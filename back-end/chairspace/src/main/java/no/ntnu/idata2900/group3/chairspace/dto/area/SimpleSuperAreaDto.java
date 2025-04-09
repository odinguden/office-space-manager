package no.ntnu.idata2900.group3.chairspace.dto.area;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import no.ntnu.idata2900.group3.chairspace.entity.Area;

/**
 * A simple DTO to be used for the super areas contained within this dto.
 * This is used to avoid including irrelevant data about
 * super areas when requesting a single area.
 */
public class SimpleSuperAreaDto {
	@JsonProperty
	private UUID id;
	@JsonProperty
	private String name;
	@JsonProperty
	private String areaType;

	/**
	 * Creates a simple super area dto based on an existing area.
	 *
	 * @param superArea the area to use as base
	 */
	public SimpleSuperAreaDto(Area superArea) {
		this.id = superArea.getId();
		this.name = superArea.getName();
		this.areaType = superArea.getAreaType().getId();
	}

	/**
	 * no args constructor.
	 */
	public SimpleSuperAreaDto() {
		// No args constructor for serialization
	}

	/**
	 * Returns the name of the super area.
	 *
	 * @return returns the name of the super area
	 */
	public UUID getId() {
		return id;
	}
}