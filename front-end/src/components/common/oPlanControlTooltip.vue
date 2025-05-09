<template>
	<v-tooltip>
		<template v-slot:activator="{ props }">
			<span v-bind="props" class="text-error plan-controlled-warning">
				This space is plan controlled
			</span>
		</template>
		<span>
			This space can only be booked when the owner marks it as available
		</span>
	</v-tooltip>
</template>

<style scoped lang="scss">
.plan-controlled-warning {
	font-size: 0.75rem;
}
</style>