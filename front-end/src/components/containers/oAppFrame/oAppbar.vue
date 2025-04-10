<template>
	<v-app-bar elevation="1">
		<slot name="prepend" />
		<v-spacer />
		<slot />
		<v-spacer />
		<slot name="append" />
	</v-app-bar>
</template>