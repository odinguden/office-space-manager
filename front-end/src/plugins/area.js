import { BACKEND_URL } from "./config";

const BASE_URL = BACKEND_URL + "/area"

const ALL_AREAS_URL = BASE_URL + "/home"

const DEFAULT_BODY = {
	method: "GET"
}

export default {
	async getAreaPagination(page) {
		return fetch(ALL_AREAS_URL + `?page=${page}`)
			.then(response => response.json())
	},

	async getArea(id) {
		return fetch(BASE_URL + `/${id}`)
			.then(response => response.json())
	}
}