const pad = (str) => String(str).padStart(2, '0')

export const timeUtil = {
	toTimeString(input) {
		return `${pad(input.getHours())}:${pad(input.getMinutes())}`
	},

	fromTimeString(input) {
		const [hours, minutes] = input.split(":").map(Number)
		const date = new Date(0)
		date.setHours(hours, minutes, 0, 0)
		return date;
	},

	formatDurationForSend(input) {
		return `PT${input.hours}H${input.minutes}M`
	},

	formatDateForSend(input) {
		return `${input.getFullYear()}-${pad(input.getMonth() + 1)}-${pad(input.getDate())}`
			+ `T${pad(input.getHours())}:${pad(input.getMinutes())}:${pad(input.getSeconds())}`
	},
}