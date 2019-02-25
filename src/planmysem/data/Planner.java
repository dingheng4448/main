package planmysem.data;

import java.time.LocalDate;

import planmysem.data.semester.Day;
import planmysem.data.semester.ReadOnlyDay;
import planmysem.data.semester.Semester;

/**
 * Represents the entire address book. Contains the data of the address book.
 */
public class Planner {
    private final Semester semester;

    /**
     * Creates an empty planner.
     */
    public Planner() {
        semester = new Semester();

        // TODO: initialize semester with data of days & determine values of months, weeks, recess week and reading week
        // Dummy data
        //        HashMap<LocalDate, Day> days = new HashMap<>();
        //        days.put(LocalDate.now(), new Day(DayOfWeek.MONDAY));
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
     * Adds a day to the address book.
     *
     * @throws Semester.DuplicateDayException if an equivalent Day already exists.
     */
    public void addDay(LocalDate date, Day day) throws Semester.DuplicateDayException {
        semester.add(date, day);
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
     * @throws Semester.DayNotFoundException if no such Day could be found.
     */
    public void removeDay(ReadOnlyDay day) throws Semester.DayNotFoundException {
        semester.remove(day);
    }

    /**
     * Removes the equivalent day from the Planner.
     *
     * @throws Semester.DayNotFoundException if no such Day could be found.
     */
    public void removeDay(LocalDate date) throws Semester.DayNotFoundException {
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
        return new Semester(semester);
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
