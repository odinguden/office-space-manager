<template>
	<v-main id="main">
		<o-app-frame />
		<router-view id="main-view" />
		<o-footer />
	</v-main>
</template>

<style scoped lang="scss">
body {
	min-height: 100vh;
}

#main {
	display:flex;
	flex-direction: column;

	#main-view {
		flex-grow: 1;
		max-width: 100vw;
		padding: 24px 16px;
	}
}
</style>