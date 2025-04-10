<script setup>
const PLANS_DESC = `
Plans are sets of rules that automatically make bookings for you, whether it be monthly, weekly,
or even daily. Creating effective plans ensures that you always have the right room when you need
it. To create a new plan, press the "Make a new Plan" button.
`

const PLANS_DISCLAIMER = `
DISCLAIMER: Although plans aim to be as effective as possible, there is no guarantee that setting a
plan will always get you a room. The narrower the requirements for your plan, the less likely it is
that you will always find a room that matches your wishes. To maximize the possibility of
automatically getting a room, avoid rules that aren't strictly necessary.
`
</script>

<template>
	<section>
		<header class="plan-header">
			<h1>My Plans</h1>
			<abbr title="" class="plan-desc">
				What are plans?
				<o-closeable-dialog
					activator="parent"
					title="What are plans?"
					max-width="356px"
				>
					 <v-card-text>
						<p>
							{{ PLANS_DESC }}
						</p>
						<p class="disclaimer">
							{{ PLANS_DISCLAIMER }}
						</p>
					 </v-card-text>
				</o-closeable-dialog>
			</abbr>
			<v-spacer />
			<v-btn color="primary" variant="outlined">
				Make a new plan
			</v-btn>
		</header>
		<o-contained-grid min-width="400px">
			<o-space-card v-for="_ in Array(4)">
				<template #timeline>
					<o-calendar />
				</template>
			</o-space-card>
		</o-contained-grid>
	</section>
</template>

<style scoped lang="scss">
header.plan-header {
	display: flex;
	align-items: baseline;
	gap: 16px;
	margin-bottom: 1rem;
}

.plan-desc {
	cursor: pointer;
}

.disclaimer {
	margin-top: 1rem;
	font-size: 0.8rem;
	color: rgba(var(--v-theme-on-surface), var(--v-medium-emphasis-opacity));
}
</style>