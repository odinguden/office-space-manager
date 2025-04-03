<script setup>
const emit = defineEmits(["cancel", "confirm"])

const props = defineProps({
	title: {
		type: String,
		default: "Confirm"
	},
	confirmText: {
		type: String,
		default: "Ok"
	},
	cancelText: {
		type: String,
		default: "Cancel"
	}
})

const open = ref(false)

function cancel() {
	open.value = false
	emit('cancel')
}

function confirm() {
	open.value = false
	emit('confirm')
}
</script>

<template>
	<v-dialog
		v-model="open"
		max-width="356px"
		persistent
	>
		<v-card>
			<v-card-title class="card-title">
				<slot name="title">
					{{ title }}
				</slot>
				<v-spacer />
				<v-btn
					icon="mdi-close"
					density="compact"
					variant="plain"
					@click="cancel"
				/>
			</v-card-title>
			<v-card-text>
				<slot>
					Are you certain you want to do this?
				</slot>
			</v-card-text>
			<v-card-actions class="card-actions">
				<slot name="cancel-button">
					<v-btn
						color="error"
						variant="outlined"
						:text="cancelText"
						@click="cancel"
					/>
					<v-btn
						color="success"
						variant="flat"
						:text="confirmText"
						@click="confirm"
					/>
				</slot>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<style scoped lang="scss">
.card-actions {
	display: grid;
	grid-template-columns: 1fr 1fr;
}

.card-title {
	display: flex;
}
</style>