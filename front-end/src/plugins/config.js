const BACKEND_URL = "http://localhost:8080"
const LOGIN_URI = "/login"
const LOGOUT_URI = "/logout"
const TYPE_TO_ICON_MAPPING = {
	"desk": "mdi-desk",
	"room": "mdi-theater",
	"floor": "mdi-stairs",
	"building": "mdi-domain",
	"other": "mdi-help-box"
}

const FEATURE_TO_ICON_MAPPING = {
	"screen": "mdi-monitor",
	"camera": "mdi-camera",
	"microphone": "mdi-microphone",
	"no elevator access": "mdi-elevator-passenger-off",
	"wheelchair accessible": "mdi-wheelchair-accessibility",
	"parking": "mdi-parking",
	"no wifi": "mdi-wifi-off",
	"other": "mdi-help-box"
}

export { BACKEND_URL }
export const LOGIN_ENDPOINT = BACKEND_URL + LOGIN_URI
export const LOGOUT_ENDPOINT = BACKEND_URL + LOGOUT_URI
export const TYPE_ICON_MAPPINGS = TYPE_TO_ICON_MAPPING
export const FEATURE_ICON_MAPPINGS = FEATURE_TO_ICON_MAPPING