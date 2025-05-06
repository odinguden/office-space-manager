<script setup>
import { useAuthStore } from '@/stores/authStore';
import { useRouter } from 'vue-router';
import { BACKEND_URL, LOGOUT_ENDPOINT } from '@/plugins/config';

const authStore = useAuthStore()
</script>

<template>
	<v-card>
		<v-card-title>
			Logged in as {{ authStore.me.fullName }}
		</v-card-title>
		<v-card-subtitle class="ml-5">
			{{ authStore.me.email }}
		</v-card-subtitle>
		<v-card-text />
		<v-card-actions class="me-actions">
			<v-btn
				variant="outlined"
				:href="BACKEND_URL + LOGOUT_ENDPOINT"
			>
				Log out
			</v-btn>
		</v-card-actions>
	</v-card>
</template>

<style scoped lang="scss">
.me-actions {
	display: flex;
	flex-direction: row-reverse;
}
</style>