package no.ntnu.idata2900.group3.chairspace.dto;

import java.util.UUID;

/**
 * A record containining a simplified version of a user.
 */
public record SimpleUser(
	UUID userId,
	String entraId,
	String fullName,
	String email,
	boolean isAdmin
) {}
