<script setup>
import fetcher from '@/plugins/fetcher';
import { useDate } from 'vuetify';

const vDate = useDate()

const props = defineProps({
	reservation: Object
})

const innerReservation = computed(() => props.reservation)
const loading = ref(false)
const errorMsg = ref(null)

const oldReservation = ref(null)
watch(innerReservation, (val) => {
	if (val != null) {
		oldReservation.value = val;
	}
})

function onDelete() {
	loading.value = true
	fetcher.deleteBooking(oldReservation.value.id)
		.then(() => {
			errorMsg.value = ""
			window.location.reload()
		})
		.catch(() => {
			errorMsg.value = "Failed to delete"
		})
		.finally(() => loading.value = false)

}

const startTime = computed(() => {
	return vDate.format(new Date(oldReservation.value.startTime), "fullDateTime24h")
})

const endTime = computed(() => {
	return vDate.format(new Date(oldReservation.value.endTime), "fullDateTime24h")

})
</script>

<template>
	<o-closeable-dialog
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
						{{ startTime }}
					</td>
				</tr>
				<tr>
					<th>
						To:
					</th>
					<td>
						{{ endTime }}
					</td>
				</tr>
				<tr>
					<th>
						Comment:
					</th>
					<td>
						{{ oldReservation.comment }}
					</td>
				</tr>
			</table>
		</v-card-text>
		<v-card-actions v-if="oldReservation && oldReservation.isMine">
			<o-delete-button
				:loading="loading"
				@delete="onDelete"
			/>
		</v-card-actions>
	</o-closeable-dialog>
</template>

<style scoped lang="scss">
table {
	border-collapse: collapse;
	width: 100%;

	> tr{
		vertical-align: top;

		> th {
			text-align: right;
			padding-right: 8px;
		}

		>td {
			text-align: left;
			word-break: break-word;
		}
	}
}
</style>