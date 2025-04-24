<script setup>
const props = defineProps({
	area: Object
})
</script>

<template>
	<o-flat-card
		class="space-card"
		elevation="0"
		tile
	>
		<v-icon>mdi-desk</v-icon>
		<div class="card-header">
			<h1><router-link to="/room/0">{{ area.name }}</router-link></h1>
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
					<o-timeline/>
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