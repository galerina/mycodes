// You are given the following information, but you may prefer to do some research for yourself.

// 1 Jan 1900 was a Monday.
// Thirty days has September,
// April, June and November.
// All the rest have thirty-one,
// Saving February alone,
// Which has twenty-eight, rain or shine.
// And on leap years, twenty-nine.
// A leap year occurs on any year evenly divisible by 4, but not on a century unless it is divisible by 400.

// How many Sundays fell on the first of the month during the twentieth century (1 Jan 1901 to 31 Dec 2000)?

package main

import (
	"fmt"
	"misc"
)

var daysInMonth []int = []int {
	31, // January
	28, // February
	31, // March
	30, // April
	31, // May
	30, // June
	31, // July
	31, // August
	30, // September
	31, // October
	30, // November
	31, // December
}

type Month int

const (
	january Month = iota
	february
	march
	april
	may
	june
	july
	august
	september
	october
	november
	december
	totalMonths
)

type DayOfWeek int

const (
	sunday DayOfWeek = iota
	monday
	tuesday
	wednesday
	thursday
	friday
	saturday
	totalWeekdays
)
	

type Date struct {
	month Month
	day int
	year int
}

func isLeapYear(year int) bool {
	return misc.IsMultiple(year,4) && 
		(!misc.IsMultiple(year,100) || misc.IsMultiple(year,400))
}

func tomorrow(date Date) Date {
	newDate := date
	if date.day == daysInMonth[date.month] || 
		(isLeapYear(date.year) && date.month == february && date.day == daysInMonth[february] + 1) {
		newDate.month = (date.month + 1) % totalMonths
		newDate.day = 1

		if newDate.month == january {
			newDate.year = date.year + 1
		}
	} else {
		newDate.day = date.day + 1
	}

	return newDate
}

func nextDayOfWeek(day DayOfWeek) DayOfWeek {
	return (day+1) % totalWeekdays
}

// Count the number of Sundays that fall on the 1st day of the month in the span between
// two dates, given the day of the week that the first date falls on
func count1stSundaysInRange(date1DayOfWeek DayOfWeek, date1 Date, date2 Date) int {
	dateCursor := date1
	dayOfWeek := date1DayOfWeek

	// Increment date cursor until we reach date2
	firstSundays := 0
	for ; dateCursor != date2; dateCursor,dayOfWeek = tomorrow(dateCursor),nextDayOfWeek(dayOfWeek) {
		if dateCursor.day == 1 && dayOfWeek == sunday {
			firstSundays++
			fmt.Printf("Sunday: %d-%d-%d\n", dateCursor.month, dateCursor.day, dateCursor.year)
		}
	}

	return firstSundays
}

func calculateWeekday(date1DayOfWeek DayOfWeek, date1 Date, date2 Date) DayOfWeek {
	dateCursor := date1
	dayOfWeek := date1DayOfWeek

	for ; dateCursor != date2; dateCursor,dayOfWeek = tomorrow(dateCursor),nextDayOfWeek(dayOfWeek) {
	}

	return dayOfWeek
}

func main() {
	weekday := calculateWeekday(monday, Date{january,1,1900}, Date{january,1,1901})
	fmt.Println(count1stSundaysInRange(weekday,Date{january,1,1901},Date{december,31,2000}))
}
