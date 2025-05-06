<template>
	<div class="app-bar-actions-container">
		<v-btn
			icon="mdi-account"
			aria-label="Open user profile"
		>
			<v-icon>mdi-account</v-icon>
			<v-menu
				activator="parent"
			>
				<o-user-card />
			</v-menu>
		</v-btn>
		<v-btn
			icon="mdi-cog"
			aria-label="Open site settings"
		/>
	</div>
</template>

<style scoped lang="scss">
.app-bar-actions-container {
	display: flex;
	gap: 16px;
	margin-right: 16px;
}
</style>