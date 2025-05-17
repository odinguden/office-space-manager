<script setup>
import { TYPE_ICON_MAPPINGS } from '@/plugins/config'
import { computed } from 'vue'

const props = defineProps({
	area: Object
})

const breadcrumbs = computed(() => {
	const newCrumbs = []
	for (let superArea of props.area.superAreas) {
		newCrumbs.push({
			text: superArea.name,
			link: `/room/${superArea.id}`
		})
	}
	return newCrumbs
})

const icon = computed(() => {
	const typeName = props.area.areaType.id.toLowerCase()

	if (typeName in TYPE_ICON_MAPPINGS) {
		return TYPE_ICON_MAPPINGS[typeName]
	}

	return TYPE_ICON_MAPPINGS["other"]
})
</script>

<template>
	<o-flat-card
		class="space-card"
		:to="`/room/${area.id}`"
	>
		<o-breadcrumbs
			v-if="area.superAreas.length > 0"
			class="card-crumbs"
			:items="breadcrumbs"
		/>
		<v-tooltip>
			<template v-slot:activator="{ props }">
				<v-icon v-bind="props">
					{{ icon }}
				</v-icon>
			</template>
			{{ area.areaType.id }}
		</v-tooltip>
		<div class="card-header">
			<h1><router-link :to="`/room/${area.id}`">{{ area.name }}</router-link></h1>
		</div>
		<div class="card-content">
			<p>{{ area.description }}</p>
			<o-plan-control-tooltip v-if="area.isPlanControlled" />
		</div>
		<div class="card-extras mb-3" @click.stop>
			<o-space-extras :features="area.areaFeatures" :type="area.areaType.id" />
		</div>
		<v-icon v-if="area.reservable">
			mdi-clock-outline
		</v-icon>
		<div v-if="area.reservable" @click.stop>
			<slot name="timeline">
				<div class="card-timeline">
					<o-timeline
						:scope-start="new Date(area.reservations.scopeStart)"
						:scope-end="new Date(area.reservations.scopeEnd)"
						:reservations="area.reservations.reservations"
					/>
				</div>
			</slot>
		</div>
		<span v-else class="text-faded card-extras">
			<v-icon size="small">
				mdi-information-slab-circle
			</v-icon>
			Space is not reservable
		</span>
		<div class="one-span-two">
			<slot name="actions" />
		</div>
	</o-flat-card>
</template>

<style scoped lang="scss">
a {
	text-decoration: none;
	color: rgb(var(--v-theme-primary))
}

.space-card {
	display: grid;
	grid-template-columns: auto 1fr;
	grid-template-rows: auto 1fr;
	gap: 8px 16px;
	align-items: center;

	.card-crumbs {
		grid-column: 1 / span 2;
		font-size: 0.75rem;
		opacity: 0.7;
		margin: -8px,
	}

	.card-header {
		display: grid;
		grid-template-columns: 1fr auto;
		gap: 16px;
		align-items: center;
	}

	.card-content {
		grid-column: 2;

		word-break: break-word;
		hyphens: auto;
		-ms-hyphens: auto;
		-moz-hyphens: auto;
		-webkit-hyphens: auto;
	}

	.card-extras {
		grid-column: 2;
	}

	.card-timeline {
		height: 24px;
	}

	.one-span-two {
		grid-column: 1 / span 2;
	}
}

.text-faded {
	opacity: var(--v-high-emphasis-opacity);
	font-size: 0.8rem;
}
</style>