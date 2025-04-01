<script setup>
const props = defineProps({
	items: Array,
	divider: {
		type: String,
		default: ">"
	}
})
</script>

<template>
	<div class="breadcrumbs" :style="{'--divider': '>'}">
		<span v-for="item, idx in items">
			<router-link to="/room/0">
				<v-icon v-if="item instanceof Object && item.icon !== undefined">
					{{ item.icon }}
				</v-icon>
				{{ item instanceof Object ? item.text : item }}
			</router-link>
		</span>
	</div>
</template>

<style scoped lang="scss">
.breadcrumbs {
	> span:not(:last-child) {
		&::after {
			content: ' > ';
			opacity: var(--v-medium-emphasis-opacity)
		}
	}
}
</style>