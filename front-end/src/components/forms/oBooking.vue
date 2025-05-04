<script setup>
import fetcher from '@/plugins/fetcher';
import { computed } from 'vue';
import { useDisplay } from 'vuetify';
const { mobile } = useDisplay()
const emit = defineEmits(['close'])

const MAX_COMMENT = 80

const nowDate = new Date()
const year = nowDate.getFullYear();
const month = String(nowDate.getMonth() + 1).padStart(2, "0")
const day = String(nowDate.getDate()).padStart(2, "0")
const now = `${year}-${month}-${day}`

const props = defineProps({
	area: Object,
	startDate: { type: String }
})

const loadingReservations = ref(true)
const reservations = ref([])
const date = ref(now)
const dateAsDate = computed(() => new Date(date.value))
const startTime = ref("")
const endTime = ref("")
const comment = ref("")

if (props.startDate) {
	const year = props.startDate.getFullYear();
	const month = String(props.startDate.getMonth() + 1).padStart(2, "0")
	const day = String(props.startDate.getDate()).padStart(2, "0")
	const text = `${year}-${month}-${day}`

	date.value = text
}

const startOfDay = computed(() => {
	const time = new Date(dateAsDate.value)
	time.setHours(0, 0, 0, 0)
	return time
})

const endOfDay = computed(() => {
	const time = new Date(dateAsDate.value)
	time.setHours(23, 59, 59, 999)
	return time
})

function getReservations() {
	loadingReservations.value = true
	fetcher.getReservationsForAreaInTime(props.area.id, startOfDay.value, endOfDay.value)
		.then(response => reservations.value = response)
		.finally(() => loadingReservations.value = false)
}

const valid = computed(() => {
	return date.value != "" && date.value != null
		&& endTime.value != "" && endTime.value != null
		&& startTime.value != "" && startTime.value != null
		&& comment.value != "" && comment.value != null
		&& comment.value.length <= MAX_COMMENT
})

getReservations()

watch(date, () => {
	reservations.value = []
	getReservations()
})

const errorMessage = ref("")

function parseTime(input) {
	return input.split(":").map(Number)
}


function tryBook() {
	if (!valid.value) return;

	const startDate = new Date(dateAsDate.value)
	const [sHours, sMinutes] = parseTime(startTime.value)
	startDate.setHours(sHours, sMinutes, 0, 0)
	const endDate = new Date(dateAsDate.value)
	const [eHours, eMinutes] = parseTime(endTime.value)
	endDate.setHours(eHours, eMinutes, 0, 0)

	fetcher.tryMakeReservation(
		props.area.id,
		startDate,
		endDate,
		comment.value
	).then(response => {
		if (!response.ok) {
			if (response.status == 409) {
				errorMessage.value = "This time is already booked"
			} else {
				errorMessage.value = "Failed to book this room"
			}
		} else {
			emit('cancel')
		}
	})
}
</script>

<template>
	<div class="booking-form" :class="{ 'mobile': mobile }" v-if="area">
		<v-defaults-provider
			:defaults="{
				global: {
					variant: 'outlined',
					hideDetails: 'auto'
				}
			}"
		>
			<v-text-field
				label="Room"
				readonly
				:model-value="area.name"
			/>
			<v-divider class="my-3" />
			<v-text-field
				label="Comment"
				:counter="MAX_COMMENT"
				hide-details="auto"
				v-model="comment"
			/>
			<v-divider class="mb-3" />
			<v-text-field
				v-model="date"
				label="Day"
				type="date"
			/>
			<div class="h-input-group">
				<v-text-field
					v-model="startTime"
					label="From"
					type="time"
				/>
				<v-text-field
					v-model="endTime"
					label="To"
					type="time"
				/>
			</div>
			<o-timeline
				v-if="!loadingReservations && area && date"
				:reservations="reservations"
				:scope-start="startOfDay"
				:scope-end="endOfDay"
				style="height: 32px"
			/>
			<v-skeleton-loader v-else-if="loadingReservations" type="heading" />
			<v-divider class="my-3" />
			<v-label v-if="errorMessage && errorMessage.length" class="error-message">
				{{ errorMessage }}
			</v-label>
			<div class="h-input-group reverse-on-mobile">
				<v-btn
					text="cancel"
					@click="emit('cancel')"
				/>
				<v-btn
					:disabled="!valid"
					text="place booking"
					variant="flat"
					color="primary"
					@click="tryBook()"
				/>
			</div>
		</v-defaults-provider>
	</div>
</template>

<style scoped lang="scss">
.booking-form {
	display: flex;
	flex-direction: column;
	gap: 12px;
	.h-input-group {
		display: grid;
		grid-auto-columns: 1fr;
		grid-auto-rows: 1fr;
		grid-auto-flow: column;
		align-items: center;
		text-align: center;
		gap: 16px;
	}

	&.mobile {
		.h-input-group {
			display: flex;
			flex-direction: column;
			align-items: stretch;

			&.reverse-on-mobile {
				flex-direction: column-reverse;
			}
		}
	}
}

.error-message {
	color: rgb(var(--v-theme-error))
}
</style>