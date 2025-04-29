import { BACKEND_URL } from "./config";

const ALL_AREAS_URL = BACKEND_URL + "/area/home"

const DEFAULT_BODY = {
	method: "GET"
}

export default {
	async getAreaPagination(page) {
		return fetch(ALL_AREAS_URL + `?page=${page}`)
			.then(response => response.json())
	}
}