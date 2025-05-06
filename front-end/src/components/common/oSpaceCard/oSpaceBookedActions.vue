<template>
	<div class="booked-actions">
		<v-btn
			color="error"
			variant="outlined"
			density="comfortable"
		>
			Cancel Booking
			<o-confirm-dialog activator="parent">
				Are you certain you want to delete this booking?
			</o-confirm-dialog>
		</v-btn>
	</div>
</template>

<style scoped lang="scss">
.booked-actions {
	display: flex;
	flex-direction: row-reverse;
}
</style>