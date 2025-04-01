<script setup>
import { useODate } from '@/plugins/oDate';
const oDate = useODate()

const now = new Date()

const selectedYear = ref(now.getFullYear())
const selectedMonth = ref(now.getMonth())
</script>

<template>
	<v-sheet>
		<header>
			<o-cycle-input
				v-model="selectedYear"
			>
				{{ selectedYear }}
			</o-cycle-input>
			<v-divider />
			<o-cycle-input
				density="compact"
				v-model="selectedMonth"
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
		<o-calendar-month :year="selectedYear" :month="selectedMonth"/>
	</v-sheet>
</template>