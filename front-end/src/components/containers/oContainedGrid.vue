<script setup>
const props = defineProps({
	minWidth: {
		type: String,
		default: "328px"
	}
})
</script>

<template>
	<section :style="{'--min-width': minWidth}" class="contained-grid">
		<slot />
	</section>
</template>

<style scoped lang="scss">
.contained-grid {
	--min-width: 328px;
	display: grid;
	gap: 16px 24px;
	grid-template-columns: repeat(auto-fill, minmax(var(--min-width), 1fr));
	align-content: start;
}
</style>