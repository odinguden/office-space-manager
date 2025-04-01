<script setup>
import { computed } from 'vue'

const emit = defineEmits([
	"on-increase",
	"on-decrease",
	"on-cycle-increase",
	"on-cycle-decrease"
])

const model = defineModel({
	"modelValue": {
		type: Number,
		default: 0
	}
})

const props = defineProps({
	"max": Number,
	"min": Number,
	"cycle": Boolean,
	"ariaName": String
})


const internalValue = ref(model.value)

const computedValue = computed({
	get() {
		return internalValue.value
	},
	set(newValue) {
		console.log(newValue)
		internalValue.value = newValue
		model.value = newValue
	}
})

function checkBoundaries() {
	if (!props.cycle) {
		if (props.max !== undefined) {
			computedValue.value = Math.min(computedValue.value, props.max)
		}
		if (props.min !== undefined) {
			computedValue.value = Math.max(computedValue.value, props.min)
		}
	}
}

function checkCycle() {
	if (props.cycle && props.max !== undefined && props.min !== undefined) {
		if (computedValue.value > props.max) {
			emit("on-cycle-increase")
			computedValue.value = props.min
		} else if (computedValue.value < props.min) {
			emit("on-cycle-decrease")
			computedValue.value = props.max
		}
	}
}

function increase() {
	emit("on-increase")
	computedValue.value++;

	checkBoundaries()
	checkCycle()
}

function decrease() {
	emit("on-decrease")
	computedValue.value--;

	checkBoundaries()
	checkCycle()
}

const maxDisabled = computed(() => {
	return !props.cycle && props.max !== undefined && computedValue.value >= props.max
})

const minDisabled = computed(() => {
	return !props.cycle && props.min !== undefined && computedValue.value <= props.min
})

</script>

<template>
	<div class="cycle-input">
		<v-btn
			icon="mdi-chevron-left"
			class="fixed-width-btn"
			color="primary"
			variant="text"
			density="comfortable"
			:aria-label="'Previous ' + props.ariaName"
			tile
			flat
			v-bind="$attrs"
			:disabled="minDisabled"
			@click="decrease"
		/>
		<v-divider vertical />
		<v-spacer />
		<slot />
		<v-spacer />
		<v-divider vertical />
		<v-btn
			icon="mdi-chevron-right"
			class="fixed-width-btn"
			color="primary"
			variant="text"
			density="comfortable"
			:aria-label="'Next ' + props.ariaName"
			tile
			flat
			v-bind="$attrs"
			:disabled="maxDisabled"
			@click="increase"
		/>
	</div>
</template>

<style scoped lang="scss">
.cycle-input {
	display: flex;
	align-items: center;

	font-weight: 500;

	.fixed-width-btn {
		width: 44px;
	}
}
</style>