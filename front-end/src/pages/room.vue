<script setup>
import { useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import fetcher from '@/plugins/fetcher';
import { TYPE_ICON_MAPPINGS } from '@/plugins/config';

const route = useRoute()
const area = ref(null)
const selectedDate = ref(null)
const authStore = useAuthStore()

const modalOpen = ref(false)

const isMine = computed(() => {
	return area.value.administratorIds.includes(authStore.me.userId)
})

const icon = computed(() => {
	if (area.value === null) return;
	const typeName = area.value.areaType.id.toLowerCase()

	if (typeName in TYPE_ICON_MAPPINGS) {
		return TYPE_ICON_MAPPINGS[typeName]
	}

	return TYPE_ICON_MAPPINGS["other"]
})

const breadcrumbs = computed(() => {
	const crumbs = []
	for (let superArea of area.value.superAreas) {
		crumbs.push({
			title: superArea.name,
			to: `/room/${superArea.id}`
		})
	}

	crumbs.push({
		title: area.value.name,
		to: `/room/${area.value.id}`,
		self: true
	})

	return crumbs
})

const capacityTooltip = computed(() => {
	const plurality = area.value.capacity === 1 ? "person" : "people"
	return `This ${area.value.areaType.id} holds ${area.value.capacity} ${plurality}`
})


function openModalForDay(day) {
	selectedDate.value = day
	modalOpen.value = true
}

function getArea() {
	const id = route.params.id

	fetcher.getArea(id)
		.then(response => area.value = response)
}

getArea()
</script>

<template>
	<section v-if="area" class="room-container">
		<v-card
			:title="area.name"
			:prepend-icon="icon"
			flat
		>
			<template v-slot:append>
				<div class="area-description" v-tooltip="capacityTooltip">
					{{ area.capacity }}
					<v-icon size="small">
						mdi-account-group
					</v-icon>
				</div>
		</template>
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
			<v-card-text class="area-description">
				{{ area.description }}
			</v-card-text>
		</v-card>
		<div v-if="isMine">
			<div class="area-info area-description mb-5">
				<v-icon>
					mdi-account-hard-hat-outline
				</v-icon>
				You are an administrator of this area
			</div>
			<v-btn
				v-if="area.isPlanControlled"
				color="primary"
				variant="outlined"
				block
			>
				Create plan
				<o-closeable-dialog
					activator="parent"
					max-width="512px"
					title="Create Plan"
				>
					<v-card-text>
						<o-plan :area-id="area.id" />
					</v-card-text>
				</o-closeable-dialog>
			</v-btn>
		</div>
		<div v-if="area.reservable" class="reservation-utils">
			<v-btn
				color="primary"
				class="mb-5"
				block
			>
				Book this room
				<v-dialog
					v-model="modalOpen"
					activator="parent"
					max-width="512px"
				>
					<template v-slot:default="{ isActive }">
						<v-card>
							<v-card-title>
								Booking
							</v-card-title>
							<v-card-text>
								<o-booking
									:area="area"
									:start-date="selectedDate"
									@cancel="isActive.value = false"
								/>
							</v-card-text>
						</v-card>
					</template>
				</v-dialog>
			</v-btn>
			<o-calendar
				:area="area"
				@day-clicked="openModalForDay"
			/>
		</div>
		<div v-if="!area.reservable" class="area-info area-description">
			<v-icon>
				mdi-information-slab-circle-outline
			</v-icon>
			<div>
				This {{ area.areaType.id }} is not bookable
			</div>
		</div>
	</section>
</template>

<style scoped lang="scss">
.room-container {
	display: flex;
	flex-direction: column;
	gap: 8px;
	margin: 8px auto;
}

.area-info {
	display: flex;
	gap: 8px;
}

.area-description {
	opacity: var(--v-medium-emphasis-opacity);
	color: rgba(var(--v-theme-on-surface), var(--v-high-emphasis-opacity))
}

.area-breadcrumbs, .v-breadcrumbs-item {
	padding: 0;
}

.superarea-crumb, .area-crumb-divider {
	opacity: var(--v-medium-emphasis-opacity);

	font-size: 0.8rem;
}
</style>