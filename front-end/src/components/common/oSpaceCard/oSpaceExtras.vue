<script setup>
function getIcon(name) {
	console.log(name)
	switch (name) {
		case "Screen":
			return "mdi-monitor"
		case "Camera":
			return "mdi-camera"
		case "Microphone":
			return "mdi-microphone"
		default:
			return "mdi-help-box"
	}

}

const props = defineProps({
	features: Array
})
</script>

<template>
	<div class="space-extras">
		<v-tooltip v-for="extra in props.features">
			<template v-slot:activator="{ props }">
				<v-icon v-bind="props">
					{{ getIcon(extra.id) }}
				</v-icon>
			</template>
			<span>
				{{ extra.id }}
				<br>
				This space comes equipped with {{ extra.desc }}
			</span>
		</v-tooltip>
	</div>
</template>

<style scoped lang="scss">
.space-extras {
	display: flex;
	justify-content: start;
	flex-wrap: wrap;
	gap: 4px;
}
</style>