<script setup>
import { useODate } from '@/plugins/oDate'
import { useDate } from 'vuetify'

const oDate = useODate()
const vDate = useDate()

const props = defineProps({
	year: Number,
	month: Number
})

const weeks = computed(() => {
	return oDate.getWeeksOfMonth(props.year, props.month)
})

function isOutOfMonth(day) {
	return day.getMonth() !== props.month
}
</script>

<template>
	<v-sheet>
		<v-sheet class="weekdays">
			<div />
			<div v-for="day in oDate.weekdays">
				{{ day.substring(0,2) }}
			</div>
		</v-sheet>
		<o-calendar-week
			v-for="week in weeks"
			:days="week.days"
			:week-number="week.number"
		>
			<template v-slot="{ day }">
				<o-calendar-day
					:day="day"
					:disabled="isOutOfMonth(day)"
				/>
			</template>
		</o-calendar-week>
	</v-sheet>
</template>

<style scoped lang="scss">
.weekdays {
	display: grid;
	grid-template-columns: repeat(8, 1fr);

	align-items: center;
	justify-items: center;

	border-bottom: 1px solid rgba(var(--v-border-color), var(--v-border-opacity));

	:first-child {
		width: 100%;
		height: 100%;
		border-right: 1px solid rgba(var(--v-border-color), var(--v-border-opacity));
	}
}

.calendar-item {
	--day-opacity: 1;
	vertical-align: middle;
	color: rgba(var(--v-theme-on-surface), var(--day-opacity));
}

.out-of-month {
	--day-opacity: 0.4;
}
</style>