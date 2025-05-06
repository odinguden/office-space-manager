import { defineStore } from "pinia";

function formatDate(input) {
	return input.toISOString().slice(0, 19)
}

function parseTime(input) {
	const [hours, minutes] = input.split(":").map(Number)
	return {hours, minutes}
}

function formatDuration(input) {
	return `PT${input.hours}H${input.minutes}M`
}

export const useSearchStore = defineStore('search', {
	state: () => ({
		areaType: "desk",
		capacity: 1,
		date: null,
		timeStart: "00:00",
		timeEnd: "23:59",
		duration: {
			hours: undefined,
			minutes: undefined
		},
		location: null,
		features: [],
		vDate: null,
		errorMessages: {}
	}),

	getters: {
		dateStart() {
			let selectedDate = this.date || new Date()

			const startDate = new Date(selectedDate)
			const startTime = parseTime(this.timeStart)
			startDate.setHours(startTime.hours)
			startDate.setMinutes(startTime.minutes)

			return startDate
		},
		dateEnd() {
			let selectedDate = this.date || new Date()

			const endDate = new Date(selectedDate)
			const endTime = parseTime(this.timeEnd)
			endDate.setHours(endTime.hours)
			endDate.setMinutes(endTime.minutes)

			return endDate
		},
		defaultDuration() {
			if (this.timeStart == null || this.timeEnd == null) {
				return {hours: "00", minutes: "00"}
			}

			const criteria = [
				this.duration.hours == undefined,
				this.duration.hours == "",
				this.duration.minutes == undefined,
				this.duration.minutes == ""
			]

			const bothEmpty = (this.duration.hours == undefined || this.duration.hours == "")
				&& (this.duration.minutes == undefined || this.duration.minutes == "")

			if (criteria.reduce((acc, val) => acc || val, false) && !bothEmpty) {
				return {
					hours: this.duration.hours || "00",
					minutes: this.duration.minutes || "00"
				}
			}

			let minutes = Math.abs(this.vDate.getDiff(this.dateStart, this.dateEnd, "minutes"))
			const hours = Math.floor(minutes / 60)
			minutes = minutes - hours * 60
			return {hours, minutes}
		}
	},

	actions: {
		validate() {
			const validationSchema = this.getValidationSchema()
			let isValid = true;

			for (let [key, rule] of Object.entries(validationSchema)) {
				const result = rule(this[key])
				if (result === true) {
					this.errorMessages[key] = null
				} else {
					if  (typeof result == "string") {
						this.errorMessages[key] = [result]
					} else {
						this.errorMessages[key] = ["An error has occurred"]
					}
					isValid = false
				}
			}

			return isValid;
		},
		setDateUtil(vDate) {
			this.vDate = vDate
		},
		createQueryParams() {
			let queryParams = {
				"area-type": this.areaType,
				capacity: String(this.capacity),
				"super-area": this.location
			}

			if (this.features.length > 0) {
				queryParams["features"] = this.features
			}

			let selectedDate = this.date || new Date()

			const startDate = new Date(selectedDate)
			const startTime = parseTime(this.timeStart)
			startDate.setHours(startTime.hours)
			startDate.setMinutes(startTime.minutes)

			const endDate = new Date(selectedDate)
			const endTime = parseTime(this.timeEnd)
			endDate.setHours(endTime.hours)
			endDate.setMinutes(endTime.minutes)

			queryParams["start-time"] = formatDate(startDate)
			queryParams["end-time"] = formatDate(endDate)

			let realDuration = this.duration;

			if (
				this.duration.hours == undefined
				|| this.duration.minutes == undefined
				|| this.duration.hours == ""
				|| this.duration.minutes == ""
			) {
				realDuration = this.defaultDuration
			}

			queryParams["duration"] = formatDuration(realDuration)

			const nonEmptyParams = {}
			for (let [idx, param] of Object.entries(queryParams)) {
				if (param !== null && param !== undefined) {
					nonEmptyParams[idx] = param
				}
			}

			return nonEmptyParams
		},
		getInitialValues: function() { return {
			areaType: this.areaType,
			capacity: this.capacity,
			date: this.date,
			timeStart: this.timeStart,
			timeEnd: this.timeEnd,
			duration: this.duration,
			location: this.location,
			features: this.features
		}},
		getValidationSchema: function() { return {
			areaType: () => true,
			capacity: (value) => value > 0 || "Capacity must be more than 0",
			date: () => true,
			timeStart: (value) => {
				if (!this.vDate)
					return "An error has occurred"
				if (value == null)
					return "Start time is required"
				return this.timeEnd != null && this.timeEnd != ""
			},
			timeEnd: (value) => {
				if (!this.vDate)
					return "An error has occurred"
				if (value == null)
					return "Start time is required"
				return this.timeStart != null && this.timeStart != ""
			},
			duration: () => true,
			location: () => true,
			features: () => true,
		}}
	}
})