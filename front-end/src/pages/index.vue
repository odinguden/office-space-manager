<script setup>
const defaultContent = Array(6)

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
	<div class="home-section-container">
		<section v-for="section in sections" class="card-section mt-3">
			<h1>{{ section.title }}</h1>
			<div class="card-container-wrapper">
				<div class="card-container">
					<o-space-card v-for="_ in section.content" />
					<o-flat-card class="see-more-card" :to="section.link">
						<h1><router-link :to="section.link">See more...</router-link></h1>
					</o-flat-card>
				</div>
			</div>
		</section>
	</div>
</template>

<style scoped lang="scss">
.section-header {
	display: flex;
}

.home-section-container {
	display: flex;
	flex-direction: column;
	gap: 16px;
}

.card-container {
	position: relative;
	display: flex;
	overflow-x: auto;
	gap: 16px 24px;
	padding: 8px 0 16px 0;


	> * {
		flex: 0 0 328px;
	}
}

.see-more-card {
	display: flex;
	align-items: center;
	justify-content: center;
}
</style>