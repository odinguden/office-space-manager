<script setup>
import { FEATURE_ICON_MAPPINGS } from '@/plugins/config';

const props = defineProps({
	features: Array,
	type: {
		type: String,
		default: "space"
	}
})

function getIcon(name) {
	name = name.toLowerCase()

	if (name in FEATURE_ICON_MAPPINGS) {
		return FEATURE_ICON_MAPPINGS[name]
	}

	console.log(name)
	return FEATURE_ICON_MAPPINGS["other"]
}


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
				This {{ props.type }} {{ extra.description }}
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