<script setup>
import { computed } from 'vue';

const model = defineModel({
	modelValue: Date
})

const pad = (str) => String(str).padStart(2, '0')

const computedValue = computed({
	get() {
		if (!model.value) {
			const newDate = new Date();
			newDate.setHours(0, 0, 0, 0)
			model.value = newDate
		}

		return `${model.value.getFullYear()}-${pad(model.value.getMonth() + 1)}-${pad(model.value.getDate())}`
	},
	set(value) {
		const [year, month, day] = value.split("-").map(Number)

		model.value = new Date(year, month - 1, day, 0, 0, 0, 0);
	}
})
</script>

<template>
	<v-text-field
		v-model="computedValue"
		type="Date"
	/>
</template>