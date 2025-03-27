<script setup>
import { useDisplay } from 'vuetify';
const { mobile, smAndDown } = useDisplay()

const model = defineModel("modelValue", {
	type: Number,
	default: 0
})

const drawerModes = [
	{
		name: "Navigation",
		icon: "mdi-menu",
		mode: "navigation"
	},
	{
		name: "Search",
		icon: "mdi-magnify",
		mode: "search"
	}
]
</script>

<template>
	<div class="drawer-mode-wrapper">
		<v-btn-toggle
			v-model="model"
			class="drawer-mode-toggle"
			:class="{'mobile': smAndDown}"
			tile
			:mandatory="!mobile"
			color="primary"
		>
			<v-btn
				v-for="mode in drawerModes"
				:key="mode.name"
				:aria-label="mode.name"
			>
				<v-icon size="x-large">{{ mode.icon }}</v-icon>
			</v-btn>
		</v-btn-toggle>
		<v-divider vertical />
	</div>
</template>

<style scoped lang="scss">
.drawer-mode-wrapper {
	height: 100%;
}

.drawer-mode-toggle {
	display: grid;
	grid-auto-flow: column;
	grid-auto-columns: 1fr;
	width: 256px;
	height: 100%;

	&.mobile {
		width: auto;
	}
}
</style>