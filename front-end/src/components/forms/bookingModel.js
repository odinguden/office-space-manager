function toDigits(number, digits=2) {
	return number.toLocaleString("en-us", { minimumIntegerDigits: 2, useGrouping: false })
}

function formatForDate(date) {
	const year = date.getFullYear()
	const month = toDigits(date.getMonth())
	const day = toDigits(date.getDate())
	return `${year}-${month}-${day}`
}

function formatForTime(time) {
	const hour = toDigits(time.getHours())
	const minutes = toDigits(time.getMinutes())

	return `${hour}:${minutes}`
}

export default class BookingModel {
	values = {
		room: {
			value: "A123",
			readonly: true
		},
		day: {
			value: formatForDate(new Date()),
			readonly: false
		},
		start: {
			value: formatForTime(new Date()),
			readonly: false
		},
		end: {
			value: undefined,
			readonly: false
		}
	}

	constructor(room, day, start, end) {
		if (room !== undefined) {
			this.values.room.value = room
			this.values.room.readonly = true
		}

		if (day !== undefined) {
			this.values.day.value = day
			this.values.day.readonly = true
		}

		if (start !== undefined) {
			this.values.start.value = start
			this.values.start.readonly = true
		}

		if (end !== undefined) {
			this.values.end.value = end
			this.values.end.readonly = true
		}
	}
}