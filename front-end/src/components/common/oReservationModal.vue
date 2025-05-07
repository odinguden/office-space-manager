<script setup>
import fetcher from '@/plugins/fetcher';
const model = defineModel({
	modelValue: Boolean
})

const props = defineProps({
	reservation: Object
})

const innerReservation = computed(() => props.reservation)

const oldReservation = ref(null)
watch(innerReservation, (val) => {
	if (val != null) {
		oldReservation.value = val;
	}
})
</script>

<template>
	<o-closeable-dialog
		v-model="model"
		title="Reservation"
		max-width="512px"
	>
		<v-card-text v-if="oldReservation">
			<table>
				<tr>
					<th>
						For room:
					</th>
					<td>
						{{ oldReservation.areaName }}
					</td>
				</tr>
				<tr>
					<th>
						By user:
					</th>
					<td>
						{{ oldReservation.userName }}
					</td>
				</tr>
				<tr>
					<th>
						From:
					</th>
					<td>
						{{ oldReservation.startTime }}
					</td>
				</tr>
				<tr>
					<th>
						To:
					</th>
					<td>
						{{ oldReservation.endTime }}
					</td>
				</tr>
			</table>
		</v-card-text>
		<v-card-actions v-if="oldReservation && oldReservation.isMine">
			<o-delete-button
				:id="oldReservation.id"
				:func="fetcher.deleteBooking"
				@delete="model = false"
			/>
		</v-card-actions>
	</o-closeable-dialog>
</template>

<style scoped lang="scss">
table > tr {
	> th {
		text-align: right;
		padding-right: 8px;
	}

	>td {
		text-align: left;
	}
}
</style>