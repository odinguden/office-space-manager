<script setup>
import { useODate } from '@/plugins/oDate'

const oDate = useODate()

const props = defineProps({
	year: Number,
	month: Number
})

const weeks = computed(() => {
	return oDate.getWeeksOfMonth(props.year, props.month)
})
</script>

<template>
	<v-sheet>
		<v-sheet class="weekdays">
			<div />
			<v-divider vertical />
			<v-sheet v-for="day in oDate.weekdays">
				{{ day.substring(0,2) }}
			</v-sheet>
		</v-sheet>
		<o-calendar-week
			v-for="week in weeks"
			:days="week.days"
			:week-number="week.number"
		>
			<template v-slot="{ day }">
				<o-calendar-day
					:day="day"
					:class="{ 'out-of-month': day.getMonth() !== props.month }"
				/>
			</template>
		</o-calendar-week>
	</v-sheet>
</template>

<style scoped lang="scss">
.weekdays {
	display: grid;
	grid-template-columns: 1fr auto repeat(7, 1fr);
	gap: 8px;
}

.out-of-month {
	color: rgba(var(--v-theme-on-surface), var(--v-medium-emphasis-opacity))
}
</style>