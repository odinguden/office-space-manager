<template>
	<o-contained-grid>
		<o-space-card v-for="_ in Array(4)">
			<template #actions>
				<o-space-booked-actions />
			</template>
		</o-space-card>
	</o-contained-grid>
</template>