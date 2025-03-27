<script setup>
const model = defineModel("modelValue", {
	type: Number
})

// Figures out what window to show in the drawer
const windowState = computed(() => {
	if (model.value === undefined) {
		// If the drawer is closed, return default
		return 0
	}

	// Otherwise return the model value (0 or 1)
	return model.value
})

// Figures out whether the drawer should be visible
const visibleState = computed({
	get() {
		// If the modelvalue is undefined, the drawer is closed
		return model.value !== undefined
	},
	set(newValue) {
		if (newValue === false) {
			// Sets the model's value to undefined (closed)
			model.value = undefined
		} else {
			// Otherwise set to what window to show
			model.value = windowState.value
		}
	}
})
</script>

<template>
	<v-navigation-drawer v-model="visibleState">
		<v-window v-model="windowState">
			<v-window-item>
				<o-navigation-drawer />
			</v-window-item>
			<v-window-item>
				<o-search-drawer />
			</v-window-item>
		</v-window>
	</v-navigation-drawer>
</template>