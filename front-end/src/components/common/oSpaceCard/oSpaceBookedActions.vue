<template>
	<div class="booked-actions">
		<v-btn
			color="error"
			variant="outlined"
			density="comfortable"
		>
			Cancel Booking
			<o-confirm-dialog activator="parent"/>
		</v-btn>
	</div>
</template>

<style scoped lang="scss">
.booked-actions {
	display: flex;
	flex-direction: row-reverse;
}
</style>