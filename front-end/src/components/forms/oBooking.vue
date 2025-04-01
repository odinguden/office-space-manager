<script setup>
import { useDisplay } from 'vuetify';
import BookingModel from './bookingModel';

const { mobile } = useDisplay()

const props = defineProps({
	bookingModel: {
		type: BookingModel,
		default: new BookingModel()
	}
})

function bookingModelComputed(property) {
	return computed({
		get() {
			return props.bookingModel.values[property].value
		},
		set(value) {
			props.bookingModel.values[property].value = value
		}
	})
}

const room = bookingModelComputed("room")
const day = bookingModelComputed("day")
const start = bookingModelComputed("start")
const end = bookingModelComputed("end")
</script>

<template>
	<div class="booking-form" :class="{ 'mobile': mobile }">
		<v-defaults-provider
			:defaults="{
				global: {
					variant: 'outlined',
					hideDetails: 'auto'
				},
				VTextField: {
				}
			}"
		>
			<v-autocomplete
				label="Room"
				v-model="room"
				:readonly="props.bookingModel.values.room.readonly"
			/>
			<v-divider class="my-3" />
			<v-text-field
				label="Day"
				type="date"
				v-model="day"
				:readonly="props.bookingModel.values.day.readonly"
			/>
			<div class="h-input-group">
				<v-text-field
					label="From"
					type="time"
					v-model="start"
					:readonly="props.bookingModel.values.start.readonly"
				/>
				<v-text-field
					label="To"
					type="time"
					v-model="end"
					:readonly="props.bookingModel.values.end.readonly"
				/>
			</div>
			<o-timeline style="height: 32px" />
			<v-divider class="my-3" />
			<div class="h-input-group reverse-on-mobile">
				<v-btn
					text="cancel"
				/>
				<v-btn
					text="place booking"
					variant="flat"
					color="primary"
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
</style>