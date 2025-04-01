<script setup>
const props = defineProps({
	weekNumber: Number,
	days: Array
})
</script>

<template>
	<v-sheet class="week">
		<div class="week-number">
			{{ weekNumber }}
		</div>
		<v-divider vertical />
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
	grid-template-columns: 1fr auto repeat(7, 1fr);
	gap: 8px;

	.week-number {
		color: rgba(var(--v-theme-on-surface), var(--v-medium-emphasis-opacity))
	}
}
</style>