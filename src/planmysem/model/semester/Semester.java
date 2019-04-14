package planmysem.model.semester;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import planmysem.common.Clock;
import planmysem.model.slot.ReadOnlySlot;
import planmysem.model.slot.Slot;

/**
 * Wraps all data of an academic semester.
 */
public class Semester implements ReadOnlySemester {
    private static HashMap<Integer, String> acadCal = new HashMap<>();

    // These variables hold the necessary details of a semester.
    private final String name;
    private final String academicYear;
    private final HashMap<LocalDate, Day> days = new HashMap<>();
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int noOfWeeks;

    // These variables aid in making searches more effective.
    private final Set<LocalDate> recessDays = new HashSet<>();
    private final Set<LocalDate> readingDays = new HashSet<>();
    private final Set<LocalDate> normalDays = new HashSet<>();
    private final Set<LocalDate> examDays = new HashSet<>();

    /**
     * Constructs a Semester from given details of a semester.
     */
    public Semester(String name, String academicYear, HashMap<LocalDate, Day> days, LocalDate startDate,
                    LocalDate endDate, int noOfWeeks, Set<LocalDate> recessDays, Set<LocalDate> readingDays,
                    Set<LocalDate> normalDays, Set<LocalDate> examDays) {

        this.name = name;
        this.academicYear = academicYear;
        this.days.putAll(days);
        this.startDate = startDate;
        this.endDate = endDate;
        this.noOfWeeks = noOfWeeks;

        this.recessDays.addAll(recessDays);
        this.readingDays.addAll(readingDays);
        this.normalDays.addAll(normalDays);
        this.examDays.addAll(examDays);
    }

    /**
     * Constructs a shallow copy of a given Semester or generate a new Semester.
     */
    public Semester(Semester source) {

        // Generate a new semester if the current date does not exist in the source semseter.
        LocalDate startDateFromFile = source.startDate;
        LocalDate endDateFromFile = source.endDate;
        LocalDate currentDate = LocalDate.now(Clock.get());
        Semester semester;
        if (currentDate.isBefore(startDateFromFile) || currentDate.isAfter(endDateFromFile)) {
            semester = generateSemester(currentDate);
        } else {
            semester = source;
        }

        this.name = semester.getName();
        this.academicYear = semester.getAcademicYear();
        this.days.putAll(semester.days);
        this.startDate = semester.startDate;
        this.endDate = semester.endDate;
        this.noOfWeeks = semester.noOfWeeks;

        this.recessDays.addAll(semester.recessDays);
        this.readingDays.addAll(semester.readingDays);
        this.normalDays.addAll(semester.normalDays);
        this.examDays.addAll(semester.examDays);
    }

    /**
     * Generates current Semester based on current date.
     * As long as the current date falls within a semester, the generated semester is always the same.
     *
     * @param currentDate the current date when the program is run
     * @return the current Semester object
     */
    public static Semester generateSemester(LocalDate currentDate) {
        String acadSem;
        String acadYear;
        HashMap<LocalDate, Day> days = new HashMap<>();
        LocalDate startDate;
        LocalDate endDate;
        int noOfWeeks;

        Set<LocalDate> recessDays = new HashSet<>();
        Set<LocalDate> readingDays = new HashSet<>();
        Set<LocalDate> normalDays = new HashSet<>();
        Set<LocalDate> examDays = new HashSet<>();

        HashMap<Integer, String> acadCalMap;
        acadCalMap = generateAcademicCalMap(currentDate);
        acadCal = acadCalMap;

        String[] semesterDetails;
        semesterDetails = getSemesterDetails(currentDate, acadCalMap);
        acadSem = semesterDetails[0];
        acadYear = semesterDetails[1];
        noOfWeeks = Integer.parseInt(semesterDetails[2]);
        startDate = LocalDate.parse(semesterDetails[3]);
        endDate = LocalDate.parse(semesterDetails[4]);

        // Initialise HashMap and Sets of all days in current semester
        List<LocalDate> datesList;
        datesList = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
        for (LocalDate date: datesList) {
            int weekOfYear = date.get(WeekFields.ISO.weekOfWeekBasedYear());
            String weekType = acadCalMap.get(weekOfYear).split("_")[0];
            days.put(date, new Day(date.getDayOfWeek(), weekType));
            switch (weekType) {
            case "Recess Week":
                recessDays.add(date);
                break;
            case "Reading Week":
                readingDays.add(date);
                break;
            case "Examination Week":
                examDays.add(date);
                break;
            default:
                normalDays.add(date);
                break;
            }
        }

        return new Semester(acadSem, acadYear, days, startDate, endDate, noOfWeeks,
                recessDays, readingDays, normalDays, examDays);
    }

    /**
     * Generates academic calendar for a given date.
     *
     * @param date used to determine academic year
     * @return details of academic calendar
     */
    private static HashMap<Integer, String> generateAcademicCalMap(LocalDate date) {
        HashMap<Integer, String> acadCalMap = new HashMap<>();

        // Determine dates of semester 1 and 2 based on the given month.
        final int august = Month.AUGUST.getValue();
        int givenMonth = date.getMonthValue();
        int givenYear = date.getYear();
        LocalDate semOneStartDate = date;
        LocalDate semTwoEndDate = date;
        int semOneStartWeek;
        int semTwoEndWeek;
        if (givenMonth < august) {
            semOneStartDate = semOneStartDate.withYear(givenYear - 1).withMonth(august)
                    .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
            semOneStartWeek = semOneStartDate.get(WeekFields.ISO.weekOfWeekBasedYear());
            semTwoEndDate = semTwoEndDate.withYear(givenYear).withMonth(august)
                    .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).minusDays(1);
            semTwoEndWeek = semTwoEndDate.get(WeekFields.ISO.weekOfWeekBasedYear());
        } else {
            semOneStartDate = semOneStartDate.withYear(givenYear).withMonth(august)
                    .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
            semOneStartWeek = semOneStartDate.get(WeekFields.ISO.weekOfWeekBasedYear());
            semTwoEndDate = semTwoEndDate.withYear(givenYear + 1).withMonth(august)
                    .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).minusDays(1);
            semTwoEndWeek = semTwoEndDate.get(WeekFields.ISO.weekOfWeekBasedYear());
        }

        // The following code assigns each week of the year to the appropriate academic week for the academic year.
        // Sem 1 - Orientation Week
        acadCalMap.put(semOneStartWeek, "Orientation Week_Sem 1");

        // Sem 1 - Week 1 to 6
        int acadWeekNo = 1;
        for (int i = semOneStartWeek + 1; i < semOneStartWeek + 7; i++) {
            acadCalMap.put(i, "Week " + acadWeekNo + "_Sem 1");
            acadWeekNo++;
        }

        // Sem 1 - Recess Week
        acadCalMap.put(semOneStartWeek + 7, "Recess Week_Sem 1");

        // Sem 1 - Week 7 to 13
        acadWeekNo = 7;
        for (int i = semOneStartWeek + 8; i < semOneStartWeek + 15; i++) {
            acadCalMap.put(i, "Week " + acadWeekNo + "_Sem 1");
            acadWeekNo++;
        }

        // Sem 1 - Reading & Examination Weeks
        acadCalMap.put(semOneStartWeek + 15, "Reading Week_Sem 1");
        acadCalMap.put(semOneStartWeek + 16, "Examination Week_Sem 1");
        acadCalMap.put(semOneStartWeek + 17, "Examination Week_Sem 1");

        // Sem 1 - Vacation
        int noOfWeeksInYear = (int) semOneStartDate.range(WeekFields.ISO.weekOfWeekBasedYear()).getMaximum();
        int vacationWeekNo = semOneStartWeek + 18;
        int semTwoStartWeek;
        semTwoStartWeek = 1;
        for (int i = 0; i < 5; i++) {
            if ((vacationWeekNo + i) <= noOfWeeksInYear) {
                acadCalMap.put(vacationWeekNo + i, "Vacation_Sem 1");
            } else {
                acadCalMap.put(semTwoStartWeek++, "Vacation_Sem 1");
            }
        }

        // Sem 2 - Week 1 to 6
        acadWeekNo = 1;
        for (int i = semTwoStartWeek; i < semTwoStartWeek + 6; i++) {
            acadCalMap.put(i, "Week " + acadWeekNo + "_Sem 2");
            acadWeekNo++;
        }

        // Sem 2 - Recess Week
        acadCalMap.put(semTwoStartWeek + 6, "Recess Week_Sem 2");

        // Sem 2 - Week 7 to 13
        acadWeekNo = 7;
        for (int i = semTwoStartWeek + 7; i < semTwoStartWeek + 14; i++) {
            acadCalMap.put(i, "Week " + acadWeekNo + "_Sem 2");
            acadWeekNo++;
        }

        // Sem 2 - Reading & Examination Weeks
        acadCalMap.put(semTwoStartWeek + 14, "Reading Week_Sem 2");
        acadCalMap.put(semTwoStartWeek + 15, "Examination Week_Sem 2");
        acadCalMap.put(semTwoStartWeek + 16, "Examination Week_Sem 2");

        // Sem 2 - Vacation
        vacationWeekNo = semTwoStartWeek + 17;
        while (vacationWeekNo <= semTwoEndWeek) {
            acadCalMap.put(vacationWeekNo++, "Vacation_Sem 2");
        }

        return acadCalMap;
    }

    /**
     * Initialises and returns details of an academic semester from a given date and academic calendar.
     *
     * @param date used to determine academic year
     * @param acadCalMap used to determine academic week
     * @return an array of Strings of an academic semester's details
     */
    private static String[] getSemesterDetails(LocalDate date, HashMap<Integer, String> acadCalMap) {
        String acadSem;
        String acadYear = null;
        LocalDate startDate = date;
        LocalDate endDate = date;
        String noOfWeeks = null;

        // Get week numbers for semester 1 and 2 from the academic calendar map.
        int firstWeekSemOne = 0;
        int firstWeekSemOneHol = 0;
        int lastWeekSemOneHol = 0;
        int firstWeekSemTwo = 0;
        int firstWeekSemTwoHol = 0;
        for (Map.Entry<Integer, String> entry: acadCalMap.entrySet()) {
            if ("Orientation Week_Sem 1".equals(entry.getValue())) {
                firstWeekSemOne = entry.getKey();
            } else if ("Examination Week_Sem 1".equals(entry.getValue())) {
                firstWeekSemOneHol = entry.getKey() + 1;
            } else if ("Week 1_Sem 2".equals(entry.getValue())) {
                lastWeekSemOneHol = entry.getKey() - 1;
                firstWeekSemTwo = entry.getKey();
            } else if ("Examination Week_Sem 2".equals(entry.getValue())) {
                firstWeekSemTwoHol = entry.getKey() + 1;
            }
        }

        // Get semester details from given date, academic calendar map and the week numbers retrieved above.
        int givenYear = date.getYear();
        int givenWeekOfYear = date.get(WeekFields.ISO.weekOfWeekBasedYear());
        String[] acadWeekDetails = acadCalMap.get(givenWeekOfYear).split("_");
        String acadWeek = acadWeekDetails[0];
        acadSem = acadWeekDetails[1];
        if ("Vacation".equals(acadWeek) && "Sem 1".equals(acadSem)) {
            noOfWeeks = "5";
            if (givenWeekOfYear < 4) {
                acadYear = "AY" + (givenYear - 1) + "/" + givenYear;
                startDate = startDate.withYear(givenYear - 1);
            } else {
                acadYear = "AY" + givenYear + "/" + (givenYear + 1);
                endDate = endDate.withYear(givenYear + 1);
            }
            startDate = startDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemOneHol);
            startDate = startDate.with(WeekFields.ISO.dayOfWeek(), 1);
            endDate = endDate.with(WeekFields.ISO.weekOfWeekBasedYear(), lastWeekSemOneHol);
            endDate = endDate.with(WeekFields.ISO.dayOfWeek(), 7);
        } else if ("Vacation".equals(acadWeek) && "Sem 2".equals(acadSem)) {
            noOfWeeks = "12";
            acadYear = "AY" + (givenYear - 1) + "/" + givenYear;
            startDate = startDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemTwoHol);
            startDate = startDate.with(WeekFields.ISO.dayOfWeek(), 1);
            endDate = endDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemTwoHol + 11);
            endDate = endDate.with(WeekFields.ISO.dayOfWeek(), 7);
        } else if ("Sem 1".equals(acadSem)) {
            noOfWeeks = "18";
            acadYear = "AY" + givenYear + "/" + (givenYear + 1);
            startDate = startDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemOne);
            startDate = startDate.with(WeekFields.ISO.dayOfWeek(), 1);
            endDate = endDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemOne + 17);
            endDate = endDate.with(WeekFields.ISO.dayOfWeek(), 7);
        } else if ("Sem 2".equals(acadSem)) {
            noOfWeeks = "17";
            acadYear = "AY" + (givenYear - 1) + "/" + givenYear;
            startDate = startDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemTwo);
            startDate = startDate.with(WeekFields.ISO.dayOfWeek(), 1);
            endDate = endDate.with(WeekFields.ISO.weekOfWeekBasedYear(), firstWeekSemTwo + 16);
            endDate = endDate.with(WeekFields.ISO.dayOfWeek(), 7);
        }

        return new String[] {acadSem, acadYear, noOfWeeks, startDate.toString(), endDate.toString()};
    }

    /**
     * Adds a Slot to the Semester.
     *
     * @throws DateNotFoundException if a date is not found in the semester.
     */
    public Day addSlot(LocalDate date, Slot slot) throws DateNotFoundException {
        if (date == null || (date.isBefore(startDate) || date.isAfter(endDate))) {
            throw new DateNotFoundException();
        }
        days.get(date).addSlot(slot);
        return days.get(date);
    }

    /**
     * Edits a Slot in the Semester.
     */
    public void editSlot(LocalDate targetDate, ReadOnlySlot targetSlot, LocalDate date, LocalTime startTime,
                         int duration, String name, String location, String description, Set<String> tags) {
        Slot editingSlot = days.get(targetDate).getSlots().stream()
            .filter(s -> s.equals(targetSlot)).findAny().orElse(null);

        if (date != null) {
            Slot savedSlot = new Slot(editingSlot);
            days.get(date).addSlot(savedSlot);
            days.get(targetDate).removeSlot(editingSlot);
            editingSlot = savedSlot;
        }
        if (startTime != null) {
            editingSlot.setStartTime(startTime);
        }
        if (duration != -1) {
            editingSlot.setDuration(duration);
        }

        editingSlot.setName(name);
        editingSlot.setLocation(location);
        editingSlot.setDescription(description);
        if (tags.size() > 0) {
            editingSlot.setTags(tags);
        }
    }

    /**
     * Get set of slots which contain all specified tags.
     */
    public Map<LocalDateTime, ReadOnlySlot> getSlots(Set<String> tags) {
        Map<LocalDateTime, ReadOnlySlot> selectedSlots = new TreeMap<>();

        for (Map.Entry<LocalDate, Day> day : days.entrySet()) {
            for (Slot slot : day.getValue().getSlots()) {
                if (slot.getTags().containsAll(tags)) {
                    selectedSlots.put(LocalDateTime.of(day.getKey(), slot.getStartTime()), slot);
                }
            }
        }

        return selectedSlots;
    }

    /**
     * Removes a Slot to the Semester.
     */
    public void removeSlot(LocalDate date, ReadOnlySlot slot) {
        days.get(date).removeSlot(slot);
    }

    /**
     * Clears all Days from the address book.
     */
    public void clearSlots() {
        for (Map.Entry<LocalDate, Day> day : days.entrySet()) {
            day.getValue().clear();
        }
    }

    /**
     * Checks if the list contains an equivalent slot as the given argument.
     */
    public boolean contains(LocalDate date, ReadOnlySlot slot) {
        return days.get(date).contains(slot);
    }

    /**
     * Checks if the list contains an equivalent Day as the given argument.
     */
    public boolean contains(ReadOnlyDay day) {
        return days.containsValue(day);
    }

    /**
     * Checks if the list contains an equivalent date as the given argument.
     */
    public boolean contains(LocalDate date) {
        return days.containsKey(date);
    }

    @Override
    public HashMap<Integer, String> getAcadCal() {
        return acadCal;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAcademicYear() {
        return academicYear;
    }

    @Override
    public void setDays(HashMap<LocalDate, Day> days) {
        this.days.clear();

        for (Map.Entry<LocalDate, Day> entry : days.entrySet()) {
            this.days.put(entry.getKey(), new Day(entry.getValue()));
        }
    }

    @Override
    public HashMap<LocalDate, Day> getDays() {
        return days;
    }

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public int getNoOfWeeks() {
        return noOfWeeks;
    }

    @Override
    public Set<LocalDate> getRecessDays() {
        return recessDays;
    }

    @Override
    public Set<LocalDate> getReadingDays() {
        return readingDays;
    }

    @Override
    public Set<LocalDate> getNormalDays() {
        return normalDays;
    }

    @Override
    public Set<LocalDate> getExamDays() {
        return examDays;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Semester // instanceof handles nulls
                && this.name.equals(((Semester) other).name)
                && this.academicYear.equals(((Semester) other).academicYear)
                && this.days.equals(((Semester) other).days)
                && this.startDate.equals(((Semester) other).startDate)
                && this.endDate.equals(((Semester) other).endDate)
                && this.noOfWeeks == (((Semester) other).noOfWeeks)
                && this.recessDays.equals(((Semester) other).recessDays)
                && this.readingDays.equals(((Semester) other).readingDays)
                && this.normalDays.equals(((Semester) other).normalDays)
                && this.examDays.equals(((Semester) other).examDays));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, academicYear, days, startDate, endDate, noOfWeeks,
                recessDays, readingDays, normalDays, examDays);
    }

    /**
     * Signals that an operation targeting a specified Day in the list would fail because
     * there is no such matching Day in the list.
     */
    public static class DateNotFoundException extends Exception {
    }
}
