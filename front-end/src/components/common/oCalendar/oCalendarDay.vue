<script setup>
import { computed } from 'vue';
import { useDate } from 'vuetify';

const vDate = useDate()

const props = defineProps({
	"day": Date
})

const isToday = computed(() => {
	const now = new Date()
	return vDate.isSameDay(now, props.day)
})
</script>

<template>
	<v-sheet class="day" :class="{ 'today': isToday }">
		{{ vDate.getDate(props.day) }}
	</v-sheet>
</template>

<style scoped lang="scss">
.day {
	&.today {
		color: rgb(var(--v-theme-primary))
	}
}
</style>