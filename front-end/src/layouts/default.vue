<script setup>
import { useAuthStore } from '@/stores/authStore';
import { onMounted } from 'vue';

const authStore = useAuthStore()

onMounted(() => authStore.updateState())
</script>

<template>
	<v-main id="main" v-if="authStore.authState === authStore.AUTH_STATES.AUTHORIZED">
		<o-app-frame />
		<router-view id="main-view" :key="$route.fullPath" />
		<o-footer />
	</v-main>
	<v-main v-else-if="authStore.authState === authStore.AUTH_STATES.UNAUTHORIZED">
		<o-login />
	</v-main>
	<v-main v-else>
		<v-skeleton-loader :type="card" />
	</v-main>
</template>

<style scoped lang="scss">
body {
	min-height: 100vh;
}

#main {
	display:flex;
	flex-direction: column;

	#main-view {
		flex-grow: 1;
		max-width: 100vw;
		padding: 24px 16px;
	}
}
</style>