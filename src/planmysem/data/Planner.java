package planmysem.data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import planmysem.data.exception.IllegalValueException;
import planmysem.data.semester.Day;
import planmysem.data.semester.ReadOnlyDay;
import planmysem.data.semester.Semester;
import planmysem.data.slot.ReadOnlySlot;
import planmysem.data.slot.Slot;
import planmysem.data.tag.Tag;

/**
 * Represents the entire Planner. Contains the data of the Planner.
 */
public class Planner {
    private final Semester semester;

    /**
     * Creates an empty planner.
     */
    public Planner() {
        semester = Semester.generateSemester(LocalDate.now());
    }

    /**
     * Constructs a Planner with the given data.
     *
     * @param semester external changes to this will not affect this Planner
     */
    public Planner(Semester semester) {
        this.semester = new Semester(semester);
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
     * Get set of slots which contain all specified tags.
     */
    public Map<LocalDateTime, ReadOnlySlot> getSlots(Set<Tag> tags) {
        return semester.getSlots(tags);
    }


    /**
     * Adds a slot to the Planner.
     *
     */
    public Day addSlot(LocalDate date, Slot slot) throws Semester.DateNotFoundException {
        return semester.addSlot(date, slot);
    }

    /**
     * Edit specific slot within the planner.
     *
     * @throws Semester.DateNotFoundException if a targetDate is not found in the semester.
     * @throws IllegalValueException if a targetDate is not found in the semester.
     */
    public void editSlot(LocalDate targetDate, ReadOnlySlot targetSlot, LocalDate date,
                         LocalTime startTime, int duration, String name, String location,
                         String description, Set<Tag> tags)
            throws Semester.DateNotFoundException, IllegalValueException {
        semester.editSlot(targetDate, targetSlot, date, startTime, duration, name, location, description, tags);
    }

    /**
     * Checks if an slot exists in planner.
     */
    public boolean containsSlot(LocalDate date, ReadOnlySlot slot) {
        return semester.contains(date, slot);
    }

    /**
     * Checks if an equivalent Day exists in the Planner.
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
     * Clears all slots from the Planner.
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
