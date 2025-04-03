<template>
	<v-main id="main">
		<o-app-frame />
		<router-view id="main-view" />
		<o-footer />
	</v-main>
</template>

<style scoped lang="scss">
#main {
	#main-view {
		max-width: 100vw;
		padding: 24px 16px;
	}
}
</style>