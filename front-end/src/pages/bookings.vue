<script setup>
import fetcher from '@/plugins/fetcher';
import { useAuthStore } from '@/stores/authStore';
import { useDate } from 'vuetify';
const authStore = useAuthStore()
const vDate = useDate()

const areas = ref({})
const scopeStart = ref(new Date())
const scopeEnd = ref(new Date())

const loading = ref(false)
const magnification = ref(60)

function getReservations() {
	loading.value = true
	fetcher.getMyBookings(authStore.me).then(response => {
		areas.value = response;
		if (Object.values(response).length > 0) {
			scopeStart.value = Object.values(response)[0].scopeStart
			scopeEnd.value = Object.values(response)[0].scopeEnd
		}
	}).finally(() => loading.value = false)
}

const absoluteLength = computed(() => {
	return Math.ceil(
		Math.abs(vDate.getDiff(scopeEnd.value, scopeStart.value, 'seconds')) / magnification.value
	)
})

getReservations()
</script>

<template>
	<div>
		<v-btn
			icon="mdi-magnify-plus"
			tile
			variant="flat"
			@click="magnification = magnification / 2"
		/>
		<v-btn icon="mdi-magnify-minus"
			tile
			variant="flat"
			@click="magnification = magnification * 2"
		/>
		<div class="timeline-list mt-2">
			<v-list
				:style="{ 'width': absoluteLength + 'px' }"
				style="min-width: 100%;"
			>
				<v-list-item v-for="reservationData, areaId in areas" :to="`/room/${areaId}`">
					{{ reservationData.reservations[0].areaName }}
					<div class="timeline-wrapper">
						<o-timeline
							style="height: 64px"
							
							:scope-start="new Date(reservationData.scopeStart)"
							:scope-end="new Date(reservationData.scopeEnd)"
							:reservations="reservationData.reservations"
						/>
					</div>
				</v-list-item>
			</v-list>
		</div>
	</div>
</template>

<style scoped lang="scss">
.timeline-list {
	overflow-x: scroll;
}
</style>