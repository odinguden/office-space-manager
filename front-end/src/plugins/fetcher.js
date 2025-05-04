import { BACKEND_URL } from "./config";

const AREA_TYPE_URL = BACKEND_URL + "/area-type"
const AREA_FEATURE_URL = BACKEND_URL + "/area-feature"

const AREA_URL = BACKEND_URL + "/area"
const ALL_AREAS_URL = AREA_URL + "/home"

const RESERVATION_URL = BACKEND_URL + "/reservation"
const RESERVATION_AREA_URL = RESERVATION_URL + "/area"

const SEARCH_URL = BACKEND_URL + "/search"

const DEFAULT_BODY = {
	method: "GET",
	credentials: "include"
}

function paramListToUrlAppendage(paramList) {
	const urlList = []
	for (let [idx, value] of Object.entries(paramList)) {
		urlList.push(`${idx}=${value}`)
	}

	return "?" + urlList.join("&")
}

function formatDate(input) {
	return input.toISOString().slice(0, 19)
}

export default {
	async getAreaPagination(page) {
		return fetch(ALL_AREAS_URL + `?page=${page}`, DEFAULT_BODY)
			.then(response => response.json())
	},

	async getArea(id) {
		return fetch(AREA_URL + `/${id}`, DEFAULT_BODY)
			.then(response => response.json())
	},

	async getAreaTypes() {
		return fetch(AREA_TYPE_URL, DEFAULT_BODY)
			.then(response => response.json())
	},

	async getAreaFeatures() {
		return fetch(AREA_FEATURE_URL, DEFAULT_BODY)
			.then(response => response.json())
	},

	async getSearchResultsWithParamList(params, page) {
		params["page"] = page || 0
		return fetch(SEARCH_URL + paramListToUrlAppendage(params), DEFAULT_BODY)
			.then(response => response.json())
	},

	async getReservationsForAreaInTime(areaId, startTime, endTime) {
		startTime = formatDate(startTime)
		endTime = formatDate(endTime)
		return fetch(`${RESERVATION_AREA_URL}/${areaId}?start=${startTime}&end=${endTime}`)
			.then(response => response.json())
	},

	async getReservationFrequencyForMonth(areaId, year, month) {
		return fetch(`${RESERVATION_AREA_URL}/${areaId}/frequency/list?year=${year}&month=${month}`)
			.then(response => response.json())
	}
}