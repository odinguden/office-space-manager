<script setup>
import fetcher from '@/plugins/fetcher';
import { useRoute, useRouter } from 'vue-router';
const route = useRoute()
const router = useRouter()

const currentPage = ref(1)
const areas = ref([])
const pages = ref(0)
const loading = ref(false)

if (route.query["page"] !== undefined) {
	currentPage.value = Number(route.query["page"])
}

function updatePagination() {
	route.query["page"] = currentPage.value
	loading.value = true;
	fetcher.getSearchResultsWithParamList(route.query, currentPage.value - 1)
		.then(response => {
			areas.value = response.content
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
			<o-space-card
				v-for="area in areas"
				:area="area"
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