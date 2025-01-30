package no.ntnu.idata2900.group3.chairspace.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Area area;
	private User user;
	private LocalDateTime start;
	private LocalDateTime end;
	private String comment;

	private Reservation() {}

	/* ---- Setters ---- */

	public void setId(int id) {
		this.id = id;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setStart(LocalDateTime start) {
		this.start = start;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	/* ---- Getters ---- */

	public int getId() {
		return id;
	}
	public Area getArea() {
		return area;
	}
	public User getUser() {
		return user;
	}
	public LocalDateTime getStart() {
		return start;
	}
	public LocalDateTime getEnd() {
		return end;
	}
	public String getComment() {
		return comment;
	}
}
