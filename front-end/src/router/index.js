/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import { createRouter, createWebHistory } from 'vue-router/auto'
import { setupLayouts } from 'virtual:generated-layouts'

import Index from '@/pages/index.vue'
import Room from '@/pages/room.vue'
import Bookings from '@/pages/bookings.vue'
import Plans from '@/pages/plans.vue'
import Search from '@/pages/search.vue'
import Spaces from '@/pages/spaces.vue'

const routes = [
	{
		path: "/",
		name: "/",
		component: Index
	},
	{
		path: "/index",
		name: "index",
		component: Index
	},
	{
		path: "/room/:id",
		name: "room",
		component: Room
	},
	{
		path: "/spaces",
		name: "spaces",
		component: Spaces
	},
	{
		path: "/bookings",
		name: "bookings",
		component: Bookings
	},
	{
		path: "/plans",
		name: "plans",
		component: Plans
	},
	{
		path: "/search",
		name: "search",
		component: Search
	}
]

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: setupLayouts(routes),
})

// Workaround for https://github.com/vitejs/vite/issues/11804
router.onError((err, to) => {
	if (err?.message?.includes?.('Failed to fetch dynamically imported module')) {
		if (!localStorage.getItem('vuetify:dynamic-reload')) {
			console.log('Reloading page to fix dynamic import error')
			localStorage.setItem('vuetify:dynamic-reload', 'true')
			location.assign(to.fullPath)
		} else {
			console.error('Dynamic import error, reloading page did not fix it', err)
		}
	} else {
		console.error(err)
	}
})

router.isReady().then(() => {
	localStorage.removeItem('vuetify:dynamic-reload')
})

export default router