<script setup>
import { useDate } from 'vuetify'

const vDate = useDate()

const mockTimeline = [
	{
		type: "gap",
		start: 0,
		duration: 0.2
	},
	{
		type: "event",
		start: 0.2,
		duration: 0.1
	},
	{
		type: "gap",
		start: 0.3,
		duration: 0.05
	},
	{
		type: "event",
		start: 0.35,
		duration: 0.35
	},
	{
		type: "gap",
		start: 0.7,
		duration: 0.2
	},
	{
		type: "event",
		start: 0.9,
		duration: 0.1
	}
]

const mockReservation = {
	start: new Date().setSeconds(0),
	duration: 12 * 60,
	timeline: mockTimeline
}

function getStart(reservation) {
	return Math.max(reservation.start, 0)
}

function getEnd(reservation) {
	return Math.min(reservation.duration, 1 - reservation.start)
}

function formatTime(time) {
	return `${vDate.format(time, "fullTime24h")}`// `${vDate.format(time, "hours24h")}:${vDate.format(time, "minutes")}`
}

function getTooltip(reservation) {
	let tip = reservation.type === "gap" ? "Free from" : "Booked from";

	const startMinutes = reservation.start * mockReservation.duration
	const startTime = vDate.addMinutes(mockReservation.start, startMinutes)
	const durationMinutes = Math.round(reservation.duration * mockReservation.duration)
	const endTime = vDate.addMinutes(startTime, durationMinutes)

	tip += ` ${formatTime(startTime)} until ${formatTime(endTime)} (${durationMinutes} minutes)`

	return tip;
}
</script>

<template>
	<div class="timeline-container">
		<v-tooltip v-for="reservation in mockReservation.timeline">
			<template
				v-slot:activator="{ props }"
			>
				<div
					v-bind="props"
					class="timeline-component"
					:class="{
						'event': reservation.type === 'event',
						'gap': reservation.type === 'gap'
					}"
					:style="{
						'--reservation-start': getStart(reservation),
						'--reservation-length': getEnd(reservation)
					}"
				/>
			</template>
			<span>
				{{ getTooltip(reservation) }}
			</span>
		</v-tooltip>
	</div>
</template>

<style scoped lang="scss">
.timeline-container {
	position: relative;
	width: 100%;
	height: 100%;

	padding: 4px;

	display: flex;
	background-color: rgba(var(--v-theme-on-surface), 0.1);
	border: 1px solid rgba(var(--v-border-color), var(--v-border-opacity));

	.timeline-component {
		flex-grow: var(--reservation-length);

		&.event {
			background-color: rgb(var(--v-theme-error));
			border: 2px solid rgba(var(--v-border-color), 0.33);
		}
	}

	&[vertical] {
		flex-direction: column;
	}
}
</style>