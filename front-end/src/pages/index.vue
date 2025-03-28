<script setup>
const defaultContent = Array(12)

const sections = [
	{
		title: "Favorites",
		content: defaultContent,
		link: "/"
	},
	{
		title: "Popular",
		content: defaultContent,
		link: "/"
	},
	{
		title: "Recent",
		content: defaultContent,
		link: "/"
	}
]
</script>

<template>
	<div>
		<section v-for="section in sections">
			<div class="section-header">
				<h1>{{ section.title }}</h1>
				<v-spacer />
				<router-link :to="section.link">See more...</router-link>
			</div>
			<o-contained-grid>
				<o-space-card v-for="_ in section.content" />
			</o-contained-grid>
		</section>
	</div>
</template>

<style scoped lang="scss">
.section-header {
	display: flex;
}
</style>