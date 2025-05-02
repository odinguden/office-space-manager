<script setup>
const modelValue = defineModel({
	modelValue: String
})

const isMenuOpen = ref(false)
</script>

<template>
	<v-text-field
		v-model="modelValue"
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
				v-model="modelValue"
				v-if="isMenuOpen"
				full-width
				format="24hr"
			/>
		</v-menu>
	</v-text-field>
</template>