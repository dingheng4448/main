package planmysem.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import planmysem.data.semester.Day;
import planmysem.data.semester.ReadOnlyDay;
import planmysem.data.semester.Semester;
import planmysem.data.slot.Slot;

/**
 * Represents the entire Planner. Contains the data of the Planner.
 */
public class Planner {
    private final Semester semester;

    /**
     * Creates an empty planner.
     */
    public Planner() {
        String filePath = "AcademicCalendar.txt";
        String acadWeek = null;
        String acadYear = null;
        String acadSem = null;
        int noOfWeeks = 0;
        Calendar cal = Calendar.getInstance();
        int currentWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        int currentYear = cal.get(Calendar.YEAR);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        // TODO: add recess days, reading days, normal days
        Set<LocalDate> recessDays = new HashSet<>();
        Set<LocalDate> readingDays = new HashSet<>();
        Set<LocalDate> normalDays = new HashSet<>();
        Map<String, String> acadCalMap = null;

        // Read AcademicCalendar.txt to get current academic week
        try {
            Stream<String> lines = Files.lines(Paths.get(filePath));
            acadCalMap = lines
                    .collect(Collectors.toMap(key -> key.split(":")[0], val -> val.split(":")[1]));
            acadWeek = acadCalMap.get(Integer.toString(currentWeekOfYear));
        } catch (IOException ioe) {
            // TODO: remove displaying of errors
            // What if file is unable to be read?
            ioe.getMessage();
        }
        //System.out.println(acadCalMap);

        // Set variables if it is currently vacation
        if (acadWeek != null && acadWeek.equals("Vacation")) {
            acadSem = "Vacation";
            if (currentWeekOfYear < 3 || currentWeekOfYear > 49) {
                noOfWeeks = 5;

                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                cal.set(Calendar.WEEK_OF_YEAR, 50);
                startDate = LocalDate.of(currentYear, 12, cal.get(Calendar.DATE));

                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                cal.set(Calendar.WEEK_OF_YEAR, 2);
                endDate = LocalDate.of(currentYear, 1, cal.get(Calendar.DATE));
            }
            if (currentWeekOfYear > 19 && currentWeekOfYear < 32) {
                noOfWeeks = 12;

                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                cal.set(Calendar.WEEK_OF_YEAR, 20);
                startDate = LocalDate.of(currentYear, 5, cal.get(Calendar.DATE));

                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                cal.set(Calendar.WEEK_OF_YEAR, 31);
                endDate = LocalDate.of(currentYear, 8, cal.get(Calendar.DATE));
            }
        }

        // Set variables if it is currently not vacation
        if (currentWeekOfYear > 31 && currentWeekOfYear < 50) {
            acadYear = "AY" + currentYear + "/" + (currentYear + 1);
            acadSem = "Sem 1";
            noOfWeeks = 18;

            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.set(Calendar.WEEK_OF_YEAR, 32);
            startDate = LocalDate.of(currentYear, 8, cal.get(Calendar.DATE));

            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            cal.set(Calendar.WEEK_OF_YEAR, 49);
            endDate = LocalDate.of(currentYear, 12, cal.get(Calendar.DATE));
        }
        if (currentWeekOfYear > 2 && currentWeekOfYear < 20) {
            acadYear = "AY" + (currentYear - 1) + "/" + currentYear;
            acadSem = "Sem 2";
            noOfWeeks = 17;

            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.set(Calendar.WEEK_OF_YEAR, 3);
            startDate = LocalDate.of(currentYear, 1, cal.get(Calendar.DATE));

            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            cal.set(Calendar.WEEK_OF_YEAR, 19);
            endDate = LocalDate.of(currentYear, 5, cal.get(Calendar.DATE));
        }

        // Initialise hashmap of all days in current semester
        HashMap<LocalDate, Day> days = new HashMap<>();
        List<LocalDate> datesList = startDate.datesUntil(endDate).collect(Collectors.toList());
        for (LocalDate date: datesList) {
            TemporalField weekField = WeekFields.ISO.weekOfWeekBasedYear();
            String weekOfYear = Integer.toString(date.get(weekField));
            String weekType = acadCalMap.get(weekOfYear);
            days.put(date, new Day(date.getDayOfWeek(), weekType));
            // TODO: Add type of the day here, recess/reading or normal
            switch (weekType) {
            case "Recess Week":
                recessDays.add(date);
                break;
            case "Reading Week":
                readingDays.add(date);
                break;
            default:
                normalDays.add(date);
                break;
            }
        }
        //System.out.println(recessDays);
        //System.out.println(readingDays);
        //System.out.println(normalDays);
        semester = new Semester(acadSem, acadYear, days, startDate, endDate, noOfWeeks,
                recessDays, readingDays, normalDays);
        // TODO: set constants for fixed numbers, simplify/optimise code, handle ioe exception
    }

    /**
     * Constructs a Planner with the given data.
     *
     * @param semester external changes to this will not affect this Planner
     */
    public Planner(Semester semester) {
        this.semester = new Semester(semester);
    }

    public static Planner empty() {
        return new Planner();
    }

    /**
     * Adds a day to the Planner.
     *
     * @throws Semester.DuplicateDayException if a date is not found in the semester.
     */
    public void addDay(LocalDate date, Day day) throws Semester.DuplicateDayException {
        semester.addDay(date, day);
    }

    /**
     * Adds a slot to the Planner.
     *
     */
    public void addSlot(LocalDate date, Slot slot) throws Semester.DateNotFoundException {
        semester.addSlot(date, slot);
    }

    /**
     * Checks if an equivalent Day exists in the address book.
     */
    public boolean containsDay(ReadOnlyDay day) {
        return semester.contains(day);
    }

    /**
     * Checks if an equivalent Day exists in the Planner.
     */
    public boolean containsDay(LocalDate date) {
        return semester.contains(date);
    }

    /**
     * Removes the equivalent day from the Planner.
     *
     * @throws Semester.DateNotFoundException if no such Day could be found.
     */
    public void removeDay(ReadOnlyDay day) throws Semester.DateNotFoundException {
        semester.remove(day);
    }

    /**
     * Removes the equivalent day from the Planner.
     *
     * @throws Semester.DateNotFoundException if no such Day could be found.
     */
    public void removeDay(LocalDate date) throws Semester.DateNotFoundException {
        semester.remove(date);
    }

    /**
     * Clears all days from the Planner.
     */
    public void clearDays() {
        semester.clearDays();
    }

    /**
     * Clears all days from the Planner.
     */
    public void clearSlots() {
        semester.clearSlots();
    }

    /**
     * Defensively copy the Semester in the Planner at the time of the call.
     */
    public Semester getSemester() {
        return semester;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Planner // instanceof handles nulls
                && this.semester.equals(((Planner) other).semester));
    }

    @Override
    public int hashCode() {
        return semester.hashCode();
    }
}
