<script setup>
const isFavorite = ref(Math.random() > 0.7)
</script>

<template>
	<o-flat-card
		class="space-card"
		elevation="0"
		tile
	>
		<v-icon>mdi-desk</v-icon>
		<div class="card-header">
			<h1><router-link to="/room/0">A123</router-link></h1>
			<v-btn
				icon
				flat
				variant="text"
				density="comfortable"
				:color="isFavorite ? 'warning' : ''"
				@click="isFavorite = !isFavorite"
			>
				<v-icon class="favorite" :class="{'is-favorite': isFavorite}">
					mdi-star{{ isFavorite ? '' : '-outline' }}
				</v-icon>
			</v-btn>
		</div>
		<div class="card-content">
			<p>Something about this room</p>
		</div>
		<div class="card-extras">
			<o-space-extras />
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