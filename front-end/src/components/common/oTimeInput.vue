<script setup>
import { computed } from 'vue';
import { timeUtil } from '@/plugins/timeUtil';

const modelValue = defineModel({
	modelValue: Date
})

const modelComputed = computed({
	get() {
		if (modelValue.value == null || modelValue.value == "") {
			return "00:00"
		}
		return timeUtil.toTimeString(modelValue.value)
	},
	set(newValue) {
		modelValue.value = timeUtil.fromTimeString(newValue)
	}
})

const isMenuOpen = ref(false)
</script>

<template>
	<v-text-field
		v-model="modelComputed"
		readonly
		:active="isMenuOpen"
		:focus="isMenuOpen"
	>
		<v-menu
			v-model="isMenuOpen"
			:close-on-content-click="false"
			activator="parent"
		>
			<v-time-picker
				v-model="modelComputed"
				v-if="isMenuOpen"
				full-width
				format="24hr"
			/>
		</v-menu>
	</v-text-field>
</template>