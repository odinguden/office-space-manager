<script setup>
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
</script>

<template>
	<o-flat-card
		class="space-card"
		elevation="0"
		tile
	>
		<o-breadcrumbs
			v-if="area.superAreas.length > 0"
			class="card-crumbs"
			:items="breadcrumbs"
		/>
		<v-icon>mdi-desk</v-icon>
		<div class="card-header">
			<h1><router-link :to="`/room/${area.id}`">{{ area.name }}</router-link></h1>
		</div>
		<div class="card-content">
			<p>{{ area.description }}</p>
		</div>
		<div class="card-extras">
			<o-space-extras :features="area.areaFeatures" />
		</div>
		<div class="one-span-two">
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
		<div class="one-span-two">
			<slot name="actions" />
		</div>
	</o-flat-card>
</template>

<style scoped lang="scss">
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
</style>