<script setup>
const props = defineProps({
	clickableDays: Boolean,
	month: Number,
	week: Object
})

const emit = defineEmits(["week-clicked", "day-clicked"])

function weekClicked() {
	if (!props.clickableDays) {
		console.log("Week")
		emit("week-clicked", props.week.number)
	}
}

function weekNumberClicked() {
	if (props.clickableDays) {
		console.log("Week number")
		emit("week-clicked", props.week.number)
	}
}

function dayClicked(day) {
	if (props.clickableDays) {
		console.log("Day")
		emit("day-clicked", day)
	}
}
</script>

<template>
	<div
		v-ripple="!props.clickableDays"
		class="week"
		:class="{'hoverable': !props.clickableDays}"
		@click="weekClicked"
	>
		<slot name="week-number">
			<v-sheet
				:class="{'hoverable': props.clickableDays}"
				v-ripple="props.clickableDays"
				@click="weekNumberClicked"
			>
				{{ week.number }}
			</v-sheet>
		</slot>
		<v-sheet
			v-for="day in week.days"
			:class="{
				'out-of-month': (month !== undefined && day.getMonth() != props.month),
				'hoverable': props.clickableDays
			}"
			v-ripple="props.clickableDays"
			@click="dayClicked"
		>
			{{ day.getDate() }}
		</v-sheet>
	</div>
</template>

<style scoped lang="scss">
.week {
	display: grid;
	grid-auto-flow: column;
	grid-auto-columns: 1fr;
	user-select: none;
	cursor: pointer;

	> *:first-child {
		border-right: 1px solid rgba(var(--v-border-color), var(--v-border-opacity));
	}

	> * {
		padding: 4px;
		aspect-ratio: 1;
		background: transparent;
		text-align: center;
		vertical-align: middle;
	}

	> .out-of-month {
		opacity: 0.5;
	}
}

.hoverable:hover {
	background-color: rgba(var(--v-theme-on-surface), var(--v-hover-opacity));
}
</style>