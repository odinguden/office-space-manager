<script setup>
import { useDisplay } from 'vuetify';

const { mobile } = useDisplay()
</script>

<template>
	<v-spacer />
	<footer class="footer" :class="{ 'mobile': mobile }">
		<section class="ownership">
			<p>
				This app was developed for Ålesund Kommune by students at the Norwegian University
				of Science and Technology
			</p>
			<div class="ownership-images">
				<img
					src="https://alesund.kommune.no/kunde/grafikk/a6e11.logo-black.svg"
					alt="Ålesund Kommune Logo"
				>
				<img
					src="https://www.ntnu.edu/o/ntnu-theme/images/logo_ntnu.svg"
					alt="Norwegian University of Science and Technology Logo"
				>
			</div>
		</section>
	</footer>
</template>

<style scoped lang="scss">
footer {
	display: grid;
	grid-template-columns: 1fr;
	gap: 24px;
	padding: 24px;
	width: 100%;
	border-top: thin solid rgba(var(--v-border-color), var(--v-border-opacity));

	&.mobile {
		display: flex;
		flex-direction: column;
		gap: 16px;

		.ownership > p {
			text-align: center;
		}

		.ownership .ownership-images {
			display: flex;
			flex-direction: column;
			align-self: center;
			gap: 12px;
		}
	}

	.ownership {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8px;

		> p {
			text-align: center;
			color: rgba(var(--v-theme-on-surface), var(--v-medium-emphasis-opacity));
		}

		.ownership-images {
			display: grid;
			grid-template-columns: 1fr 1fr;
			justify-items: center;
			gap: 12px;
			align-items: center;

			img {
				max-height: 3rem;
			}
		}
	}
}
</style>