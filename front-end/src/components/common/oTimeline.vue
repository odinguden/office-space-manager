<script setup>
import { computed } from 'vue'
import { useDate } from 'vuetify'

const vDate = useDate()

const props = defineProps({
	reservations: Array,
	scopeStart: Date,
	scopeEnd: Date,
	disabled: Boolean
})

const selectedReservation = ref(null)

const timeline = computed(() => {
	const timeline = []

	const startScope = props.scopeStart.getTime()
	const endScope = props.scopeEnd.getTime()
	const totalMs = endScope - startScope

	let lastEnd = new Date(startScope)

	for (let reservation of props.reservations) {
		let start = new Date(reservation.startTime)
		let end = new Date(reservation.endTime)

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
			isMine: reservation.isMine,
			data: reservation
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

const isModalOpen = computed({
	get: () => selectedReservation.value != null,
	set: () => selectedReservation.value = null
})

function setReservation(reservationSegment) {
	if (reservationSegment.type === 'event') {
		selectedReservation.value = reservationSegment.data
	}
}
</script>

<template>
	<div class="timeline-container" :class="{ disabled }">
		<div class="timeline-inner-container">
			<v-tooltip v-for="reservation in timeline" :disabled="disabled">
				<template
					v-slot:activator="{ props }"
				>
					<div
						v-bind="props"
						class="timeline-component"
						:class="{
							'event': reservation.type === 'event',
							'gap': reservation.type === 'gap',
							'mine': reservation.isMine,
							'disabled': disabled
						}"
						:style="{
							'--reservation-length': getDuration(reservation)
						}"
						:disabled="disabled"
						@click.prevent="setReservation(reservation)"
					/>
				</template>
				<div class="tooltip-grid">
					<span>{{ vDate.format(reservation.start, "shortDate") }}</span>
					<span> - </span>
					<span>{{ vDate.format(reservation.end, "shortDate") }}</span>

					<span>{{ vDate.format(reservation.start, "fullTime24h").substring(0,5) }}</span>
					<span> - </span>
					<span>{{ vDate.format(reservation.end, "fullTime24h").substring(0,5) }}</span>
				</div>
			</v-tooltip>
		</div>
		<o-reservation-modal
			v-model="isModalOpen"
			:reservation="selectedReservation"
		/>
	</div>
</template>

<style scoped lang="scss">
.timeline-container {
	width: 100%;
	height: 100%;

	padding: 6px;

	border-radius: 100vmax;

	background-color: rgba(var(--v-theme-on-surface), 0.1);

	box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.2);

	.timeline-inner-container {
		display: flex;
		gap: 1px;
		width: 100%;
		height: 100%;
		border-radius: 100vmax;
		overflow: hidden;
	}

	&.disabled {
		background-color: rgba(var(--v-theme-on-surface), 0.3)
	}

	.timeline-component {
		flex-grow: var(--reservation-length);

		&.event {
			background-color: rgb(var(--v-theme-error));
			border-color: rgb(var(--v-theme-error));
			border: 2px solid;
			cursor: pointer;

			&.mine {
				background-color: rgb(var(--v-theme-blue));
				border-color: rgb(var(--v-theme-blue));
			}
		}
	}

	&[vertical] {
		border-radius: 0;
		.timeline-inner-container {
			flex-direction: column;
			border-radius: 0;
		}
	}
}

.tooltip-grid {
	display: grid;
	grid-template-columns: 1fr auto 1fr;
	gap: 0px 8px;
	align-items: center;
}
</style>