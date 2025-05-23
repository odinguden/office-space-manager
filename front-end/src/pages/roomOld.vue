<script setup>
import fetcher from '@/plugins/fetcher';
import { useAuthStore } from '@/stores/authStore';
import { useRoute } from 'vue-router';

const authStore = useAuthStore()

const route = useRoute()

const area = ref(null)
const selectedDate = ref(null)
const breadcrumbs = ref([])
const showModal = ref(false)

function getArea() {
	const id = route.params.id

	fetcher.getArea(id)
		.then(response => area.value = response)
		.then(() => {
			const newCrumbs = []
			for (let superArea of area.value.superAreas) {
				newCrumbs.push({
					text: superArea.name,
					link: `/room/${superArea.id}`
				})
			}

			newCrumbs.push({
				text: area.value.name,
				link: null
			})

			breadcrumbs.value = newCrumbs
		})
}

function openModal(day) {
	if (day == undefined) day = new Date()
	selectedDate.value = day
	showModal.value = true
}

function closeModal() {
	showModal.value = false
}

const isMine = computed(() => area.value.administratorIds.includes(authStore.me.userId))
const isPlanned = computed(() => area.value.isPlanControlled)
getArea()
</script>

<template>
	<section class="room-page" v-if="area !== null" :key="area.id">
		<o-breadcrumbs
			:items="breadcrumbs"
			class="breadcrumbs"
		/>
		<header class="room-header">
			<v-icon>mdi-desk</v-icon>
			<h1>{{ area.name }}</h1>
			<p>{{ area.description }}</p>
			<o-plan-control-tooltip v-if="area.isPlanControlled" />
			<o-space-extras :features="area.areaFeatures" />
		</header>
		<v-btn
			color="primary"
			text="Book this room"
			tile
			variant="outlined"
			@click="openModal()"
		/>
		<v-dialog
			v-model="showModal"
			max-width="512px"
		>
			<v-card>
				<v-card-title>
					Booking
				</v-card-title>
				<v-card-text>
					<o-booking
						:area="area"
						:start-date="selectedDate"
						@cancel="closeModal"
					/>
				</v-card-text>
			</v-card>
		</v-dialog>
		<v-btn
			v-if="isMine && isPlanned"
			color="primary"
			tile
			variant="outlined"
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
		<o-calendar :area="area" @day-clicked="openModal($event)" />
	</section>
	<v-skeleton-loader
		v-else
		type="heading,paragraph,card"
	/>
</template>

<style scoped lang="scss">

section.room-page {
	display: flex;
	flex-direction: column;
	gap: 8px;
	margin: 8px auto;
	width: 448px;
}

header.room-header {
	display: grid;
	grid-template-columns: auto 1fr;
	grid-template-rows: auto auto;
	gap: 8px 16px;

	align-items: center;

	> :first-child {
		font-size: 2em;
	}

	> :not(:first-child) {
		grid-column: 2;
		width: 100%;
		text-wrap: wrap;
		word-break: break-word;
	}
}

.breadcrumbs {
	opacity: 0.8;
}
</style>