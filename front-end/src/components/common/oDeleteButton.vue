<script setup>
const emit = defineEmits(["delete"])
const props = defineProps({
	text: {
		type: String,
		default: "Delete"
	}
})

function doDelete() {
	emit("delete")
}
</script>

<template>
	<v-btn
		color="error"
		prepend-icon="mdi-delete"
		variant="outlined"
	>
		{{ props.text }}
		<o-confirm-dialog activator="parent" @confirm="doDelete">
			Are you certain you want to delete this?
		</o-confirm-dialog>
	</v-btn>
</template>