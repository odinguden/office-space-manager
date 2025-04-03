<script setup>
import { useODate } from '@/plugins/oDate';
const oDate = useODate()

const now = new Date()

const selectedYear = ref(now.getFullYear())
const selectedMonth = ref(now.getMonth())
</script>

<template>
	<v-sheet class="calendar">
		<header>
			<o-cycle-input
				v-model="selectedYear"
				aria-name="year"
			>
				{{ selectedYear }}
			</o-cycle-input>
			<o-cycle-input
				v-model="selectedMonth"
				aria-name="month"
				density="compact"
				:min="0"
				:max="11"
				cycle
				@on-cycle-increase="selectedYear++"
				@on-cycle-decrease="selectedYear--"
			>
				{{ oDate.getMonthName(selectedMonth) }}
			</o-cycle-input>
		</header>
		<v-divider />
		<o-calendar-month
			class="calendar-content"
			:year="selectedYear"
			:month="selectedMonth"
		/>
	</v-sheet>
</template>

<style scoped lang="scss">
.calendar {
	border: 1px solid rgba(var(--v-border-color), var(--v-border-opacity));
}
</style>