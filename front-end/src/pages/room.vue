<script setup>
import BookingModel from '@/components/forms/bookingModel';

const breadcrumbItems = [
	{
		text: "Town hall",
		icon: "mdi-domain"
	},
	{
		text: "Floor 1",
		icon: "mdi-stairs"
	},
	{
		text: "A123",
		icon: "mdi-desk"
	}
]

const showModal = ref(false)

function getNewBookingModel() {
	return new BookingModel("A123")
}
</script>

<template>
	<section class="room-page">
		<o-breadcrumbs
			:items="breadcrumbItems"
			class="breadcrumbs"
		/>
		<header class="room-header">
			<v-icon>mdi-desk</v-icon>
			<h1>A123</h1>
			<p>Something about this room...</p>
			<o-space-extras />
		</header>
		<v-btn
			color="primary"
			text="Book this room"
			tile
			variant="outlined"
			@click="showModal = true"
		/>
		<v-dialog max-width="512px" v-model="showModal">
			<v-card>
				<v-card-title>
					Booking
				</v-card-title>
				<v-card-text>
					<o-booking :booking-model="getNewBookingModel()" />
				</v-card-text>
			</v-card>
		</v-dialog>
		<o-calendar />
	</section>
</template>

<style scoped lang="scss">
section.room-page {
	display: flex;
	flex-direction: column;
	gap: 8px;
	margin: 8px auto;
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
	}
}

.breadcrumbs {
	opacity: 0.8;
}
</style>