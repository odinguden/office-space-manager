<script setup>
import { computed } from 'vue'
import { useDate } from 'vuetify'
import { useRouter } from 'vue-router'
import fetcher from '@/plugins/fetcher'
import { useSearchStore } from '@/stores/search'
import { TYPE_ICON_MAPPINGS } from '@/plugins/config'

const vDate = useDate()
const router = useRouter()
const search = useSearchStore()
search.setDateUtil(vDate)

// The different types of rooms permissible for search
const ROOM_TYPES = [
	{
		name: "Rooms",
		value: "room",
		icon: "mdi-theater"
	},
	{
		name: "Desks",
		value: "desk",
		icon: "mdi-desk"
	},
	{
		name: "All",
		value: "all",
		icon: "mdi-dots-horizontal"
	}
]

const areaFeatures = ref([])
const areaTypes = ref([])
const superAreas = ref([])

function fetchAreaFeatures() {
	if (areaFeatures.value.length == 0) {
		fetcher.getAreaFeatures().then(response => areaFeatures.value = response)
	}
}
function fetchSuperAreas(name) {
	if (typeof(name) !== "string") return;
	fetcher.getSuperAreasByName(name)
		.then(response => superAreas.value = response)
}
function fetchAreaTypes() {
	fetcher.getAreaTypes().then(response => areaTypes.value = response)
}
fetchAreaTypes()
fetchSuperAreas()

const computedDate = computed({
	get() {
		return new Date(search.date)
	},
	set(newValue) {
		const year = newValue.getFullYear();
		const month = String(newValue.getMonth() + 1).padStart(2, "0")
		const day = String(newValue.getDate()).padStart(2, "0")

		search.date = `${year}-${month}-${day}`
	}
})

const durationPlaceholder = computed(() => {
	return search.defaultDuration
})

function getIcon(typeName) {
	if (typeName in TYPE_ICON_MAPPINGS) {
		return TYPE_ICON_MAPPINGS[typeName]
	}

	return TYPE_ICON_MAPPINGS["other"]
}

function onDateClicked() {
	if (search.date == null) {
		computedDate.value = new Date()
	}
}

function onSubmit() {
	if (search.validate()) {
		router.push({name: 'search', query: search.createQueryParams()})
	}
}
</script>

<template>
	<v-form
		class="search-form"
		@submit.prevent="onSubmit"
	>
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
				v-model="search.areaType"
				color="primary"
				mandatory
			>
				<v-btn
					v-for="type in ROOM_TYPES"
					stacked
					:value="type.value"
					:prepend-icon="type.icon"
					size="small"
					density="compact"
				>
					{{ type.name }}
				</v-btn>
			</v-btn-toggle>
			<v-text-field
				v-model="search.capacity"
				:error-messages="search.errorMessages.capacity"
				type="number"
				min="1"
				placeholder="1"
				persistent-placeholder
				label="Capacity"
			/>
			<v-divider/>
			<v-text-field
				v-model="search.date"
				:error-messages="search.errorMessages.date"
				clearable
				label="Date"
				placeholder="Today"
				persistent-placeholder
				:type="search.date == undefined ? 'text' : 'date'"
				@click="onDateClicked"
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
			<div class="time-form">
				<v-label>
					Time Between
				</v-label>
				<o-time-input
					v-model="search.timeStart"
					:error-messages="search.errorMessages.timeStart"
				/>
				<v-label>and</v-label>
				<o-time-input
					v-model="search.timeEnd"
					:error-messages="search.errorMessages.timeEnd"
				/>
			</div>
			<o-duration-input
				v-model="search.duration"
				:error-messages="search.errorMessages.duration"
				:placeholder="durationPlaceholder"
			/>
			<v-divider />
			<v-autocomplete
				v-model="search.location"
				:error-messages="search.errorMessages.location"
				:items="superAreas"
				item-title="name"
				item-value="id"
				density="compact"
				label="Location"
				placeholder="Any"
				prepend-inner-icon="mdi-magnify"
				persistent-placeholder
				hide-details="auto"
				@update:search="fetchSuperAreas"
			>
				<template v-slot:item="{ props, item }">
					<v-list-item
						v-bind="props"
						:prepend-icon="getIcon(item.raw.areaType.id)"
						:title="item.raw.name"
					/>
				</template>
			</v-autocomplete>
			<v-divider />
			<v-autocomplete
				v-model="search.features"
				:error-messages="search.errorMessages.features"
				:items="areaFeatures"
				no-filter
				item-title="id"
				multiple
				chips
				closable-chips
				density="compact"
				label="Features"
				placeholder="Type to search"
				prepend-inner-icon="mdi-magnify"
				@update:focused="fetchAreaFeatures"
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