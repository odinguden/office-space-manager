<script setup>
import fetcher from '@/plugins/fetcher';
import { useAuthStore } from '@/stores/authStore';
import { watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const currentPage = ref(1)
const plans = ref([])
const pages = ref(0)
const loading = ref(false)

if (route.query["page"] !== undefined) {
	currentPage.value = Number(route.query["page"])
}

function updatePagination() {
	router.push(`/plans?page=${currentPage.value}`)
	loading.value = true;
	fetcher.getAllUsersPlans(authStore.me.userId, currentPage.value - 1).then(response => {
		plans.value = response.content
		pages.value = response.totalPages

		loading.value = false
	})
}
updatePagination()

watch(currentPage, updatePagination)
</script>

<template>
	<div>
		<o-contained-grid v-if="loading">
			<v-skeleton-loader
				v-for="_ in Array(12)"
				type="card"
			/>
		</o-contained-grid>
		<o-contained-grid v-else>
			<o-plan-card
				v-for="plan in plans"
				:plan="plan"
			/>
		</o-contained-grid>
		<v-pagination
			v-model="currentPage"
			class="mt-3"
			:disabled="loading"
			:length="pages"
			:total-visible="8"
		/>
	</div>
</template>