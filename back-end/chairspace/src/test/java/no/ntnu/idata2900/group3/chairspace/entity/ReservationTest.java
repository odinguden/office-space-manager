package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import no.ntnu.idata2900.group3.chairspace.exceptions.ReservedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test for the Reservation entity.
 * The tests in this class covers creation and basic methods of a reservation.
 * Both negative tests and positive tests are included.
 *
 * @author Odin Lyngsgård
 * @version 0.1
 * @since 0.1
 * @see Reservation
 */
class ReservationTest {
	private static User admin;
	private static User nonAdmin;
	private static Area area;

	@BeforeAll
	static void initialize() {
		admin = new User.Builder("Admin", "User")
			.email("Admin@Test.tt")
			.build();
		nonAdmin = new User.Builder("User", "User")
			.email("User@Test.tt")
			.build();
		area = new Area.Builder(
			"Area",
			10,
			new AreaType("Test type", "This is for testing")
			).administrator(admin)
			.build();
	}

	@Test
	void testCreation() {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = LocalDateTime.now().plusDays(1).plusHours(3);
		String comment = "Writing tests";
		Reservation reservation = null;
		try {
			reservation = new Reservation(
				area,
				nonAdmin,
				start,
				end,
				comment
				);
		} catch (ReservedException e) {
			e.printStackTrace();
		}
		assertNotNull(reservation);
		assertEquals(area, reservation.getArea(), "Area was not assigned correctly");
		assertEquals(nonAdmin, reservation.getUser(), "User was not assigned correctly");
		assertEquals(start, reservation.getStart(), "Start time was not assigned correctly");
		assertEquals(end, reservation.getEnd(), "End time was not assigned correctly");
		assertEquals(comment, reservation.getComment(), "comment was not assigned correctly");
		area.removeReservation(reservation);
	}

	@Test
	void testThatNullAreaThrows() {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = LocalDateTime.now().plusDays(1).plusHours(3);
		String comment = "Writing tests";
		assertThrows(
			IllegalArgumentException.class,
			() -> new Reservation(null, admin, start, end, comment)
		);
	}

	@Test
	void testThatNullUserThrows() {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = LocalDateTime.now().plusDays(1).plusHours(3);
		String comment = "Writing tests";
		assertThrows(
			IllegalArgumentException.class,
			() -> new Reservation(area, null, start, end, comment)
		);
	}

	@Test
	void testThatNullStartThrows() {
		LocalDateTime end = LocalDateTime.now().plusDays(1).plusHours(3);
		String comment = "Writing tests";
		assertThrows(
			IllegalArgumentException.class,
			() -> new Reservation(area, admin, null, end, comment)
		);
	}

	@Test
	void testThatNullEndThrows() {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		String comment = "Writing tests";
		assertThrows(
			IllegalArgumentException.class,
			() -> new Reservation(area, admin, start, null, comment)
		);
	}

	@Test
	void testThatNullCommentThrows() {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = LocalDateTime.now().plusDays(1).plusHours(3);
		assertThrows(
			IllegalArgumentException.class,
			() -> new Reservation(area, admin, start, end, null)
		);
	}

	@Test
	void testThatBlankCommentThrows() {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = LocalDateTime.now().plusDays(1).plusHours(3);
		String comment = "";
		assertThrows(
			IllegalArgumentException.class,
			() -> new Reservation(area, admin, start, end, comment)
		);
	}

	@Test
	void testThatCreatingOverlappingReservationsThrows() {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = LocalDateTime.now().plusDays(1).plusHours(3);
		String comment = "Writing tests";
		try {
			new Reservation(
				area,
				nonAdmin,
				start,
				end,
				comment
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LocalDateTime newStart = start.plusHours(2);
		LocalDateTime newEnd = end.plusHours(2);
		assertThrows(
			IllegalStateException.class,
			() -> new Reservation(
				area,
				nonAdmin,
				newStart,
				newEnd,
				comment
				)
		);
	}

	@Test
	void testThatSetStartInPastThrows() {
		LocalDateTime start = LocalDateTime.now().minusDays(1);
		LocalDateTime end = LocalDateTime.now().minusDays(1).plusHours(3);
		String comment = "Finding out what the fox says";
		assertThrows(
			IllegalStateException.class,
			() -> new Reservation(
				area,
				nonAdmin,
				start,
				end,
				comment
				)
		);
	}

	@Test
	void testThatSetEndBeforeStartThrows() {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = LocalDateTime.now().plusDays(1).minusHours(3);
		String comment = "Finding out what the fox says";
		LocalDateTime newStart = start.plusHours(2);
		LocalDateTime newEnd = end.plusHours(2);
		assertThrows(
			IllegalStateException.class,
			() -> new Reservation(
				area,
				nonAdmin,
				newStart,
				newEnd,
				comment
				)
		);
	}
}
