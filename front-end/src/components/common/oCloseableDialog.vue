<script setup>
const props = defineProps({
	title: String
})

const model = defineModel({
	"modelValue": Boolean
})
</script>

<template>
	<v-dialog v-model="model">
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
					@click="model = false"
				/>
			</v-card-title>
			<slot />
		</v-card>
	</v-dialog>
</template>

<style scoped lang="scss">
.card-title {
	display: flex;
}
</style>