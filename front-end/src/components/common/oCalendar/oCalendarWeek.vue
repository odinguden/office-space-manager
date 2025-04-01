<script setup>
const props = defineProps({
	weekNumber: Number,
	days: Array
})
</script>

<template>
	<v-sheet class="week">
		<slot name="week-number" :number="weekNumber">
			<div class="week-number">
				{{ weekNumber }}
			</div>
		</slot>
		<div v-for="day in props.days">
			<slot :day="day">
				<o-calendar-day :day="day"/>
			</slot>
		</div>
	</v-sheet>
</template>

<style scoped lang="scss">
.week {
	display: grid;
	grid-template-columns: repeat(8, 1fr);

	.week-number {
		display: flex;
		align-items: center;
		justify-content: center;
		border-right: 1px solid rgba(var(--v-border-color), var(--v-border-opacity))
	}
}
</style>