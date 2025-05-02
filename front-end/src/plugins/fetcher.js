import { BACKEND_URL } from "./config";

const AREA_TYPE_URL = BACKEND_URL + "/area-type"
const AREA_FEATURE_URL = BACKEND_URL + "/area-feature"
const AREA_URL = BACKEND_URL + "/area"
const ALL_AREAS_URL = AREA_URL + "/home"
const SEARCH_URL = BACKEND_URL + "/search"

const DEFAULT_BODY = {
	method: "GET"
}

function paramListToUrlAppendage(paramList) {
	const urlList = []
	for (let [idx, value] of Object.entries(paramList)) {
		urlList.push(`${idx}=${value}`)
	}

	return "?" + urlList.join("&")
}

export default {
	async getAreaPagination(page) {
		return fetch(ALL_AREAS_URL + `?page=${page}`)
			.then(response => response.json())
	},

	async getArea(id) {
		return fetch(AREA_URL + `/${id}`)
			.then(response => response.json())
	},

	async getAreaTypes() {
		return fetch(AREA_TYPE_URL)
			.then(response => response.json())
	},

	async getAreaFeatures() {
		return fetch(AREA_FEATURE_URL)
			.then(response => response.json())
	},

	async getSearchResultsWithParamList(params, page) {
		params["page"] = page || 0
		return fetch(SEARCH_URL + paramListToUrlAppendage(params))
			.then(response => response.json())
	}
}