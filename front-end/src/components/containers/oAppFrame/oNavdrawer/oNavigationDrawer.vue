<script setup>
// Specification for the different menu items to navigate to.
const NAV_TARGETS = {
	default: [
		{
			name: "Home",
			icon: "mdi-home",
			link: "/"
		},
		{
			name: "My space",
			icon: "mdi-desk",
			link: "/room/1"
		},
		{
			name: "My bookings",
			icon: "mdi-receipt-text",
			link: null
		},
		{
			name: "My plans",
			icon: "mdi-clock",
			link: null
		}
	],
	admin: [
		{
			name: "Admin Panel",
			icon: "mdi-account-hard-hat",
			link: null
		},
		{
			name: "My Superspace",
			icon: "mdi-domain",
			link: null
		}
	]
}
</script>

<template>
	<v-list>
		<v-list-item
			v-for="target in NAV_TARGETS.default"
			:key="target.name"
			:title="target.name"
			:to="target.link || undefined"
			:prepend-icon="target.icon"
			:disabled="target.link === null"
			color="primary"
			class="py-4"
		/>
		<v-divider class="my-3" />
		<v-list-item
		v-for="target in NAV_TARGETS.admin"
			:key="target.name"
			:title="target.name"
			:to="target.link || undefined"
			:prepend-icon="target.icon"
			:disabled="target.link === null"
			color="primary"
			class="py-4"
		/>
	</v-list>
</template>