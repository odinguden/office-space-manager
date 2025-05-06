import { defineStore } from "pinia";
import { BACKEND_URL, LOGIN_ENDPOINT } from "@/plugins/config";
import fetcher from '@/plugins/fetcher';

const AUTH_STATES = {
	"UNCHECKED": 0,
	"UNAUTHORIZED": -1,
	"AUTHORIZED": 1,
	"PROBLEMATIC": -2
}


export const useAuthStore = defineStore('auth', {
	state: () => ({
		authState: AUTH_STATES.UNCHECKED,
		me: null
	}),

	getters: {
		AUTH_STATES: () => (AUTH_STATES),
		LOGIN_ENDPOINT: () => (BACKEND_URL + LOGIN_ENDPOINT)
	},

	actions: {
		updateState() {
			fetcher.whoAmI()
				.then(response => {
					this.me = null
					if (response === null) {
						this.authState = AUTH_STATES.UNAUTHORIZED
					} else if (response?.userId) {
						this.authState = AUTH_STATES.AUTHORIZED
						this.me = response
					} else {
						this.authState = AUTH_STATES.PROBLEMATIC
					}
				})
				.catch(() => {
					this.me = null
					this.authState = AUTH_STATES.PROBLEMATIC
				})

		}
	}
})