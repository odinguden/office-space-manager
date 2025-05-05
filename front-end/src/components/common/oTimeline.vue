<script setup>
import { computed } from 'vue'
import { useDate } from 'vuetify'

const vDate = useDate()

const props = defineProps({
	reservations: Array,
	scopeStart: Date,
	scopeEnd: Date
})

const timeline = computed(() => {
	const timeline = []

	const startScope = props.scopeStart.getTime()
	const endScope = props.scopeEnd.getTime()
	const totalMs = endScope - startScope

	let lastEnd = new Date(startScope)

	for (let reservation of props.reservations) {
		const start = new Date(reservation.startTime)
		const end = new Date(reservation.endTime)

		if (start > lastEnd) {
			timeline.push({
				type: 'gap',
				start: lastEnd,
				end: start,
				startPercent: (lastEnd - startScope) / totalMs,
				durationPercent: (start - lastEnd) / totalMs
			})
		}

		timeline.push({
			type: 'event',
			start,
			end,
			startPercent: (start - startScope) / totalMs,
			durationPercent: (end - start) / totalMs,
			isMine: reservation.isMine
		})

		lastEnd = end
	}

	if (lastEnd < endScope) {
		timeline.push({
			type: 'gap',
			start: lastEnd,
			end: new Date(endScope),
			startPercent: (lastEnd - startScope) / totalMs,
			durationPercent: (endScope - lastEnd) / totalMs
		})
	}

	return timeline
})

function getDuration(reservation) {
	const start = reservation.startPercent
	const end = start + reservation.durationPercent

	const duration = Math.min(end, 1) - Math.max(start, 0)

	return Math.max(duration, 0);
}

function formatTime(time) {
	return `${vDate.format(time, "fullTime24h")}`// `${vDate.format(time, "hours24h")}:${vDate.format(time, "minutes")}`
}

function getTooltip(reservation) {
	const tip = `${formatTime(reservation.start)} - ${formatTime(reservation.end)}`
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
						'gap': reservation.type === 'gap',
						'mine': reservation.isMine
					}"
					:style="{
						'--reservation-length': getDuration(reservation)
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

			&.mine {
				background-color: rgb(var(--v-theme-blue));
			}
		}
	}

	&[vertical] {
		flex-direction: column;
	}
}
</style>