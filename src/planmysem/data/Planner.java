package planmysem.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javafx.util.Pair;
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
    //    public void addDay(LocalDate date, Day day) throws Semester.DuplicateDayException {
    //        semester.addDay(date, day);
    //    }

    /**
     * Adds a slot to the Planner.
     *
     */
    public Day addSlot(LocalDate date, Slot slot) throws Semester.DateNotFoundException {
        return semester.addSlot(date, slot);
    }

    /**
     * Removes a Slot in the Planner.
     */
    public void removeSlot(LocalDate date, ReadOnlySlot slot) {
        semester.removeSlot(date, slot);
    }

    /**
     * Edit specific slot within the planner.
     *
     * @throws IllegalValueException if a targetDate is not found in the semester.
     */
    public void editSlot(LocalDate targetDate, ReadOnlySlot targetSlot, LocalDate date,
                         LocalTime startTime, int duration, String name, String location,
                         String description, Set<Tag> tags) throws IllegalValueException {
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

    /**
     * gets all days in the Planner.
     */
    public HashMap<LocalDate, Day> getAllDays() {
        return semester.getDays();
    }

    /**
     * gets specific day in the Planner.
     */
    public Day getDay(LocalDate date) {
        return getAllDays().get(date);
    }

    /**
     * gets all slots in the Planner containing all specified tags.
     */
    public Map<LocalDate, Pair<ReadOnlyDay, ReadOnlySlot>> getSlots(Set<Tag> tags) {
        final Map<LocalDate, Pair<ReadOnlyDay, ReadOnlySlot>> selectedSlots = new TreeMap<>();

        for (Map.Entry<LocalDate, Day> entry : getAllDays().entrySet()) {
            for (Slot slot : entry.getValue().getSlots()) {
                if (slot.getTags().containsAll(tags)) {
                    selectedSlots.put(entry.getKey(), new Pair<>(entry.getValue(), slot));
                }
            }
        }

        return selectedSlots;
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
