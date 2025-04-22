<script setup>
import { useODate } from '@/plugins/oDate';
import { watch } from 'vue';
const props = defineProps({
	year: Number,
	month: Number,
	clickableDays: Boolean
})

const oDate = useODate()

const selectedWeek = ref(undefined)

const weeks = computed(() => {
	return oDate.getWeeksOfMonth(props.year, props.month)
})

function onWeekClicked(week) {
	if (selectedWeek.value === undefined) {
		selectedWeek.value = week
	} else {
		selectedWeek.value = undefined
	}
}

watch(props, () => selectedWeek.value = undefined)
</script>

<template>
	<v-sheet v-if="selectedWeek==undefined">
		<o-clickable-week
			v-for="week in weeks"
			:key="week"
			:week="week"
			:month="month"
			@week-clicked="onWeekClicked(week)"
		/>
	</v-sheet>
	<v-sheet v-else>
		<o-clickable-week
			:week="selectedWeek"
			:month="undefined"
			clickable-days
			@day-clicked=""
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
				v-for="day in selectedWeek.days"
			>
				<o-timeline vertical />
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