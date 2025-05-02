<script setup>
import fetcher from '@/plugins/fetcher';
import { useRoute } from 'vue-router';

const route = useRoute()
const result = ref({})
fetcher.getSearchResultsWithParamList(route.query)
	.then(response => result.value = response)
</script>

<template>
	<div>
		
	</div>
</template>