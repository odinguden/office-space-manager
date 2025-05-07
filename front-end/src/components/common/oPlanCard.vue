<script setup>
import fetcher from '@/plugins/fetcher';
import { timeUtil } from '@/plugins/timeUtil';
import { computed } from 'vue';
import { useDate } from 'vuetify';

const vDate = useDate()

const props = defineProps({
	plan: Object
})

function getDateText(input) {
	const inputAsDate = timeUtil.parseJavaLocalDate(input)

	return vDate.format(inputAsDate, "fullDate")

}

const startText = computed(() => getDateText(props.plan.start))
const endText = computed(() => getDateText(props.plan.end))

function onDelete() {
	fetcher.deletePlan(props.plan.id)
}
</script>

<template>
	<o-flat-card>
		<v-card-title>
			{{ plan.areaName }}
		</v-card-title>
		<v-card-subtitle>
			<b>{{ startText }}</b> to <b>{{ endText }}</b>
		</v-card-subtitle>
		<v-card-actions style="justify-content: end">
			<o-delete-button
				text="Cancel"
				prepend-icon="mdi-calendar-minus"
				@delete="onDelete"
			/>
		</v-card-actions>
	</o-flat-card>
</template>