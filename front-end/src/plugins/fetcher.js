import { BACKEND_URL } from "./config";
import { timeUtil } from "./timeUtil";

const AREA_TYPE_URL = BACKEND_URL + "/area-type"
const AREA_FEATURE_URL = BACKEND_URL + "/area-feature"

const AREA_URL = BACKEND_URL + "/area"
const ALL_AREAS_URL = AREA_URL + "/home"
const MY_AREAS_URL = AREA_URL + "/user"
const SUPERAREAS_URL = AREA_URL + "/superareas"

const RESERVATION_URL = BACKEND_URL + "/reservation"
const RESERVATION_MAKE_URL = RESERVATION_URL + "/make"
const RESERVATION_AREA_URL = RESERVATION_URL + "/area"
const MY_RESERVATIONS_URL = RESERVATION_URL + "/user"

const SEARCH_URL = BACKEND_URL + "/search"

const PLANS_URL = BACKEND_URL + "/plan"
const USER_PLANS_URL = PLANS_URL + "/user"

const USER_URL = BACKEND_URL + "/user"
const WHOAMI_URL = USER_URL + "/whoami"

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

async function doFetch(URL, content={}) {
	const body = { ...DEFAULT_BODY, ...content }
	return fetch(URL, body)
		.catch(error => console.error(`Failed to fetch from ${URL} with method ${body.method}:`, error))
}

export default {
	async getAreaPagination(page) {
		return doFetch(ALL_AREAS_URL + `?page=${page}`)
			.then(response => response.json())
	},

	async getArea(id) {
		return doFetch(AREA_URL + `/${id}`)
			.then(response => response.json())
	},

	async getAreaTypes() {
		return doFetch(AREA_TYPE_URL)
			.then(response => response.json())
	},

	async getAreaFeatures() {
		return doFetch(AREA_FEATURE_URL)
			.then(response => response.json())
	},

	async getSearchResultsWithParamList(params, page) {
		params["page"] = page || 0
		return doFetch(SEARCH_URL + paramListToUrlAppendage(params))
			.then(response => response.json())
	},

	async getReservationsForAreaInTime(areaId, startTime, endTime) {
		startTime = timeUtil.formatDateTimeForSend(startTime)
		endTime = timeUtil.formatDateTimeForSend(endTime)
		return doFetch(`${RESERVATION_AREA_URL}/${areaId}?start=${startTime}&end=${endTime}`)
			.then(response => response.json())
	},

	async getReservationFrequencyForMonth(areaId, year, month) {
		return doFetch(`${RESERVATION_AREA_URL}/${areaId}/frequency/list?year=${year}&month=${month}`)
			.then(response => response.json())
	},

	async tryMakeReservation(areaId, startTime, endTime, comment="") {
		const requestBody = {
			method: "POST",
			headers: {
				"Content-type": "application/json"
			},
			body: JSON.stringify({
				roomId: areaId,
				startTime: timeUtil.formatDateTimeForSend(startTime),
				endTime: timeUtil.formatDateTimeForSend(endTime),
				comment
			})
		}
		return doFetch(RESERVATION_MAKE_URL, requestBody)
	},

	async whoAmI() {
		return doFetch(WHOAMI_URL)
			.then(response => {
				if (response.ok) {
					return response.json()
				}
				return null
			})
	},

	async getMyAreas(me, page) {
		if (me === null) {
			return new Promise()
		}
		return doFetch(`${MY_AREAS_URL}/${me.userId}?page=${page}`)
			.then(response => response.json())
	},

	async getMyBookings(me) {
		if (me === null) {
			return new Promise()
		}

		return doFetch(`${MY_RESERVATIONS_URL}/me`)
			.then(response => response.json())
	},

	async deleteBooking(id) {
		const body = { method: 'DELETE' }
		return doFetch(`${RESERVATION_URL}/${id}`, body)
	},

	async tryCreatePlan(areaId, startTime, endTime, name) {
		const body = {
			method: "POST",
			headers: {
				"Content-type": "application/json"
			},
			body: JSON.stringify({
				areaId,
				start: timeUtil.formatDateForSend(startTime),
				end: timeUtil.formatDateForSend(endTime),
				name
			})
		}

		return doFetch(PLANS_URL, body)
	},

	async getAllUsersPlans(userId) {
		return doFetch(`${USER_PLANS_URL}/${userId}`)
			.then(response => response.json())
	},

	async deletePlan(id) {
		const body = { method: 'DELETE' }
		return doFetch(`${PLANS_URL}/${id}`, body)
	},

	async getSuperAreasByName(name="") {
		return doFetch(`${SUPERAREAS_URL}?name=${name}`)
			.then(response => response.json())
	}
}