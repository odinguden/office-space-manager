<script setup>
// The different types of rooms permissible for search
const ROOM_TYPES = [
	{
		name: "Desks",
		icon: "mdi-desk"
	},
	{
		name: "Rooms",
		icon: "mdi-theater"
	},
	{
		name: "Other",
		icon: "mdi-dots-horizontal"
	}
]
</script>

<template>
	<o-search-form />
</template>