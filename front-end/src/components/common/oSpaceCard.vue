<script setup>
import { defineProps } from 'vue';
import { TYPE_ICON_MAPPINGS } from '@/plugins/config';

const props = defineProps({
	area: Object
})

const icon = computed(() => {
	const typeName = props.area.areaType.id.toLowerCase()

	if (typeName in TYPE_ICON_MAPPINGS) {
		return TYPE_ICON_MAPPINGS[typeName]
	}

	return TYPE_ICON_MAPPINGS["other"]
})

const areaTypeName = computed(() => {
	const name = props.area.areaType.id
	return name.substring(0,1).toUpperCase() + name.substring(1)
})

const capacityTooltip = computed(() => {
	const plurality = props.area.capacity === 1 ? "person" : "people"
	return `This ${props.area.areaType.id} holds ${props.area.capacity} ${plurality}`
})

const breadcrumbs = computed(() => {
	const crumbs = []
	for (let superArea of props.area.superAreas) {
		crumbs.push({
			title: superArea.name,
			to: `/room/${superArea.id}`
		})
	}

	crumbs.push({
		title: props.area.name,
		to: `/room/${props.area.id}`,
		self: true
	})

	return crumbs
})
</script>

<template>
	<v-card
		elevation="3"
		:prepend-icon="icon"
		:title="props.area.name"
		:subtitle="props.area.description"
		:to="`/room/${area.id}`"
	>
		<template v-slot:title>
			<v-breadcrumbs
				class="area-breadcrumbs"
				:items="breadcrumbs"
				v-ripple.stop
				density="compact"
			>
				<template v-slot:item="{ item }">
					<router-link
						:to="item.to"
						class="v-breadcrumbs-item"
						:class="{'superarea-crumb': !item.self}"
					>
						{{ item.title }}
					</router-link>
				</template>
				<template v-slot:divider>
					<div class="area-crumb-divider">
						/
					</div>
				</template>
			</v-breadcrumbs>
		</template>
		<template v-slot:prepend>
			<v-icon v-tooltip="areaTypeName">
				{{ icon }}
			</v-icon>
		</template>
		<template v-slot:append>
			<div class="card-capacity card-info" v-tooltip="capacityTooltip">
				{{ props.area.capacity }}
				<v-icon size="small">
					mdi-account-group
				</v-icon>
			</div>
		</template>
		<v-card-text class="card-text">
			<div class="card-details-container">
				<div v-if="props.area.isPlanControlled">
					<v-icon>
						mdi-information-slab-circle-outline
					</v-icon>
					This {{ props.area.areaType.id }} is controlled by a plan
				</div>
				<div v-else-if="!props.area.reservable" class="card-info">
					<v-icon>
						mdi-information-slab-circle-outline
					</v-icon>
					This {{ props.area.areaType.id }} is not bookable
				</div>
				<v-spacer v-else />
				<o-space-extras
					v-if="area.areaFeatures.length"
					class="card-features"
					:features="area.areaFeatures"
					:type="area.areaType.id"
				/>
				<v-icon v-else />
			</div>
			<slot name="timeline">
				<div v-if="props.area.reservable" class="card-timeline">
					<o-timeline
						:scope-start="new Date(area.reservations.scopeStart)"
						:scope-end="new Date(area.reservations.scopeEnd)"
						:reservations="area.reservations.reservations"
					/>
				</div>
			</slot>
		</v-card-text>
	</v-card>
</template>

<style scoped lang="scss">
.card-timeline {
	height: 20px;
}

.card-prepend {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 2px;
}

.card-capacity {
	display: flex;
	align-items: center;
	gap: 4px;
}

.card-text {
	display: flex;
	flex-direction: column;
	gap: 8px;

	.card-features {
		justify-self: end;
	}
}

.card-info {
	color: rgba(var(--v-theme-on-surface), var(--v-medium-emphasis-opacity));
}

.card-details-container {
	display: flex;
	justify-content: space-between;
}

.area-breadcrumbs, .v-breadcrumbs-item {
	padding: 0;
}

.superarea-crumb, .area-crumb-divider {
	opacity: var(--v-medium-emphasis-opacity);
	
	font-size: 0.8rem;
}
</style>