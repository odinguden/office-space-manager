<script setup>
import { LOGIN_ENDPOINT } from '@/plugins/config';
</script>


<template>
	<v-sheet class="login-screen" color="surface-light">
		<v-card max-width="260px" elevation="4">
			<v-card-title>
				Log in
			</v-card-title>
			<v-card-text>
				You are not logged in. Please log in using your Microsoft account.
				<o-entra
					class="mt-2"
					:href="LOGIN_ENDPOINT"
				/>
			</v-card-text>
		</v-card>
	</v-sheet>
</template>

<style scoped lang="scss">
.login-screen {
	display: flex;
	height: 100vh;
	align-items: center;
	justify-content: center;
	flex-direction: column;
}
</style>