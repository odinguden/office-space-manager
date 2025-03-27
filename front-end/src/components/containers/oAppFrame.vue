<template>
	<o-appbar>
		<template v-slot:prepend>
			<o-navdrawer-controls />
		</template>
		<template v-slot:append>
			<o-appbar-actions />
		</template>
	</o-appbar>
	<o-navdrawer />
</template>