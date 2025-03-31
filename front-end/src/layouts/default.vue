<template>
	<v-main id="main">
		<o-app-frame />
		<router-view />
		<o-footer />
	</v-main>
</template>

<style scoped lang="scss">
#main {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: start;
}
</style>