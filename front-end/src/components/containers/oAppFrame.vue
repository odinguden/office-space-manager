<script setup>
import { useDisplay } from 'vuetify';
const { mobile } = useDisplay()

// Whatever state the navdrawer should be in
// Defaults to menu if desktop, but closed if mobile
const navdrawerState = ref(mobile.value ? undefined : 0)
</script>

<template>
	<o-appbar>
		<template v-slot:prepend>
			<o-navdrawer-controls v-model="navdrawerState" />
		</template>
		<template v-slot:append>
			<o-appbar-actions />
		</template>
	</o-appbar>
	<o-navdrawer v-model="navdrawerState" />
</template>