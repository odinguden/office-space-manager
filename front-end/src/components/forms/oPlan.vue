<script setup>
import fetcher from '@/plugins/fetcher';
import { useDate, useDisplay } from 'vuetify';
const { mobile } = useDisplay()
const vDate = useDate()
const emit = defineEmits(["close"])
const props = defineProps({
	areaId: String
})

const DAY_SELECTS = [
	'One day',
	'Several days'
]

const rangeType = ref(DAY_SELECTS[0])
const startDate = ref(new Date())
const endDate = ref(new Date())
const comment = ref("")

const loading = ref(false)

const bothDates = computed({
	get() {
		return startDate.value
	},
	set(value) {
		startDate.value = value
		endDate.value = value
	}
})

const nowDate = computed(() => {
	const nowDate = new Date()
	nowDate.setHours(0,0,0,0)
	return nowDate
})

function isDateBeforeOrEqual(date, comparing) {
	return vDate.isBefore(date, comparing) || vDate.isEqual(date, comparing)
}

const validators = {
	startDate: () => {
		const messages = []

		if (startDate.value == null) {
			messages.push("Start date is required")
		}
		if (!isDateBeforeOrEqual(nowDate.value, startDate.value)) {
			messages.push("Start date cannot be in the past")
		}

		return messages
	},
	endDate: () => {
		const messages = []

		if (endDate.value == null) {
			messages.push("End date is required")
		}
		if (!isDateBeforeOrEqual(startDate.value, endDate.value)) {
			messages.push("End date needs to be after start date")
		}

		return messages
	},
	comment: () => {
		const messages = []

		if (comment.value == null || comment.value == "") {
			messages.push("Comment is required")
		}
		if (comment.value.length > 80) {
			messages.push("Comment is longer than 80 characters")
		}

		return messages
	}
}

const isValid = computed(() => {
	return !Object.values(validators)
		.some((val) => val().length > 0)
})

function trySubmit() {
	loading.value = true
	fetcher.tryCreatePlan(props.areaId, startDate.value, endDate.value, comment.value)
		.then(() => window.location.reload())
		.finally(() => loading.value = false)
}
</script>

<template>
	<v-form class="plan-form" :class="{ mobile }">
		<v-defaults-provider
			:defaults="{
				global: {
					variant: 'outlined',
					hideDetails: 'auto'
				}
			}"
		>
			<v-select
				v-model="rangeType"
				:items="DAY_SELECTS"
			/>
			<div
				v-if="rangeType == DAY_SELECTS[0]"
			>
				<o-date-input
					v-model="bothDates"
					label="Date"
					:error-messages="validators.startDate()"
				/>
			</div>
			<div
				v-else
				class="h-input-group"
			>
				<o-date-input
					v-model="startDate"
					label="From"
					:error-messages="validators.startDate()"
				/>
				<o-date-input
					v-model="endDate"
					label="To"
					:error-messages="validators.endDate()"
				/>
			</div>
			<v-text-field
				v-model="comment"
				counter="80"
				label="Comment"
				:error-messages="validators.comment()"
			/>
			<div class="h-input-group reverse-on-mobile">
				<v-btn
					:loading="loading"
					text="cancel"
					@click="emit('close')"
				/>
				<v-btn
					text="make plan"
					variant="flat"
					color="primary"
					:disabled="!isValid"
					@click="trySubmit"
				/>
			</div>
		</v-defaults-provider>
	</v-form>
</template>

<style scoped lang="scss">
.plan-form {
	display: flex;
	flex-direction: column;
	gap: 12px;

	.h-input-group {
		display: grid;
		grid-auto-columns: 1fr;
		grid-auto-rows: 1fr;
		grid-auto-flow: column;
		align-items: start;
		text-align: center;
		gap: 16px;
	}

	&.mobile {
		.h-input-group {
			display: flex;
			flex-direction: column;
			align-items: stretch;

			&.reverse-on-mobile {
				flex-direction: column-reverse;
			}
		}
	}
}
</style>