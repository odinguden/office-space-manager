/**
 * plugins/vuetify.js
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Composables
import { createVuetify } from 'vuetify'
import { VTimePicker } from 'vuetify/labs/components'

const appTheme = {
	dark: false,
	colors: {
		primary: "#5F0F40",
		warning: "#EFC88B",
		'background': '#F9F9F9',
		'on-background': "#121212",
		'surface': '#f9f9f9',
		'on-surface': "#1d2f6f",
	}
}

// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
	theme: {
		defaultTheme: 'appTheme',
		themes: {
			appTheme
		}
	},
	display: {
		mobileBreakpoint: "md"
	},
	components: {
		VTimePicker
	}
})
