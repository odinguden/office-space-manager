<script setup>
const props = defineProps({
	maxWidth: {
		type: String,
		default: "328px"
	}
})
</script>

<template>
	<section :style="{'--max-width': maxWidth}" class="contained-grid">
		<slot />
	</section>
</template>

<style scoped lang="scss">
.contained-grid {
	--max-width: 328px;
	display: grid;
	gap: 16px 24px;
	grid-template-columns: repeat(auto-fill, minmax(var(--max-width), 1fr));
	align-content: start;
}
</style>