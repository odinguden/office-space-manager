<script setup>
import { computed } from 'vue'
import { useDate } from 'vuetify'

const vDate = useDate()

const props = defineProps({
	reservations: Array,
	scopeStart: Date,
	scopeEnd: Date
})

console.log(props.scopeStart, props.scopeEnd)

const scopeDuration = computed(() => {
	return props.scopeEnd.getTime() - props.scopeStart.getTime()
})

const timeline = computed(() => {
	const timeline = []

	const scopeStartMs = props.scopeStart.getTime()
	let prevTime = 0;

	for (let reservation of props.reservations) {
		const startTime = Date.parse(reservation.startTime) - scopeStartMs
		const endTime = Date.parse(reservation.endTime) - scopeStartMs
		const startPercent = startTime / scopeDuration.value
		const endPercent = endTime / scopeDuration.value
		const prevTimePercent = (prevTime) / scopeDuration.value
		console.log(startPercent, endPercent, endPercent-startPercent)

		if (prevTime < startTime) {
			timeline.push({
				type: 'gap',
				startPercent: prevTimePercent,
				endPercent: startPercent,
				durationPercent: startPercent - prevTimePercent,
				startDate: new Date(prevTime + scopeStartMs),
				endDate: new Date(startTime + scopeStartMs)
			})
		}

		timeline.push({
			type: 'event',
			startPercent,
			endPercent,
			durationPercent: endPercent - startPercent,
			startDate: new Date(startTime + scopeStartMs),
			endDate: new Date(endTime + scopeStartMs)
		})

		prevTime = endTime
	}

	if (prevTime < props.scopeEnd.getTime()) {
		const startPercent = prevTime / scopeDuration.value
		const endPercent = 1
		timeline.push({
			type: 'gap',
			startPercent: startPercent,
			endPercent: endPercent,
			durationPercent: endPercent - startPercent,
			startDate: new Date(prevTime + scopeStartMs),
			endDate: new Date(props.scopeEnd.getTime())
		})
	}

	return timeline
})

console.log(timeline.value)


function getStart(reservation) {
	return Math.max(reservation.startPercent, 0)
}

function getEnd(reservation) {
	return Math.min(reservation.durationPercent, 1 - reservation.startPercent)
}

function formatTime(time) {
	return `${vDate.format(time, "fullTime24h")}`// `${vDate.format(time, "hours24h")}:${vDate.format(time, "minutes")}`
}

function getTooltip(reservation) {
	const tip = `${formatTime(reservation.startDate)} - ${formatTime(reservation.endDate)}`
	return tip;
}
</script>

<template>
	<div class="timeline-container">
		<v-tooltip v-for="reservation in timeline">
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