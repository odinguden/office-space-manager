<script setup>
import area from '@/plugins/Area';
import { watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const currentPage = ref(1)
const router = useRouter()
const route = useRoute()

const areas = ref([])
const pages = ref(0)

if (route.query["page"] !== undefined) {
	currentPage.value = Number(route.query["page"])
}

function updatePagination() {
	window.history.replaceState({}, "", `/index?page=${currentPage.value}`)
	area.getAreaPagination(currentPage.value - 1).then(response => {
		areas.value = response.pageContent
		pages.value = response.numberOfPages
		console.log(pages.value)
	})
}
updatePagination()

watch(currentPage, updatePagination)

</script>

<template>
	<div>
		<o-contained-grid>
			<o-space-card
				v-for="area in areas"
				:area="area"
			/>
		</o-contained-grid>
		<v-pagination
			v-model="currentPage"
			:length="pages"
		/>
	</div>
</template>