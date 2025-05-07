<script setup>
const props = defineProps({
	clickableDays: Boolean,
	area: Object,
	month: Number,
	week: Object,
	frequencies: Array
})

const emit = defineEmits(["week-clicked", "day-clicked"])

function weekClicked() {
	if (!props.clickableDays) {
		emit("week-clicked", props.week.number)
	}
}

function weekNumberClicked() {
	if (props.clickableDays) {
		emit("week-clicked", props.week.number)
	}
}

function dayClicked(day) {
	if (props.clickableDays && getIsDayAvailable(day)) {
		emit("day-clicked", day)
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
			v-for="(day, idx) in week.days"
			:class="{
				'out-of-month': (month !== undefined && day.getMonth() != props.month),
				'hoverable': props.clickableDays,
				'disabled': !getIsDayAvailable(day)
			}"
			:style="{
				'--frequency': (month === undefined || day.getMonth() == props.month)
					? frequencies[day.getDate() - 1] / 100 : 0
			}"
			v-ripple="props.clickableDays && getIsDayAvailable(day)"
			:disabled="!getIsDayAvailable(day)"
			@click="dayClicked(day)"
		>
			<div>
				{{ day.getDate() }}
			</div>
		</v-sheet>
	</div>
</template>

<style scoped lang="scss">
.week {
	--frequency: 0;
	display: grid;
	grid-auto-flow: column;
	grid-auto-columns: 1fr;
	user-select: none;
	cursor: pointer;

	> *:first-child {
		border-right: 1px solid rgba(var(--v-border-color), var(--v-border-opacity));
	}

	> * {
		--frequency: 0;
		background-color: rgba(var(--v-theme-error), var(--frequency));
		padding: 4px;
		aspect-ratio: 1;
		text-align: center;
		vertical-align: middle;

		> div {
			background-color: rgba(var(--v-theme-surface), 0.8);
			border-radius: 100vmax;
			height: 1.5rem;
			aspect-ratio: 1;
			vertical-align: middle;
			text-align: center;
		}
	}

	> .out-of-month {
		opacity: 0.5;
	}

	> .disabled {
		background-color: rgba(var(--v-theme-on-surface), 0.1)
	}
}

.hoverable:not(.disabled):hover {
	opacity: 0.8;
}
</style>