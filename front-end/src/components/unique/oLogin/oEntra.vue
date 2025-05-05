<template>
	<v-btn
		class="entra-login"
		rounded="0"
		variant="outlined"
	>
		<img
			class="entra-logo"
			alt="Microsoft Logo"
			src="https://learn.microsoft.com/en-us/entra/identity-platform/media/howto-add-branding-in-apps/ms-symbollockup_mssymbol_19.svg"
		>
		<p class="entra-font">
			Sign in with Microsoft
		</p>
	</v-btn>
</template>

<style scoped lang="scss">
.entra-login {
	height: 41px;
	padding: 0 12px;
	display: flex;
	align-items: center;
	border: 1px solid rgba(var(--v-border-color),0.45);

	.entra-logo {
		margin-right: 12px;
	}

	.entra-font {
		font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important;
		text-transform: none;
		font-size: 15px;
		font-weight: 600;
		color: rgba(var(--v-theme-on-surface),0.63)
	}
}
</style>