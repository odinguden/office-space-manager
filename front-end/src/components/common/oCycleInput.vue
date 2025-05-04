<script setup>
import { computed, watch } from 'vue'

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
	"max": Number, // The maximum value of this input
	"min": Number, // The minimum value of this input
	"cycle": Boolean, // Whether or not the input should loop
	"ariaName": { // An aria friendly name used to determine aria labels
		type: String,
		default: "value"
	}
})


// Since model updates a tick late, we need to store
// a local version in order to ensure proper handling
const internalValue = ref(model.value)

// This computed property ensures that both the internal
// and model value are set correctly
const computedValue = computed({
	get() {
		return internalValue.value
	},
	set(newValue) {
		internalValue.value = newValue
		model.value = newValue
	}
})

watch(model, (newValue) => {
	internalValue.value = newValue
})

// Checks whether the current value exceeds any boundaries
function checkBoundaries() {
	// If it's a loopign component, we don't correct anything
	if (!props.cycle) {
		if (props.max !== undefined) {
			// Clamp the value to the max value
			computedValue.value = Math.min(computedValue.value, props.max)
		}
		if (props.min !== undefined) {
			// Clamp the value to the min value
			computedValue.value = Math.max(computedValue.value, props.min)
		}
	}
}

// Checks whether to loop the current value if this is a loopable
// component.
function checkCycle() {
	if (props.cycle && props.max !== undefined && props.min !== undefined) {
		// Emit events as necessary and loop values
		if (computedValue.value > props.max) {
			emit("on-cycle-increase")
			computedValue.value = props.min
		} else if (computedValue.value < props.min) {
			emit("on-cycle-decrease")
			computedValue.value = props.max
		}
	}
}

// Changes the value and emits relevant events
function change(amount) {
	if (amount > 0) {
		emit("on-increase")
	} else if (amount < 0) {
		emit("on-decrease")
	}

	computedValue.value += amount

	checkBoundaries()
	checkCycle()
}

// Checks whether the right arrow should be disabled
const addDisabled = computed(() => {
	return !props.cycle // Never disable when the component is cycleable
		&& props.max !== undefined // Never disable if no max exists
		&& computedValue.value >= props.max // Disable if the current value is at max
})

// Checks whether the left arrow should be disabled
const subDisabled = computed(() => {
	// Almost identical logic to above
	return !props.cycle
		&& props.min !== undefined
		&& computedValue.value <= props.min
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
			:disabled="subDisabled"
			@click="change(-1)"
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
			:disabled="addDisabled"
			@click="change(1)"
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