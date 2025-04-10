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
	<v-form class="search-form">
		<v-defaults-provider
			:defaults="{
				global: {
					variant: 'outlined'
				},
				VTextField: {
					density: 'compact',
					hideDetails: 'auto'
				}
			}"
		>
			<v-btn-toggle
				color="primary"
				mandatory
			>
				<v-btn
					v-for="type in ROOM_TYPES"
					stacked
					:prepend-icon="type.icon"
					size="small"
					density="compact"
				>
					{{ type.name }}
				</v-btn>
			</v-btn-toggle>
			<v-text-field
				clearable
				label="Date"
				placeholder="Any"
				persistent-placeholder
				readonly
				@click="console.log('Open calendar')"
			>
				<template v-slot:append>
					<v-btn
						class="faded-button"
						icon
						variant="outlined"
						density="default"
						size="small"
					>
						<v-icon>
							mdi-calendar
						</v-icon>
					</v-btn>
				</template>
			</v-text-field>
			<v-divider />
			<v-text-field
				type="number"
				min="1"
				placeholder="1"
				persistent-placeholder
				label="Capacity"
			/>
			<v-divider/>
			<div class="time-form">
				<v-label>
					Time Between
				</v-label>
				<v-text-field
					type="time"
				/>
				<v-label>and</v-label>
				<v-text-field
					type="time"
				/>
			</div>
			<v-text-field
				type="time"
				label="Duration"
				placeholder="07:00"
				persistent-placeholder
			/>
			<v-divider />
			<v-autocomplete
				density="compact"
				label="Location"
				placeholder="Nearby"
				prepend-inner-icon="mdi-magnify"
				persistent-placeholder
				hide-details="auto"
			/>
			<v-divider />
			<v-autocomplete
				chips
				closable-chips
				density="compact"
				label="Features"
				placeholder="Type to search"
				prepend-inner-icon="mdi-magnify"
			/>
			<v-spacer />
			<v-btn
				color="primary"
				prepend-icon="mdi-magnify"
				text="Search"
				type="submit"
			/>
		</v-defaults-provider>
	</v-form>
</template>

<style scoped lang="scss">
.search-form {
	padding: 16px;

	display: flex;
	flex-direction: column;
	gap: 16px;
	min-height: 100%;

	.time-form {
		display: grid;
		gap: 0 8px;
		grid-template-rows: auto auto;
		grid-template-columns: 1fr auto 1fr;
		label:first-child {
			font-size: 0.75rem;
			grid-column: 1 / span 3;
		}
	}
}

.faded-button {
	border-color: rgba(var(--v-border-color), 0.38);
	border-radius: 4px;
}
</style>


<style lang="scss">
.search-form .v-input {
	flex: none; // Without this, stuff looks like crap with min-height 100%
}
</style>