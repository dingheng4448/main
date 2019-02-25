package planmysem.data.semester;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import planmysem.data.exception.DuplicateDataException;

/**
 * A list of days. Does not allow null elements or duplicates.
 *
 * @see Day#equals(Object)
 */
public class Semester implements ReadOnlySemester {
    private final String name;
    private final String academicYear;
    private final HashMap<LocalDate, Day> days = new HashMap<>();
    private final String startDate;
    private final String endDate;
    private final int noOfWeeks;

    /**
     * Constructs empty semester.
     */
    public Semester() {
        this.name = null;
        this.academicYear = null;
        this.startDate = null;
        this.endDate = null;
        this.noOfWeeks = 0;
    }

    /**
     * Constructs a semester with the given Days.
     */
    public Semester(String name, String academicYear, HashMap<LocalDate, Day> days, String startDate,
                    String endDate, int noOfWeeks) {
        this.name = name;
        this.academicYear = academicYear;
        this.days.putAll(days);
        this.startDate = startDate;
        this.endDate = endDate;
        this.noOfWeeks = noOfWeeks;
    }

    /**
     * Constructs a shallow copy of the Semester.
     */
    public Semester(Semester source) {
        this.name = source.getName();
        this.academicYear = source.getAcademicYear();
        this.days.putAll(source.days);
        this.startDate = source.startDate;
        this.endDate = source.endDate;
        this.noOfWeeks = source.noOfWeeks;

    }

    /**
     * Adds a person to the list.
     *
     * @throws DuplicateDayException if the Day to add is a duplicate of an existing Day in the list.
     */
    public void add(LocalDate date, Day day) throws DuplicateDayException {
        if (contains(day)) {
            throw new DuplicateDayException();
        }
        days.put(date, day);
    }

    /**
     * Removes the equivalent Day from the list.
     *
     * @throws DayNotFoundException if no such Day could be found in the list.
     */
    public void remove(ReadOnlyDay day) throws DayNotFoundException {
        if (!contains(day)) {
            throw new DayNotFoundException();
        }
        days.remove(day);
    }

    /**
     * Removes the equivalent Day from the list.
     *
     * @throws DayNotFoundException if no such Day could be found in the list.
     */
    public void remove(LocalDate date) throws DayNotFoundException {
        if (!contains(date)) {
            throw new DayNotFoundException();
        }
        days.remove(date);
    }

    /**
     * Clears all Days from the address book.
     */
    public void clearDays() {
        days.clear();
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
    public String getName() {
        return name;
    }

    @Override
    public String getAcademicYear() {
        return academicYear;
    }

    @Override
    public HashMap<LocalDate, Day> getDays() {
        return days;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public int getNoOfWeeks() {
        return noOfWeeks;
    }

    /**
     * Signals that an operation would have violated the 'no duplicates' property.
     */
    public static class DuplicateDayException extends DuplicateDataException {
        protected DuplicateDayException() {
            super("Operation would result in duplicate days");
        }
    }

    /**
     * Signals that an operation targeting a specified Day in the list would fail because
     * there is no such matching Day in the list.
     */
    public static class DayNotFoundException extends Exception {
    }
}
