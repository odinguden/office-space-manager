<script setup>
import fetcher from '@/plugins/fetcher';
import { useODate } from '@/plugins/oDate';
import { watch } from 'vue';
import { useDate } from 'vuetify';
const props = defineProps({
	year: Number,
	month: Number,
	area: Object,
	clickableDays: Boolean
})

const vDate = useDate()
const oDate = useODate()

const selectedWeek = ref(undefined)
const frequencies = ref([])
const timelines = ref([])

function getFrequencies() {
	fetcher.getReservationFrequencyForMonth(props.area.id, props.year, props.month + 1)
		.then(response => frequencies.value = response)
}

const weeks = computed(() => {
	return oDate.getWeeksOfMonth(props.year, props.month)
})

function getTimelines(week) {
	for (let [idx, day] of week.days.entries()) {
		const startOfDay = new Date(day)
		startOfDay.setHours(0, 0, 0, 0)
		const endOfDay = new Date(day)
		endOfDay.setHours(23, 59, 59, 999)
		fetcher.getReservationsForAreaInTime(props.area.id, startOfDay, endOfDay)
			.then(response => timelines.value[idx] = {
				reservations: response,
				scopeStart: startOfDay,
				scopeEnd: endOfDay
			})
	}
}

function onWeekClicked(week) {
	if (selectedWeek.value === undefined) {
		selectedWeek.value = week
		getTimelines(week)
	} else {
		timelines.value = []
		selectedWeek.value = undefined
	}
}

function getIsDayAvailable(day) {
	if (!props.area.isPlanControlled) {
		return true
	}

	let isPlanOpened = false

	for (let plan of props.area.plans) {
		const planStart = new Date(plan.start)
		const planEnd = new Date(plan.end)

		if (
			(vDate.isEqual(planStart, planEnd) && vDate.isEqual(day, planStart))
			||
			(vDate.isAfter(planStart, day) && vDate.isBefore(planEnd, day))
		) {
			isPlanOpened = true
		}
	}

	return isPlanOpened;
}

watch(props, () => selectedWeek.value = undefined)
getFrequencies()
</script>

<template>
	<v-sheet v-if="selectedWeek==undefined">
		<o-clickable-week
			v-for="week in weeks"
			:key="week"
			:week="week"
			:month="month"
			:frequencies="frequencies"
			:area="area"
			@week-clicked="onWeekClicked(week)"
		/>
	</v-sheet>
	<v-sheet v-else>
		<o-clickable-week
			:week="selectedWeek"
			:month="month"
			:frequencies="frequencies"
			:area="area"
			clickable-days
			@day-clicked="$emit('day-clicked', $event)"
		>
			<template #week-number>
				<v-btn
					style="height: 100%; width: 100%;"
					flat
					icon
					tile
					density="compact"
					@click="onWeekClicked"
				>
					<v-icon>
						mdi-chevron-left
					</v-icon>
				</v-btn>
			</template>
		</o-clickable-week>
		<div class="timeline-section">
			<div />
			<div
				v-for="day, idx in selectedWeek.days"
			>
				<o-timeline
					v-if="timelines[idx] != undefined"
					:reservations="timelines[idx].reservations"
					:scope-start="timelines[idx].scopeStart"
					:scope-end="timelines[idx].scopeEnd"
					:disabled="!getIsDayAvailable(day)"
					vertical
				/>
				<div v-else />
			</div>
		</div>
	</v-sheet>
</template>

<style scoped lang="scss">
.timeline-section {
	display: grid;
	grid-auto-flow: column;
	grid-auto-columns: 1fr;

	> * {
		aspect-ratio: 1 / 5;
		padding: 0 4px;

		&:first-child {
			border-right: 1px solid rgba(var(--v-border-color), var(--v-border-opacity));
		}
	}
}
</style>