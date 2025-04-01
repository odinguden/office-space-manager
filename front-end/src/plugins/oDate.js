import { useDate } from "vuetify"

const WEEKDAY_NAMES = [
	"Monday",
	"Tuesday",
	"Wednesday",
	"Thursday",
	"Friday",
	"Saturday",
	"Sunday"
]

const WEEKDAY_MAP = {
	"Mon": 0,
	"Tue": 1,
	"Wed": 2,
	"Thu": 3,
	"Fri": 4,
	"Sat": 5,
	"Sun": 6
}

const MONTH_NAMES = [
	"January",
	"February",
	"March",
	"April",
	"May",
	"June",
	"July",
	"August",
	"September",
	"October",
	"November",
	"December"
]

export const useODate = () => {
	const vDate = useDate()
	const oDate = {}

	oDate.weekdays = WEEKDAY_NAMES

	/**
	 * Gets the name of a specified month.
	 *
	 * @param {Number} monthNumber the month of year, 0 to 11
	 * @returns {String} the name of the specified month
	 */
	oDate.getMonthName = (monthNumber) => {
		return MONTH_NAMES[monthNumber]
	}

	/**
	 * Gets the week number of a specified date.
	 *
	 * @param {Date} inputDate a day in the week from which to grab the week number
	 * @returns {Number} the week number of the input date
	 */
	oDate.getWeekNumber = (inputDate) => {
		const janFirst = new Date(inputDate.getFullYear(), 0, 1)

		const dayOfYear = vDate.getDiff(inputDate, janFirst, "days") + 1
		const dayOfWeek = WEEKDAY_MAP[vDate.format(inputDate, "weekdayShort")] + 1
		const weekOfYear = Math.floor((10 + dayOfYear - dayOfWeek) / 7)

		return weekOfYear || 53
	}

	/**
	 * Returns `num` amount of weeks from the first week of a month, including
	 * weeks that might fall out of bound for that month.
	 *
	 * @param {Date} inputDate a date containing at least a month and a year
	 * @param {Number} num the number of weeks to get from the start of the month
	 * @returns {Object} an array of objects containing the weekdays and the weeknumbers of weeks
	 */
	oDate.getWeeksOfMonth = (year, month, num=6) => {
		const date = new Date(year, month)

		const weeks = []

		const startOfMonth = vDate.startOfMonth(date)
		const startOfWeekMonth = vDate.startOfWeek(startOfMonth, 1)
		for (let w = 0; w < num; w++) {
			const week = {number: 0, days: []}
			const startOfWeek = vDate.addDays(startOfWeekMonth, w * 7)

			for (let d = 0; d < 7; d++) {
				week.days.push(vDate.addDays(startOfWeek, d))
			}

			week.number = oDate.getWeekNumber(vDate.toJsDate(week.days[4]))
			weeks.push(week)
		}

		return weeks
	}

	return oDate
}