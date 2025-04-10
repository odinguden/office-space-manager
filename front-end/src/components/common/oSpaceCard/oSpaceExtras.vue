<script setup>
const mockExtras = [
	{
		icon: "mdi-monitor",
		title: "Monitor",
		desc: "a desktop computer screen"
	},
	{
		icon: "mdi-laptop",
		title: "Office laptop",
		desc: "an office provided laptop"
	},
	{
		icon: "mdi-keyboard",
		title: "Computer equipment",
		desc: "a set of office provided computer equipment"
	},
	{
		icon: "mdi-curtains",
		title: "Window",
		desc: "a nearby window"
	},
	{
		icon: "mdi-headset",
		title: "Headset",
		desc: "an office provided headset"
	}
]
</script>

<template>
	<div class="space-extras">
		<v-tooltip v-for="extra in mockExtras">
			<template v-slot:activator="{ props }">
				<v-icon v-bind="props">
					{{ extra.icon }}
				</v-icon>
			</template>
			<span>
				{{ extra.title }}
				<br>
				This space comes equipped with {{ extra.desc }}
			</span>
		</v-tooltip>
	</div>
</template>

<style scoped lang="scss">
.space-extras {
	display: flex;
	justify-content: start;
	flex-wrap: wrap;
	gap: 4px;
}
</style>