<template>
	<v-card
		class="flat-card"
		elevation="0"
		tile
	>
		<slot />
	</v-card>
</template>

<style scoped lang="scss">
.flat-card {
	border: 1px solid rgba(var(--v-border-color), var(--v-border-opacity));
	padding: 16px 24px;
}
</style>