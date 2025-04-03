<script setup>
import { useDisplay } from 'vuetify';
const { mobile } = useDisplay()
</script>

<template>
	<div class="booking-form" :class="{ 'mobile': mobile }">
		<v-defaults-provider
			:defaults="{
				global: {
					variant: 'outlined',
					hideDetails: 'auto'
				}
			}"
		>
			<v-autocomplete
				label="Room"
			/>
			<v-divider class="my-3" />
			<v-text-field
				label="Day"
				type="date"
			/>
			<div class="h-input-group">
				<v-text-field
					label="From"
					type="time"
				/>
				<v-text-field
					label="To"
					type="time"
				/>
			</div>
			<o-timeline style="height: 32px" />
			<v-divider class="my-3" />
			<div class="h-input-group reverse-on-mobile">
				<v-btn
					text="cancel"
					@click="$emit('cancel')"
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