<script setup>
import { useODate } from '@/plugins/oDate';
import { computed } from 'vue';
import { useDate } from 'vuetify';

const vDate = useDate()
const oDate = useODate()

const props = defineProps({
	"day": Date
})

// Checks whether this day is the current day
const isToday = computed(() => {
	const now = new Date()

	return props.day !== undefined && vDate.isSameDay(now, props.day)
})

// Creates an aria-compliant name for this day
const dateName = computed(() => {
	const day = vDate.getDate(props.day)
	const month = oDate.getMonthName(props.day.getMonth())
	const year = props.day.getFullYear()

	return `The ${day}. of ${month}, ${year}`
})
</script>

<template>
	<v-btn
		class="day"
		:aria-label="dateName"
		icon
		density="comfortable"
		:variant="isToday ? 'flat' : 'text'"
		:color="isToday ? 'primary' : undefined"
		@click=""
	>
		<span>
			{{ vDate.getDate(props.day) }}
		</span>
	</v-btn>
</template>

<style scoped lang="scss">
.day {
	--day-opacity: 1;
	aspect-ratio: 1;
	border-radius: 100em;
	margin: 4px;

	color: rgba(var(--v-theme-on-surface), var(--day-opacity));
	text-align: center;
	vertical-align: middle;

	display: flex;
	align-items: center;
	justify-content: center;

	transition: none;

	&:hover, &:active, &:focus {
		background: rgba(var(--v-theme-on-surface), var(--v-focus-opacity))
	}
}
</style>