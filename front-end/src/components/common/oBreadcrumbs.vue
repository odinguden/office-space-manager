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
			<router-link
				v-if="item.link"
				:to="item.link"
			>
				<v-icon v-if="item.icon !== undefined">
					{{ item.icon }}
				</v-icon>
				{{ item.text }}
			</router-link>
			<span v-else>
				<v-icon v-if="item.icon !== undefined">
					{{ item.icon }}
				</v-icon>
				{{ item.text }}
			</span>
		</span>
	</div>
</template>

<style scoped lang="scss">
.breadcrumbs > span::before {
	content: ' > ';
	opacity: var(--v-medium-emphasis-opacity);
}
</style>