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
			name: "My spaces",
			icon: "mdi-desk",
			link: "/spaces"
		},
		{
			name: "My bookings",
			icon: "mdi-receipt-text",
			link: "/bookings"
		},
		{
			name: "My plans",
			icon: "mdi-calendar-clock",
			link: "/plans"
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