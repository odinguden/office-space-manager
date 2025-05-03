<script setup>
import { watch } from 'vue'
const props = defineProps({
	placeholder: {
		type: Object,
		default: {hours: 2, minutes: 0}
	}
})

const model = defineModel({
	modelValue: Object
})

const hours = ref()
const minutes = ref()

const value = computed({
	get() {
		return {hours: hours.value, minutes: minutes.value}
	}
})

watch([hours, minutes], () => { model.value = value.value })
</script>

<template>
	<div>
		<v-label style="font-size: 0.75em">
			Duration
		</v-label>
		<v-input class="duration-input">
			<v-text-field
				v-model="hours"
				type="number"
				:placeholder="String(placeholder.hours)"
				persistent-placeholder
				max="99"
				min="0"
				label="Hours"
			/>
			<v-text-field
				v-model="minutes"
				type="number"
				:placeholder="String(placeholder.minutes)"
				persistent-placeholder
				max="59"
				min="0"
				label="Minutes"
			/>
			<template #append>
				<v-icon>
					mdi-clock
				</v-icon>
			</template>
		</v-input>
	</div>
</template>

<style lang="scss">
.duration-input  > .v-input__control {
	max-width: 100%;
	display: grid;
	grid-template-columns: 1fr 1fr;
	align-items: center;
	gap: 8px;
}
</style>