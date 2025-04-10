package no.ntnu.idata2900.group3.chairspace.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import no.ntnu.idata2900.group3.chairspace.exceptions.InvalidArgumentCheckedException;
import no.ntnu.idata2900.group3.chairspace.exceptions.NotReservableException;
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
		try {
			admin = new User("Admin", "User", "Admin@Email.no");
			nonAdmin = new User("User", "User", "User@Test.tt");
			area = new Area.Builder(
				"Area",
				10,
				new AreaType("Test type", "This is for testing")
				).administrator(admin)
				.build();
		} catch (Exception e) {
			fail("Failed to initialize variables" + e.getMessage(), e);
			return;
		}
	}

	@Test
	void testCreationWithoutComment() {
		LocalDateTime start = LocalDateTime.now().plusDays(10);
		LocalDateTime end = LocalDateTime.now().plusDays(10).plusHours(3);
		Reservation reservation = null;
		try {
			reservation = new Reservation(
				area,
				nonAdmin,
				start,
				end
				);
		} catch (Exception e) {
			fail("Failed to create reservation" + e.getMessage(), e);
			return;
		}
		assertNotNull(reservation);
		assertEquals(area, reservation.getArea(), "Area was not assigned correctly");
		assertEquals(nonAdmin, reservation.getUser(), "User was not assigned correctly");
		assertEquals(start, reservation.getStart(), "Start time was not assigned correctly");
		assertEquals(end, reservation.getEnd(), "End time was not assigned correctly");
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
		} catch (Exception e) {
			fail("Failed to create reservation" + e.getMessage(), e);
			return;
		}
		assertNotNull(reservation);
		assertEquals(area, reservation.getArea(), "Area was not assigned correctly");
		assertEquals(nonAdmin, reservation.getUser(), "User was not assigned correctly");
		assertEquals(start, reservation.getStart(), "Start time was not assigned correctly");
		assertEquals(end, reservation.getEnd(), "End time was not assigned correctly");
		assertEquals(comment, reservation.getComment(), "comment was not assigned correctly");
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
	void testThatBlankCommentDoesNotThrows() {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = LocalDateTime.now().plusDays(1).plusHours(3);
		String comment = "";
		assertDoesNotThrow(
			() -> new Reservation(area, admin, start, end, comment)
		);
	}

	@Test
	void testThatSetStartInPastThrows() {
		LocalDateTime start = LocalDateTime.now().minusDays(1);
		LocalDateTime end = LocalDateTime.now().minusDays(1).plusHours(3);
		String comment = "Finding out what the fox says";
		assertThrows(
			InvalidArgumentCheckedException.class,
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
			InvalidArgumentCheckedException.class,
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
	void testNullArgsForDoesCollideThrows() {
		LocalDateTime start = LocalDateTime.now().plusDays(23432);
		LocalDateTime end = LocalDateTime.now().plusDays(23432).plusHours(3);
		String comment = "Finding out what the fox says";
		Reservation reservation;
		try {
			reservation = new Reservation(
				area,
				nonAdmin,
				start,
				end,
				comment
			);
		} catch (InvalidArgumentCheckedException | ReservedException | NotReservableException e) {
			fail("Failed to create reservation", e);
			return;
		}
		LocalDateTime time = LocalDateTime.now().plusDays(1);
		assertThrows(
			IllegalArgumentException.class,
			() -> reservation.doesCollide(null, time),
			"DoesCollide throws when start is null"
		);
		assertThrows(
			IllegalArgumentException.class,
			() -> reservation.doesCollide(time, null),
			"DoesCollide throws when end is null"
		);
		assertThrows(
			IllegalArgumentException.class,
			() -> reservation.doesCollide(null),
			"DoesCollide throws when timePoint is null"
		);
	}

	@Test
	void testDoesCollideDoesReturnsFalseIfSameEndAndStartIsSame() {
		Reservation reservation;
		LocalDateTime start = LocalDateTime.now().plusDays(27);
		LocalDateTime end = start.plusHours(2);
		LocalDateTime start2 = end;
		LocalDateTime end2 = start2.plusHours(2);
		assertEquals(start2, end);
		try {
			reservation = new Reservation(area, admin, start, end);
		} catch (InvalidArgumentCheckedException | ReservedException | NotReservableException e) {
			fail(e.getMessage(), e);
			return;
		}
		assertFalse(reservation.doesCollide(start2, end2));
	}
}
