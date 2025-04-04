<template>
	<section>
		<h1>My Plans</h1>
		<o-contained-grid>
			<o-space-card v-for="_ in Array(4)">
				<template #timeline>
					<o-calendar />
				</template>
			</o-space-card>
		</o-contained-grid>
	</section>
</template>