package planmysem.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
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
        String acadSem;
        String acadYear;
        String[] semesterDetails;
        int noOfWeeks;
        LocalDate startDate;
        LocalDate endDate;
        List<LocalDate> datesList;
        Map<String, String> acadCalMap;
        HashMap<LocalDate, Day> days = new HashMap<>();
        Set<LocalDate> recessDays = new HashSet<>();
        Set<LocalDate> readingDays = new HashSet<>();
        Set<LocalDate> normalDays = new HashSet<>();

        acadCalMap = getAcadCalMap();
        TemporalField weekField = WeekFields.ISO.weekOfWeekBasedYear();
        int currentWeekOfYear = LocalDate.now().get(weekField);
        semesterDetails = getSemesterDetails(currentWeekOfYear, acadCalMap);
        acadSem = semesterDetails[1];
        acadYear = semesterDetails[2];
        noOfWeeks = Integer.parseInt(semesterDetails[3]);
        startDate = LocalDate.parse(semesterDetails[4]);
        endDate = LocalDate.parse(semesterDetails[5]);

        // Initialises HashMap and Sets of all days in current semester
        datesList = startDate.datesUntil(endDate).collect(Collectors.toList());
        for (LocalDate date: datesList) {
            int weekOfYear = date.get(weekField);
            int firstMonOfYear = date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).getDayOfMonth();
            if (firstMonOfYear == 1) {
                weekOfYear += 1;
            }
            String weekType = acadCalMap.get(Integer.toString(weekOfYear)).split("_")[0];
            days.put(date, new Day(date.getDayOfWeek(), weekType));
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

        semester = new Semester(acadSem, acadYear, days, startDate, endDate, noOfWeeks,
                recessDays, readingDays, normalDays);
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
     * Reads a file containing academic calendar details.
     *
     * @return a map of week of year to academic week
     * throws some exception if a AcademicCalendar.txt is unable to be read.
     */
    private Map<String, String> getAcadCalMap() {
        String filePath = "AcademicCalendar.txt";
        Map<String, String> acadCalMap = null;
        try {
            Stream<String> lines = Files.lines(Paths.get(filePath));
            acadCalMap = lines
                    .collect(Collectors.toMap(key -> key.split(":")[0], val -> val.split(":")[1]));
        } catch (IOException ioe) {
            // TODO: remove displaying of errors
            // What if file is unable to be read?
            ioe.getMessage();
        }
        return acadCalMap;
    }

    /**
     * Initialises current semester's details.
     *
     * @param currentWeekOfYear current week of the year
     * @param acadCalMap used to determine current academic week
     * @return an array of Strings of the current semester's details
     */
    private String[] getSemesterDetails(int currentWeekOfYear, Map<String, String> acadCalMap) {
        String acadWeek = null;
        String acadSem = null;
        String acadYear = null;
        String noOfWeeks = null;
        String[] acadWeekDetails;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        int currentYear = LocalDate.now().getYear();
        int weekOfYear = currentWeekOfYear;

        // Initialise week numbers for certain weeks.
        int firstWeekSemOne = 32;
        int lastWeekSemOne = 49;
        int firstWeekSemOneHol = 50;
        int lastWeekSemOneHol = 2;
        int firstWeekSemTwo = 3;
        int lastWeekSemTwo = 19;
        int firstWeekSemTwoHol = 20;
        int lastWeekSemTwoHol = 31;
        int firstMonOfYear = LocalDate.of(currentYear, 1, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).getDayOfMonth();

        // Readjust weeks if first Monday of the year falls on the 1st.
        if (firstMonOfYear == 1) {
            weekOfYear += 1;
            firstWeekSemOne -= 1;
            lastWeekSemOne -= 1;
            firstWeekSemOneHol -= 1;
            lastWeekSemOneHol -= 1;
            firstWeekSemTwo -= 1;
            lastWeekSemTwo -= 1;
            firstWeekSemTwoHol -= 1;
            lastWeekSemTwoHol -= 1;
        }

        // Set semester details.
        acadWeekDetails = acadCalMap.get(Integer.toString(weekOfYear)).split("_");
        acadWeek = acadWeekDetails[0];
        acadSem = acadWeekDetails[1];
        if ("Vacation".equals(acadWeek) && "Sem 1".equals(acadSem)) {
            noOfWeeks = "5";
            acadYear = "AY" + currentYear + "/" + (currentYear + 1);
            startDate = startDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemOneHol);
            startDate = startDate.with(WeekFields.ISO.dayOfWeek(), 1);
            endDate = LocalDate.of(currentYear + 1, 1, 1);
            endDate = endDate.with(WeekFields.ISO.weekOfWeekBasedYear(), lastWeekSemOneHol);
            endDate = endDate.with(WeekFields.ISO.dayOfWeek(), 7);
        } else if ("Vacation".equals(acadWeek) && "Sem 2".equals(acadSem)) {
            noOfWeeks = "12";
            acadYear = "AY" + (currentYear - 1) + "/" + currentYear;
            startDate = startDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemTwoHol);
            startDate = startDate.with(WeekFields.ISO.dayOfWeek(), 1);
            endDate = endDate.with(WeekFields.ISO.weekOfWeekBasedYear(), lastWeekSemTwoHol);
            endDate = endDate.with(WeekFields.ISO.dayOfWeek(), 7);
        } else if ("Sem 1".equals(acadSem)) {
            noOfWeeks = "18";
            acadYear = "AY" + currentYear + "/" + (currentYear + 1);
            startDate = startDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemOne);
            startDate = startDate.with(WeekFields.ISO.dayOfWeek(), 1);
            endDate = endDate.with(WeekFields.ISO.weekOfWeekBasedYear(), lastWeekSemOne);
            endDate = endDate.with(WeekFields.ISO.dayOfWeek(), 7);
        } else if ("Sem 2".equals(acadSem)) {
            noOfWeeks = "17";
            acadYear = "AY" + (currentYear - 1) + "/" + currentYear;
            startDate = startDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemTwo);
            startDate = startDate.with(WeekFields.ISO.dayOfWeek(), 1);
            endDate = endDate.with(WeekFields.ISO.weekOfWeekBasedYear(), lastWeekSemTwo);
            endDate = endDate.with(WeekFields.ISO.dayOfWeek(), 7);
        }
        return new String[] {acadWeek, acadSem, acadYear, noOfWeeks, startDate.toString(), endDate.toString()};
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
